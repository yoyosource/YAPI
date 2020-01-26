package yapi.compression.string;

import yapi.string.StringFormatting;

import java.util.Objects;

public class StringCount {

    private String string;
    private long count = 1L;

    private long loss = 0L;

    private byte[] key = new byte[0];
    private int keyLength = 0;

    public StringCount(String string) {
        this.string = string;
    }

    public void increment() {
        count++;
    }

    public String getString() {
        return string;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        loss = (string.length() - (keyLength + 1)) * (count - 1) - (keyLength + 1);
    }

    public long getLoss() {
        loss = (string.length() - (keyLength + 1)) * (count - 1) - (keyLength + 1);
        return loss;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
        loss = (string.length() - (keyLength + 1)) * (count - 1) - (keyLength + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringCount)) return false;
        StringCount that = (StringCount) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return "'" + StringFormatting.toHex(StringFormatting.toBytes(string), true) + "':" + count + "(" + loss + "){" + StringFormatting.toHex(key) + "}";
    }
}
