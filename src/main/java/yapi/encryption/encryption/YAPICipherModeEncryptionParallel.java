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
import java.io.FileOutputStream;
import java.io.IOException;

@WorkInProgress(context = WorkInProgressType.ALPHA)
class YAPICipherModeEncryptionParallel implements YAPICipherImpl {

    private static long id = 0;
    private final long currentID = id++;

    private static final File baseFileYAPI = new File(FileUtils.getUserHome() + "/YAPI/YAPICipher");
    private final File baseFileCipher = new File(baseFileYAPI.getAbsoluteFile() + "/" + StringCrypting.hash(currentID + "", HashType.SHA512, false) + "-");

    static {
        baseFileYAPI.mkdirs();
    }

    private static final int BLOCK_SIZE = 1024 * 1024;
    // 1024*1024*2
    //   2.097.152
    // 255* 8192
    // 255*   32
    // 255*    1
    // 255*8192 + 255*32 + 255*1
    //   2.097.375

    public long partitionSource(FileInputStream fileInputStream) throws IOException {
        long fileID = 0;
        byte[] bytes;
        long lengthToRemove = 0;
        while (fileInputStream.available() > 0) {
            bytes = fileInputStream.readNBytes(BLOCK_SIZE);
            if (bytes.length < BLOCK_SIZE) {
                lengthToRemove += BLOCK_SIZE - bytes.length;
            }
            File f = new File(baseFileCipher.getAbsolutePath() + ++fileID);
            f.createNewFile();
            f.deleteOnExit();
            FileUtils.dump(f, bytes);
        }

        if (fileID % 2 == 0) {
            lengthToRemove += BLOCK_SIZE;
            createPadding(++fileID);
        }
        createHeader(lengthToRemove);
        return fileID;
    }

    public void createHeader(long lengthToRemove) throws IOException {
        System.out.println(lengthToRemove);
        File file = new File(baseFileCipher.getAbsolutePath() + 0);
        file.createNewFile();
        file.deleteOnExit();
    }

    public void createPadding(long fileID) throws IOException {
        File file = new File(baseFileCipher.getAbsolutePath() + fileID);
        file.createNewFile();
        file.deleteOnExit();
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
            partitionSource(source);
        } catch (IOException e) {
            throw new CipherException(e.getMessage(), e.getCause());
        }
    }

    public static void main(String[] args) throws Exception {
        YAPICipherModeEncryptionParallel yapiCipherModeEncryptionParallel = new YAPICipherModeEncryptionParallel();
        yapiCipherModeEncryptionParallel.cryptParallel(new byte[]{0}, new FileInputStream(new File("/Users/jojo/Resilio Sync/Transfer/Fuchsbau165-Wlan-2.pings")), new FileOutputStream(new File("/Users/jojo/Resilio Sync/Transfer/Test.txt")), 1);
        //yapiCipherModeEncryptionParallel.cryptParallel(new byte[]{0}, new FileInputStream(new File("/Users/jojo/IdeaProjects/YACryptor/src/main/resources/entest.txt")), new FileOutputStream(new File("/Users/jojo/Resilio Sync/Transfer/Test.txt")), 1);
        ThreadUtils.sleep(100000);
    }

}