package yapi.compression.string;

import java.util.Arrays;

public class StringKeyGenerator {

    private byte[] key;
    private byte start = (byte)15;

    public StringKeyGenerator(int size) {
        key = new byte[size];
        Arrays.fill(key, start);
    }

    public byte[] getKey() {
        byte[] k = Arrays.copyOf(key, key.length);
        boolean update = true;
        int index = 0;
        while (update) {
            key[index]++;
            update = false;
            if (key[index] == 0) {
                key[index] += start;
                update = true;
            }
            index++;
        }
        return k;
    }

    public int length() {
        return key.length;
    }

    public long keys() {
        return (long)Math.pow((256 - start), key.length);
    }

}
