// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.fastencryption;

import yapi.encryption.encryption.EncryptionOutputStreamProcessor;
import yapi.runtime.ThreadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastEncryptionSymmetricQueue {

    private boolean running = true;
    private List<EncryptionSymmetricTask> tasks = new ArrayList<>();

    private long bytesPerSecond = 0;
    private String nPerSecond = "";

    public FastEncryptionSymmetricQueue() {
        Runnable runnable = () -> {
            while (running) {
                while (tasks.isEmpty()) {
                    try {
                        wait(100);
                    } catch (InterruptedException e) {

                    }
                }

                tasks.get(0).run();
                tasks.remove(0);
            }
        };
        Thread t = new Thread(ThreadUtils.yapiGroup, runnable);
        t.setName("FastEncryptionSymmetricQueue");
        t.start();
    }

    private class EncryptionSymmetricTask {

        private boolean mode;
        private String password;
        private File source;
        private File destination;

        public EncryptionSymmetricTask(boolean mode, String password, File source, File destination) {
            this.mode = mode;
            this.password = password;

            if (source.isDirectory()) {
                throw new IllegalArgumentException();
            }
            if (destination.isDirectory()) {
                throw new IllegalArgumentException();
            }
            if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
                throw new IllegalArgumentException();
            }

            this.source = source;
            this.destination = destination;
        }

        public boolean isMode() {
            return mode;
        }

        public String getPassword() {
            return password;
        }

        public File getSource() {
            return source;
        }

        public File getDestination() {
            return destination;
        }

        private void run() {
            try {
                FastEncryptionSymmetricAsyncStream fastEncryptionSymmetricAsyncStream = new FastEncryptionSymmetricAsyncStream(new FileInputStream(source));
                fastEncryptionSymmetricAsyncStream.setEncryptionStreamProcessor(new EncryptionOutputStreamProcessor(new FileOutputStream(destination)));
                String key = FastEncrytptionSymmetric.createKey(password);

                if (mode) {
                    fastEncryptionSymmetricAsyncStream.encrypt(key);
                } else {
                    fastEncryptionSymmetricAsyncStream.decrypt(key);
                }

                while (fastEncryptionSymmetricAsyncStream.isNotDone()) {
                    bytesPerSecond = fastEncryptionSymmetricAsyncStream.bPS();
                    nPerSecond = fastEncryptionSymmetricAsyncStream.nPerSecond();
                }

                bytesPerSecond = 0;
                nPerSecond = "--- b/s";
            } catch (IOException e) {

            }
        }

    }

    public void encryptionTask(File source, File destination, String password) {
        tasks.add(new EncryptionSymmetricTask(false, password, source, destination));
    }

    public void decryptionTask(File source, File destination, String password) {
        tasks.add(new EncryptionSymmetricTask(true, password, source, destination));
    }

    public List<EncryptionSymmetricTask> getTasks() {
        return tasks;
    }

    public long bytesPerSecond() {
        return bytesPerSecond;
    }

    public String nPerSecond() {
        return nPerSecond;
    }

    public void close() {
        running = false;
    }

}