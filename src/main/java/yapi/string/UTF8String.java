// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.array.LinkedListUtils;
import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.internal.runtimeexceptions.StringException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class UTF8String {

    private byte[] bytes;

    public UTF8String(String s) {
        bytes = s.getBytes(StandardCharsets.UTF_8);
    }

    public UTF8String(byte[] bytes, String charsetName) {
        try {
            this.bytes = new String(bytes, charsetName).getBytes(StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new StringException();
        }
    }

    public UTF8String(char[] chars) {
        bytes = new String(chars).getBytes(StandardCharsets.UTF_8);
    }

    private UTF8String(byte[] bytes, boolean copy) {
        if (copy) {
            this.bytes = Arrays.copyOf(bytes, bytes.length);
        } else {
            this.bytes = bytes;
        }
    }

    private UTF8String(byte[] bytes) {
        this(bytes, false);
    }

    private UTF8String(UTF8String utf8String) {
        this(utf8String.bytes, true);
    }

    public byte[] getRaw() {
        return bytes;
    }

    public static UTF8String fromHex(String hex) {
        hex = hex.replace(" ", "");
        if (!hex.matches("[0-9A-Fa-f]+")) {
            throw new StringException();
        }
        if (hex.length() % 2 != 0) {
            throw new StringException();
        }

        List<String> strs = new ArrayList<>();
        for (int i = 0; i < hex.length() / 2; i++) {
            strs.add(hex.charAt(i * 2) + "" + hex.charAt(i * 2 + 1));
        }
        String[] strings = strs.toArray(new String[0]);

        byte[] bytes = new byte[strings.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(strings[i], 16);
        }
        return new UTF8String(bytes);
    }

    public String toHex() {
        return toHex(false);
    }

    public String toHex(boolean spaces) {
        return StringFormatting.toHex(bytes, spaces);
    }

    public String toBinary() {
        return toBinary(false);
    }

    public String toBinary(boolean spaces) {
        StringBuilder st = new StringBuilder();
        boolean b = false;
        for (byte t : bytes) {
            if (b && spaces) st.append(" ");
            st.append(toBinary(t));
            b = true;
        }
        return st.toString();
    }

    private String toBinary(byte b) {
        int i = b < 0 ? b + 256 : b;
        int t = 128;
        StringBuilder st = new StringBuilder();
        while (t != 0) {
            if (t <= i) st.append("1");
            else st.append("0");
            if (t <= i) i -= t;
            t /= 2;
        }
        return st.toString();
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public UTF8String reverse() {
        byte[] nBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            nBytes[bytes.length - i - 1] = bytes[i];
        }
        return new UTF8String(nBytes);
    }

    public void reverseInPlace() {
        for (int i = 0; i < bytes.length / 2; i++) {
            byte b = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = b;
        }
    }

    public byte byteAt(int index) {
        if (index < 0 || index > bytes.length) {
            throw new StringException();
        }
        return bytes[index];
    }

    public UTF8String substring(int start) {
        if (start < 0) {
            return substringInternal(bytes.length - start * -1, bytes.length);
        }
        return substringInternal(start, bytes.length);
    }

    public UTF8String substring(int start, int end) {
        if (start < 0 && end <= 0) {
            return substringInternal(bytes.length - start * -1, bytes.length - end * -1);
        }
        if (start == end) {
            return new UTF8String(new byte[0]);
        }
        return substringInternal(start, end);
    }

    public UTF8String substringInternal(int start, int end) {
        if (end < start) {
            return new UTF8String(Arrays.copyOfRange(bytes, end, start)).reverse();
        }
        return new UTF8String(Arrays.copyOfRange(bytes, start, end));
    }

    public UTF8String append(String s) {
        appendInternal(s.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    public UTF8String append(UTF8String s) {
        appendInternal(s.bytes);
        return this;
    }

    public UTF8String append(StringBuilder st) {
        append(st.toString());
        return this;
    }

    public UTF8String append(StringBuffer sb) {
        append(sb.toString());
        return this;
    }

    public UTF8String append(char c) {
        append(c + "");
        return this;
    }

    public UTF8String append(char... chars) {
        append(new String(chars));
        return this;
    }

    public UTF8String append(String encoding, byte... bytes) {
        try {
            append(new String(bytes, encoding));
        } catch (UnsupportedEncodingException e) {

        }
        return this;
    }

    public UTF8String append(int i) {
        append(i + "");
        return this;
    }

    public UTF8String append(long l) {
        append(l + "");
        return this;
    }

    private void appendInternal(byte[] bytes) {
        byte[] nBytes = new byte[this.bytes.length + bytes.length];
        for (int i = 0; i < nBytes.length; i++) {
            if (i < this.bytes.length) {
                nBytes[i] = this.bytes[i];
            } else {
                nBytes[i] = bytes[i - this.bytes.length];
            }
        }
        this.bytes = nBytes;
    }

    public boolean startsWith(UTF8String string) {
        if (string.bytes.length > bytes.length) {
            return false;
        }

        int index = 0;
        while (index < string.bytes.length) {
            if (bytes[index] != string.bytes[index]) {
                return false;
            }
            index++;
        }
        return true;
    }

    public boolean endsWith(UTF8String string) {
        if (string.bytes.length > bytes.length) {
            return false;
        }

        int index = 0;
        int l1 = string.bytes.length;
        int l2 = bytes.length;
        while (index < l1) {
            if (bytes[l2 - index - 1] != string.bytes[l1 - index - 1]) {
                return false;
            }
            index++;
        }
        return true;
    }

    public boolean contains(UTF8String string) {
        return indexOf(string, 0) != -1;
    }

    public boolean contains(UTF8String string, int index) {
        return indexOf(string, index) != -1;
    }

    public int indexOf(UTF8String string) {
        return indexOf(string, 0);
    }

    public int indexOf(UTF8String string, int index) {
        if (string.bytes.length > bytes.length) {
            return -1;
        }

        int length = 0;
        while (index < bytes.length) {
            if (string.bytes.length == length) {
                return index - length;
            }
            if (bytes[index] == string.bytes[length]) {
                length++;
            } else if (length > 0) {
                index -= length;
                length = 0;
            }
            index++;
        }
        return -1;
    }

    public int lastIndexOf(UTF8String string) {
        return lastIndexOf(string, bytes.length - 1);
    }

    public int lastIndexOf(UTF8String string, int index) {
        if (string.bytes.length > bytes.length) {
            return -1;
        }

        int length = 0;
        int l1 = string.bytes.length;
        while (index >= 0) {
            if (bytes[index] == string.bytes[l1 - length - 1]) {
                length++;
            } else {
                index += length;
                length = 0;
            }
            if (string.bytes.length == length) {
                return index;
            }
            index--;
        }
        return -1;
    }

    public int[] occurrences(UTF8String string) {
        int index = 0;
        LinkedList<Integer> linkedList = new LinkedList<>();
        while ((index = indexOf(string, index)) != -1) linkedList.add(index++);
        return LinkedListUtils.toIntegerArray(linkedList);
    }

    private void copy(byte[] from, byte[] into, int fromIndex, int fromLength, int intoIndex) {
        if (fromLength < 0) return;
        if (fromIndex < 0 || fromIndex > from.length - 1) return;
        if (intoIndex < 0 || intoIndex > into.length - 1) return;

        // I did not use this Line because of better readability
        // if (fromLength >= 0) System.arraycopy(from, fromIndex, into, intoIndex, fromLength);
        for (int i = 0; i < fromLength; i++) {
            into[intoIndex + i] = from[fromIndex + i];
        }
    }

    @WorkInProgress(context = WorkInProgressType.ALPHA)
    public UTF8String replace(UTF8String target, UTF8String replacement) {
        int[] occurrences = occurrences(target);
        if (occurrences.length == 0) {
            return this;
        }

        byte[] nBytes = new byte[bytes.length - target.bytes.length * occurrences.length + replacement.bytes.length * occurrences.length];
        System.out.println(bytes.length + " " + nBytes.length);
        for (int i = 0; i < occurrences.length; i++) {
            System.out.println(occurrences[i]);
        }
        return this;
    }

    public UTF8String replaceFirst(UTF8String target, UTF8String replacement) {
        int index = indexOf(target);
        if (index == -1) {
            return this;
        }

        byte[] nBytes = new byte[bytes.length - target.bytes.length + replacement.bytes.length];
        copy(bytes, nBytes, 0, index, 0);
        copy(replacement.bytes, nBytes, 0, replacement.bytes.length, index);
        copy(bytes, nBytes, index + target.bytes.length, bytes.length - (index + target.bytes.length), index + replacement.bytes.length);
        bytes = nBytes;
        return this;
    }

    public UTF8String replaceLast(UTF8String target, UTF8String replacement) {
        int index = lastIndexOf(target);
        if (index == -1) {
            return this;
        }
        byte[] nBytes = new byte[bytes.length - target.bytes.length + replacement.bytes.length];
        copy(bytes, nBytes, 0, index, 0);
        copy(replacement.bytes, nBytes, 0, replacement.bytes.length, index);
        copy(bytes, nBytes, index + target.bytes.length, bytes.length - (index + target.bytes.length), index + replacement.bytes.length);
        bytes = nBytes;
        return this;
    }

    public static void main(String[] args) {
        UTF8String string = new UTF8String("Hallo WeWelt äöü");
        string.append("Hello World!");
        System.out.println(string);
        System.out.println(string.indexOf(new UTF8String("Welt")));
        System.out.println(string.substring(string.indexOf(new UTF8String("Welt"))));
        System.out.println(string.lastIndexOf(new UTF8String("Welt")));
        System.out.println(string.substring(string.lastIndexOf(new UTF8String("Welt"))));
        System.out.println(string.lastIndexOf(new UTF8String("Hallo")));
        System.out.println(string.substring(string.lastIndexOf(new UTF8String("Hallo"))));

        string.replaceFirst(new UTF8String("Welt"), new UTF8String("Hello World"));
        System.out.println(string);
        string.replaceLast(new UTF8String("Hello"), new UTF8String("Hello World"));
        System.out.println(string);
        string.replace(new UTF8String("H"), new UTF8String("B"));
    }

    @Override
    public String toString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}