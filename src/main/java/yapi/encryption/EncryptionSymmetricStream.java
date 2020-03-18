package yapi.encryption;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EncryptionSymmetricStream {

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

    private final EncryptionSymmetricStream instance = this;

    public EncryptionSymmetricStream(FileInputStream fileInputStream) {
        this.stream = fileInputStream;
    }

    public EncryptionSymmetricStream(ByteArrayInputStream byteArrayInputStream) {
        this.stream = byteArrayInputStream;
    }

    public EncryptionSymmetricStream(InputStream inputStream) {
        this.stream = inputStream;
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
            try {
                while (stream.available() > 0) {
                    byte[] bytes = stream.readNBytes(2007);
                    bytes = EncryptionSymmetric.encrypt(bytes, k);
                    processData(bytes);
                    k = EncryptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (queue.size() > 7) {
                        try {
                            synchronized (instance) {
                                wait(1000);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            done = true;
        };
        Thread t = new Thread(runnable);
        t.setName("EncryptionSymmetricStream - Encrypt");
        t.start();
    }

    public void decrypt(String key) {
        if (used) throw new IllegalStateException();
        used = true;

        Runnable runnable = () -> {
            String k = key;
            try {
                while (stream.available() > 0) {
                    byte[] bytes = stream.readNBytes(2007);
                    bytes = EncryptionSymmetric.decrypt(bytes, k);
                    if (bytes.length == 0) {
                        break;
                    }
                    processData(bytes);
                    k = EncryptionSymmetric.deriveKey(k);

                    synchronized (instance) {
                        notifyAll();
                    }

                    while (queue.size() > 7) {
                        try {
                            synchronized (instance) {
                                wait(1000);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            done = true;
        };
        Thread t = new Thread(runnable);
        t.setName("EncryptionSymmetricStream - Decrypt");
        t.start();
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
