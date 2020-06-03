// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.file.FileUtils;
import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.internal.exceptions.CipherException;
import yapi.runtime.ThreadUtils;
import yapi.string.HashType;
import yapi.string.StringCrypting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.ALPHA)
class YAPICipherModeEncryptionParallel implements YAPICipherImpl {

    private static long id = 0;
    private final long currentID = id++;

    private static final File baseFileYAPI = new File(FileUtils.getUserHome() + "/YAPI/YAPICipher");
    private final File baseFileCipher = new File(baseFileYAPI.getAbsoluteFile() + "/" + StringCrypting.hash(currentID + "", HashType.SHA512, false) + "-");

    static {
        baseFileYAPI.mkdirs();
    }

    private static final int BLOCK_SIZE = 16 * 64 * 128;

    public long partitionSource(File source) throws CipherException {
        long fileID = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            while (fileInputStream.available() > 0) {
                byte[] bytes = fileInputStream.readNBytes(BLOCK_SIZE);
                if (bytes.length < BLOCK_SIZE) {

                }
                fileID++;
                File f = new File(baseFileCipher.getAbsolutePath() + fileID);
                f.createNewFile();
                f.deleteOnExit();
                FileUtils.dump(f, bytes);
            }
            File file = new File(baseFileCipher.getAbsolutePath() + 0);
            file.createNewFile();
            file.deleteOnExit();

            if (fileID % 2 == 0) {
                fileID++;
                file = new File(baseFileCipher.getAbsolutePath() + fileID);
                file.createNewFile();
                file.deleteOnExit();
            }
        } catch (IOException e) {
            throw new CipherException(e.getMessage(), e.getCause());
        }
        return fileID;
    }

    @Override
    public byte[] assembleOutput(List<Block> blocks) throws CipherException {
        return new byte[0];
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
    public void cryptParallel(byte[] key, File source, File destination, int threads) throws CipherException {
        if (!source.isFile()) {
            throw new CipherException("Source is not a File");
        }
        if (!source.exists()) {
            throw new CipherException("Source not found");
        }
        if (!destination.isFile()) {
            throw new CipherException("Destination is not a File");
        }
        if (!destination.exists()) {
            try {
                destination.createNewFile();
            } catch (IOException e) {
                throw new CipherException(e.getMessage(), e.getCause());
            }
        }

        partitionSource(source);
    }

    public static void main(String[] args) throws Exception {
        YAPICipherModeEncryptionParallel yapiCipherModeEncryptionParallel = new YAPICipherModeEncryptionParallel();

        ThreadUtils.sleep(100000);
    }

}