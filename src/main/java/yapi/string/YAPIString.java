package yapi.string;

import java.util.Arrays;

public class YAPIString {

    private char[] chars;
    private byte[] bytes;
    public YAPIString(StringBuilder st) {
        chars = new char[st.length()];
        st.getChars(0, st.length(), chars, 0);
        bytes = StringFormatting.toBytes(chars);
    }

    private boolean reversed = false;

    public YAPIString(String s) {
        chars = s.toCharArray();
        bytes = s.getBytes();
    }

    private YAPIString() {
        chars = new char[0];
        bytes = new byte[0];
    }

    public static void main(String[] args) {
        YAPIString yapiString = new YAPIString("Hello World!");

        System.out.println(yapiString);
        yapiString.insert("pip ", 6);
        yapiString.contains("orl");
        System.out.println(yapiString);
    }

    public YAPIString(char[] chars) {
        this.chars = Arrays.copyOf(chars, chars.length);
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
        chars = StringFormatting.toChars(bytes);
    }

    public YAPIString reverse() {
        reversed = !reversed;
        return this;
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
        if (start == stop) {
            return new YAPIString();
        }
        StringBuilder st = new StringBuilder();
        if (start >= 0 && start < stop && stop <= length()) {
            for (int i = start; i < stop; i++) {
                st.append(getChar(i));
            }
            return new YAPIString(st);
        }
        if (stop >= 0 && stop < start && start <= length()) {
            for (int i = stop; i < start; i++) {
                st.append(getChar(i));
            }
            return new YAPIString(st).reverse();
        }
        if (start <= 0 && start > stop && stop >= -length()) {
            start = length() - -start - 1;
            stop = length() - -stop;
            for (int i = start; i >= stop; i--) {
                st.append(getChar(i));
            }
            return new YAPIString(st);
        }
        throw new IllegalArgumentException();
    }

    public void insert(char c, int index) {
        char[] cs = new char[chars.length + 1];
        int i = 0;
        while (i < chars.length + 1) {
            if (i == index) {
                cs[i] = c;
            } else if (i > index) {
                cs[i] = chars[i - 1];
            } else {
                cs[i] = chars[i];
            }
            i++;
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void insert(String s, int index) {
        char[] cs = new char[chars.length + s.length()];
        for (int i = 0; i < index; i++) {
            cs[i] = chars[i];
        }
        char[] st = s.toCharArray();
        for (int i = 0; i < st.length; i++) {
            cs[i + index] = st[i];
        }
        for (int i = 0; i < chars.length - index; i++) {
            cs[i + index + st.length] = chars[i + index];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void insert(byte b, int index) {
        byte[] bs = new byte[bytes.length + 1];
        int i = 0;
        while (i < bytes.length + 1) {
            if (i == index) {
                bs[i] = b;
            } else if (i > index) {
                bs[i] = bytes[i - 1];
            } else {
                bs[i] = bytes[i];
            }
            i++;
        }
        bytes = bs;
        chars = StringFormatting.toChars(bytes);
    }

    public void delete(int index) {
        char[] cs = new char[chars.length - 1];
        for (int i = 0; i < chars.length - 1; i++) {
            if (i >= index) {
                cs[i] = chars[i + 1];
            } else {
                cs[i] = chars[i];
            }
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void deleteFirst() {
        char[] cs = new char[chars.length - 1];
        for (int i = 1; i < chars.length; i++) {
            cs[i - 1] = chars[i];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void deleteFirst(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        char[] cs = new char[chars.length - size];
        for (int i = size; i < chars.length; i++) {
            cs[i - size] = chars[i];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void deleteLast() {
        chars = Arrays.copyOf(chars, chars.length - 1);
        bytes = StringFormatting.toBytes(chars);
    }

    public void deleteLast(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        chars = Arrays.copyOf(chars, chars.length - size);
        bytes = StringFormatting.toBytes(chars);
    }

    public int occurrences(char c) {
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                count++;
            }
        }
        return count;
    }

    public boolean contains(char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return true;
            }
        }
        return false;
    }

    public int occurrences(String s) {
        return occurrences(new YAPIString(s));
    }

    public int occurrences(YAPIString yapiString) {
        int index = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == yapiString.getChar(index)) {
                index++;
            } else {
                index = 0;
            }

            if (index == yapiString.length()) {
                count++;
                index = 0;
            }
        }
        return count;
    }

    public boolean contains(String s) {
        return contains(new YAPIString(s));
    }

    public boolean contains(YAPIString yapiString) {
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == yapiString.getChar(index)) {
                index++;
            } else {
                index = 0;
            }

            if (index == yapiString.length()) {
                return true;
            }
        }
        return false;
    }

    public int occurrences(byte b) {
        int count = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) {
                count++;
            }
        }
        return count;
    }

    public boolean contains(byte b) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) {
                return true;
            }
        }
        return false;
    }

    public YAPIString longestCommonPrefix(YAPIString yapiString) {
        int length = Math.min(yapiString.length(), length());
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (yapiString.getChar(i) != getChar(i)) {
                return new YAPIString(st);
            }
            st.append(getChar(i));
        }
        return new YAPIString(st);
    }

    public YAPIString longestCommonSuffix(YAPIString yapiString) {
        int length = Math.min(yapiString.length(), length());
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int tI = length - i;
            int yI = yapiString.length() - i;
            if (yapiString.getChar(yI) != getChar(tI)) {
                return new YAPIString(st);
            }
            st.append(getChar(tI));
        }
        return new YAPIString(st);
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
