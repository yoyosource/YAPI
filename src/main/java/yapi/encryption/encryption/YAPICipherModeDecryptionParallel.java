// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.file.FileUtils;
import yapi.internal.exceptions.CipherException;
import yapi.manager.worker.WorkerPool;
import yapi.math.NumberRandom;
import yapi.runtime.ThreadUtils;
import yapi.string.HashType;
import yapi.string.StringCrypting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

import static yapi.encryption.encryption.YAPICipherSynchronizer.BLOCK_SIZE_DECRYPTION;

class YAPICipherModeDecryptionParallel implements YAPICipherImpl {

    private final File baseFileCipher = new File(YAPICipherSynchronizer.getBaseFileYAPIPath() + "/" + StringCrypting.hash(YAPICipherSynchronizer.getID() + "", HashType.SHA512, false) + "-");

    private int partitionSource(FileInputStream fileInputStream) throws IOException {
        int fileID = 0;
        byte[] bytes;
        while (fileInputStream.available() > 0) {
            bytes = fileInputStream.readNBytes(BLOCK_SIZE_DECRYPTION);
            File f = new File(baseFileCipher.getAbsolutePath() + fileID++);
            if (f.exists()) { FileUtils.emptyFile(f); } else { FileUtils.create(f); }
            f.deleteOnExit();
            FileUtils.dump(f, bytes);
        }
        return fileID;
    }

    @Override
    public byte[] crypt(byte[] key, byte[] bytes) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'cryptParallel()' instead");
    }

    @Override
    public byte[] derive(byte[] salt, byte[] key, int size) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'cryptParallel()' instead");
    }

    @Override
    public void cryptParallel(byte[] key, FileInputStream source, FileOutputStream destination, int threads) throws CipherException {
        if (threads < 0) {
            threads = 1;
        }
        try {
            int ids = partitionSource(source);
            YAPICipher keySpaceCipher = YAPICipher.getInstance(YAPICipher.DERIVATION);
            byte[] salt = salt();
            byte[] keySpace = keySpaceCipher.derive(salt, key, ids * 8);

            WorkerPool workerPool = new WorkerPool(threads);
            for (int i = 0; i < ids; i++) {
                work(keySpace, workerPool, i);
            }
            destination.write(header(salt, key));
            workerPool.awaitClose();

            for (int i = 0; i < ids; i++) {
                try (FileInputStream fileInputStream = new FileInputStream(new File(baseFileCipher.getAbsolutePath() + i))) {
                    destination.write(fileInputStream.readAllBytes());
                }
            }

            fileCollector(ids);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new CipherException(e.getMessage(), e.getCause());
        }
    }

    private void fileCollector(int ids) {
        Runnable fileCollector = () -> {
            LinkedList<Integer> stillToDelete = new LinkedList<>();
            int index = 0;
            while (index < ids || !stillToDelete.isEmpty()) {
                if (index < ids) {
                    boolean b = new File(baseFileCipher.getAbsolutePath() + index).delete();
                    if (!b) stillToDelete.add(index);
                    index++;
                } else {
                    int i = stillToDelete.getFirst();
                    boolean b = new File(baseFileCipher.getAbsolutePath() + i).delete();
                    if (!b) stillToDelete.add(i);
                }
                ThreadUtils.sleep(1);
            }
        };
        Thread fileCollectorThread = new Thread(fileCollector);
        fileCollectorThread.setName("File Collector");
        fileCollectorThread.setDaemon(false);
        fileCollectorThread.start();
    }

    private void work(byte[] keySpace, WorkerPool workerPool, int id) throws CipherException {
        AtomicReference<CipherException> cipherException = new AtomicReference<>();
        workerPool.work(() -> {
            File f = new File(baseFileCipher.getAbsolutePath() + id);
            try (FileInputStream fileInputStream = new FileInputStream(f)) {
                byte[] bytes = fileInputStream.readAllBytes();
                YAPICipher yapiCipher = YAPICipher.getInstance(YAPICipher.DECRYPTION);
                bytes = yapiCipher.crypt(getKey(keySpace, id), bytes);

                FileOutputStream fileOutputStream = new FileOutputStream(f);
                fileOutputStream.write(bytes);
            } catch (IOException e) {
                cipherException.set(new CipherException(e.getMessage(), e.getCause()));
            } catch (NoSuchAlgorithmException e) {
                cipherException.set(new CipherException(e.getMessage(), e.getCause()));
            } catch (CipherException e) {
                cipherException.set(e);
            }
        });
        if (cipherException.get() != null) throw cipherException.get();
    }

    private byte[] getKey(byte[] keySpace, int id) {
        byte[] key = new byte[8];
        for (int i = 0; i < 8; i++) {
            key[i] = keySpace[id * 8 + i];
        }
        return key;
    }

    private byte[] salt() {
        return new NumberRandom().getBytes(new byte[8]);
    }

    private byte[] header(byte[] salt, byte[] key) throws CipherException {
        byte[] bytes = new NumberRandom().getBytes(new byte[salt.length * 16]);
        for (int i = 0; i < salt.length; i++) {
            bytes[i * 16] = salt[i];
        }
        try {
            YAPICipher yapiCipher = YAPICipher.getInstance(YAPICipher.ENCRYPTION);
            return yapiCipher.crypt(key, bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException(e.getMessage(), e.getCause());
        }
    }

}