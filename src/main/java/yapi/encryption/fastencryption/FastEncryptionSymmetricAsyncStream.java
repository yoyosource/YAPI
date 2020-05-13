// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.fastencryption;

import yapi.encryption.encryption.EncryptionStreamProcessor;
import yapi.file.FileUtils;
import yapi.runtime.ThreadUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class FastEncryptionSymmetricAsyncStream {

    private final int blockSize = 1024*1024;

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

    private final FastEncryptionSymmetricAsyncStream instance = this;

    private static ThreadGroup threadGroup = new ThreadGroup(ThreadUtils.yapiGroup, "EncryptionSymmetricAsyncStream");

    private long time = 0;
    private long doneBytes = 0;

    private int threadCount = Runtime.getRuntime().availableProcessors() * 4 - 1;

    public FastEncryptionSymmetricAsyncStream(FileInputStream fileInputStream) {
        this.stream = fileInputStream;
    }

    public FastEncryptionSymmetricAsyncStream(FileInputStream fileInputStream, int threadCount) {
        this.stream = fileInputStream;
        if (threadCount < 1) {
            threadCount = 1;
        }
        this.threadCount = threadCount;
    }

    public FastEncryptionSymmetricAsyncStream(ByteArrayInputStream byteArrayInputStream) {
        this.stream = byteArrayInputStream;
    }

    public FastEncryptionSymmetricAsyncStream(ByteArrayInputStream byteArrayInputStream, int threadCount) {
        this.stream = byteArrayInputStream;
        if (threadCount < 1) {
            threadCount = 1;
        }
        this.threadCount = threadCount;
    }

    public FastEncryptionSymmetricAsyncStream(InputStream inputStream) {
        this.stream = inputStream;
    }

    public FastEncryptionSymmetricAsyncStream(InputStream inputStream, int threadCount) {
        this.stream = inputStream;
        if (threadCount < 1) {
            threadCount = 1;
        }
        this.threadCount = threadCount;
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
        if (used) throw new IllegalStateException();
        used = true;

        time = System.currentTimeMillis();

        Runnable runnable = () -> {
            String k = key;
            int id = 0;
            Queue<AsyncResult> results = new ArrayDeque<>();
            try {
                while (stream.available() > 0) {
                    byte[] bytes = stream.readNBytes(blockSize - 32);
                    AsyncResult asyncResult = new AsyncResult(id++);
                    results.add(asyncResult);

                    process(asyncResult, k, bytes, false);
                    process(results);
                    k = FastEncrytptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (results.size() > threadCount) {
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

        time = System.currentTimeMillis();

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
                    k = FastEncrytptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (results.size() > threadCount) {
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
        t.setName("FastEncryptionSymmetricAsyncStream - Decrypt");
        t.start();
    }

    public long bPS() {
        return (long)(doneBytes / ((System.currentTimeMillis() - time) / 1000.0));
    }

    public long getDoneBytes() {
        return doneBytes;
    }

    public String bytesPerSecond() {
        return bPS() + " b/s";
    }

    public String kilobytesPerSecond() {
        return bPS() / 10 / 10.0 + " Kb/s";
    }

    public String megabytesPerSecond() {
        return bPS() / 1000 / 10 / 10.0 + " Mb/s";
    }

    public String nPerSecond() {
        String s = FileUtils.getSize(bPS(), true, true, 2);
        StringBuilder st = new StringBuilder().append(s);
        st.insert(st.length() - 1, ' ');
        s = st.toString();
        if (!s.endsWith("b")) {
            s = s + "b";
        }
        return s + "/s";
    }

    private void process(AsyncResult asyncResult, String key, byte[] bytes, boolean mode) {
        Runnable runnable = () -> {
            if (!mode) {
                asyncResult.bytes = FastEncrytptionSymmetric.encrypt(key, bytes);
            } else {
                asyncResult.bytes = FastEncrytptionSymmetric.decrypt(key, bytes);
            }
            doneBytes += bytes.length;
            synchronized (instance) {
                notifyAll();
            }
        };
        Thread t = new Thread(threadGroup, runnable);
        t.setName("FastEncryptionSymmetricAsyncStream - " + (!mode ? "Encrypt" : "Decrypt") + " process");
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