// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.runtime.ThreadUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class EncryptionSymmetricAsyncStream {

    private final int blockSize = 2048;

    private InputStream stream;
    private Queue<byte[]> queue = new ArrayDeque<>();
    private EncryptionStreamProcessor encryptionStreamProcessor = new EncryptionStreamProcessor() {
        @Override
        public void processData(byte[] bytes) {
            queue.add(bytes);
        }
    };

    private boolean used = false;
    private boolean done = false;

    private final EncryptionSymmetricAsyncStream instance = this;

    private static ThreadGroup threadGroup = new ThreadGroup(ThreadUtils.yapiGroup, "EncryptionSymmetricAsyncStream");

    public EncryptionSymmetricAsyncStream(FileInputStream fileInputStream) {
        this.stream = fileInputStream;
    }

    public EncryptionSymmetricAsyncStream(ByteArrayInputStream byteArrayInputStream) {
        this.stream = byteArrayInputStream;
    }

    public EncryptionSymmetricAsyncStream(InputStream inputStream) {
        this.stream = inputStream;
    }

    private class AsyncResult {

        private int id;

        AsyncResult(int id) {
            this.id = id;
        }

        private byte[] bytes = null;

        @Override
        public String toString() {
            return "AsyncResult{" +
                    "id=" + id +
                    '}';
        }
    }

    public void setEncryptionStreamProcessor(EncryptionStreamProcessor encryptionStreamProcessor) {
        if (used) throw new IllegalStateException();

        this.encryptionStreamProcessor = encryptionStreamProcessor;
    }

    public void encrypt(String key) {
        if (used) throw new IllegalStateException();
        used = true;

        Runnable runnable = () -> {
            String k = key;
            int id = 0;
            Queue<AsyncResult> results = new ArrayDeque<>();
            try {
                while (stream.available() > 0) {
                    byte[] bytes = stream.readNBytes(blockSize - 41);
                    AsyncResult asyncResult = new AsyncResult(id++);
                    results.add(asyncResult);

                    process(asyncResult, k, bytes, false);
                    process(results);
                    k = EncryptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (results.size() > 15) {
                        process(results);

                        try {
                            synchronized (instance) {
                                wait(100);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    while (queue.size() > 7) {
                        try {
                            synchronized (instance) {
                                wait(100);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!results.isEmpty()) {
                process(results);
            }
            done = true;
        };
        Thread t = new Thread(threadGroup, runnable);
        t.setName("EncryptionSymmetricAsyncStream - Encrypt");
        t.start();
    }

    public void decrypt(String key) {
        if (used) throw new IllegalStateException();
        used = true;

        Runnable runnable = () -> {
            String k = key;
            int id = 0;
            Queue<AsyncResult> results = new ArrayDeque<>();
            try {
                while (stream.available() > 0) {
                    byte[] bytes = stream.readNBytes(blockSize - 41);
                    AsyncResult asyncResult = new AsyncResult(id++);
                    results.add(asyncResult);

                    process(asyncResult, k, bytes, true);
                    process(results);
                    k = EncryptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (results.size() > 15) {
                        process(results);

                        try {
                            synchronized (instance) {
                                wait(100);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    while (queue.size() > 7) {
                        try {
                            synchronized (instance) {
                                wait(100);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!results.isEmpty()) {
                process(results);
            }
            done = true;
        };
        Thread t = new Thread(threadGroup, runnable);
        t.setName("EncryptionSymmetricAsyncStream - Decrypt");
        t.start();
    }

    private void process(AsyncResult asyncResult, String key, byte[] bytes, boolean mode) {
        Runnable runnable = () -> {
            if (!mode) {
                asyncResult.bytes = EncryptionSymmetric.encrypt(bytes, key);
            } else {
                asyncResult.bytes = EncryptionSymmetric.decrypt(bytes, key);
            }
            synchronized (instance) {
                notifyAll();
            }
        };
        Thread t = new Thread(threadGroup, runnable);
        t.setName("EncryptionSymmetricAsyncStream - " + (!mode ? "Encrypt" : "Decrypt") + " process");
        t.start();
    }

    private void process(Queue<AsyncResult> results) {
        while (!results.isEmpty() && results.peek().bytes != null) {
            processData(results.poll().bytes);
        }
    }

    public boolean isNotDone() {
        return !done;
    }

    public boolean isDone() {
        return done;
    }

    public boolean hasData() {
        return !queue.isEmpty();
    }

    public void waitForFinish() {
        while (isNotDone()) {
            try {
                synchronized (instance) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void waitForData() {
        while (!hasData()) {
            try {
                synchronized (instance) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void waitForData(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (size > 8) {
            throw new IllegalArgumentException();
        }
        while (queue.size() < size) {
            try {
                synchronized (instance) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public byte[] getData(boolean wait) {
        if (!wait) {
            return getData();
        }

        while (!hasData()) {
            try {
                synchronized (instance) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return getData();
    }

    public byte[] getData() {
        if (!hasData()) {
            return new byte[0];
        }

        synchronized (instance) {
            notifyAll();
        }
        return queue.poll();
    }

    public int dataBlocks() {
        return queue.size();
    }

    private void processData(byte[] bytes) {
        encryptionStreamProcessor.processData(bytes);
    }

}