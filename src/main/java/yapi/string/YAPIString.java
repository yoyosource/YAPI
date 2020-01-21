package yapi.string;

import java.util.Arrays;

public class YAPIString {

    public static void main(String[] args) {
        YAPIString yapiString = new YAPIString("Hello World!");
        yapiString.reverse();
        System.out.println(yapiString);
        System.out.println(yapiString.substring(4));
        System.out.println(yapiString.substring(-4));
    }

    private char[] chars = null;
    private byte[] bytes = null;

    private boolean reversed = false;

    public YAPIString(String s) {
        chars = s.toCharArray();
        bytes = s.getBytes();
    }

    public YAPIString(StringBuilder s) {
        this(s.toString());
    }

    public YAPIString(char[] chars) {
        this.chars = Arrays.copyOf(chars, chars.length);
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
        chars = StringFormatting.toChars(bytes);
    }

    public void reverse() {
        reversed = !reversed;
    }

    public int length() {
        return chars.length;
    }

    public char getChar(int i) {
        if (reversed) {
            return chars[chars.length - i - 1];
        } else {
            return chars[i];
        }
    }

    public char[] getChars() {
        if (reversed) {
            char[] r = new char[this.chars.length];
            for (int i = 0; i < r.length; i++) {
                r[i] = getChar(i);
            }
            return r;
        }
        return Arrays.copyOf(chars, chars.length);
    }

    public char[] getChars(int length) {
        if (reversed) {
            char[] r = new char[length];
            for (int i = 0; i < r.length; i++) {
                r[i] = getChar(i);
            }
            return r;
        }
        return Arrays.copyOf(chars, length);
    }

    public byte getByte(int i) {
        if (reversed) {
            return bytes[bytes.length - i - 1];
        } else {
            return bytes[i];
        }
    }

    public byte[] getBytes() {
        if (reversed) {
            byte[] r = new byte[bytes.length];
            for (int i = 0; i < r.length; i++) {
                r[i] = getByte(i);
            }
            return r;
        }
        return Arrays.copyOf(bytes, bytes.length);
    }

    public byte[] getBytes(int length) {
        if (reversed) {
            byte[] r = new byte[length];
            for (int i = 0; i < r.length; i++) {
                r[i] = getByte(i);
            }
            return r;
        }
        return Arrays.copyOf(bytes, length);
    }

    public YAPIString substring(int start) {
        boolean r = false;
        if (start < 0) {
            r = true;
            start = -start;
            reverse();
        }
        StringBuilder st = new StringBuilder();
        for (int i = start; i < length(); i++) {
            st.append(getChar(i));
        }
        if (r) reverse();
        return new YAPIString(st);
    }

    public YAPIString substring(int start, int stop) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            st.append(getChar(i));
        }
        return st.toString();
    }

}
