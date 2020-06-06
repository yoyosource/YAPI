package yapi.encryption.encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class YAPICipherDescription {

    enum DescriptionType {
        CRYPT,
        CRYPT_PARALLEL,

        DERIVE
    }

    DescriptionType type;

    byte[] key;
    byte[] salt;
    byte[] bytes;
    int size;
    FileInputStream source;
    FileOutputStream destination;
    int threads;

    public YAPICipherDescription(byte[] key, byte[] bytes) {
        this.key = key;
        this.bytes = bytes;

        type = DescriptionType.CRYPT;
    }

    public YAPICipherDescription(byte[] key, FileInputStream source, FileOutputStream destination, int threads) {
        this.key = key;
        this.source = source;
        this.destination = destination;
        this.threads = threads;

        type = DescriptionType.CRYPT_PARALLEL;
    }

    public YAPICipherDescription(byte[] salt, byte[] key, int size) {
        this.salt = salt;
        this.key = key;
        this.size = size;

        type = DescriptionType.DERIVE;
    }

}
