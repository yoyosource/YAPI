package yapi.compression.general;

import yapi.string.StringFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringCount {

    private byte[] value;
    private long count = 1;
    private long loss = 0L;

    private List<Integer> occurrences = new ArrayList<>();

    private int keyLength = 2;
    private byte[] key = new byte[0];

    public StringCount(byte[] value) {
        this.value = value;
    }

    public void increment() {
        count++;
        loss = (value.length - (keyLength + 1)) * (count - 1) - (keyLength + 1);
    }

    public void setReplacementLength(int length) {
        this.keyLength = length;
        loss = (value.length - (keyLength + 1)) * (count - 1) - (keyLength + 1);
    }

    public byte[] getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }

    public long getLoss() {
        return loss;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void addOccurrence(int occurrence) {
        occurrences.add(occurrence);
    }

    public List<Integer> getOccurrences() {
        return occurrences;
    }

    @Override
    public String toString() {
        return "'" + StringFormatting.toHex(value, true) + "':" + value.length + "(" + count + "){" + loss + "}[" + StringFormatting.toHex(key, true) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringCount)) return false;
        StringCount that = (StringCount) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
