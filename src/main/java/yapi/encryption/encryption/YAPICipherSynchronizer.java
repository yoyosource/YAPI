package yapi.encryption.encryption;

import yapi.file.FileUtils;

import java.io.File;

class YAPICipherSynchronizer {

    static final int BLOCK_SIZE_ENCRYPTION = 1024 * 128 - 32;
    static final int BLOCK_SIZE_DECRYPTION = 1024 * 128;

    private static long id = 0;

    private static final File baseFileYAPI = new File(FileUtils.getUserHome() + "/YAPI/YAPICipher");

    static {
        baseFileYAPI.mkdirs();
    }

    static synchronized long getID() {
        return id++;
    }

    static synchronized File getBaseFileYAPI() {
        return baseFileYAPI;
    }

    static synchronized String getBaseFileYAPIPath() {
        return baseFileYAPI.getAbsolutePath();
    }

}
