// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.internal.exceptions.StringException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UTF8String {

    public static void main(String[] args) {
        UTF8String string = new UTF8String("Hallo Welt äöü");
        string.append("Hello World!");
        System.out.println(string);
        System.out.println(string.toHex(true));
        System.out.println(string.toBinary(true));
        System.out.println(UTF8String.fromHex(string.toHex()).toHex(true));
    }

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

    private UTF8String(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getRaw() {
        return bytes;
    }

    public static UTF8String fromHex(String hex) {
        if (!(hex.matches("[0-9A-Fa-f]+") || hex.matches("[0-9A-Fa-f]{2}( [0-9A-Fa-f]{2})*"))) {
            throw new StringException();
        }
        if (hex.length() % 2 != 0) {
            throw new StringException();
        }

        String[] strings;
        if (hex.matches("[0-9A-Fa-f]+")) {
            List<String> strs = new ArrayList<>();
            for (int i = 0; i < hex.length() / 2; i++) {
                strs.add(hex.charAt(i * 2) + "" + hex.charAt(i * 2 + 1));
            }
            strings = strs.toArray(new String[0]);
        } else {
            strings = hex.split(" ");
        }

        byte[] bytes = new byte[strings.length];
        int i = 0;
        for (String s : strings) {
            bytes[i++] = (byte)Integer.parseInt(s, 16);
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
        return substringInternal(start, end);
    }

    private UTF8String substringInternal(int start, int end) {
        return new UTF8String(toString().substring(start, end));
    }

    public UTF8String substringRisky(int start) {
        if (start < 0) {
            return substringRiskyInternal(bytes.length - start * -1, bytes.length);
        }
        return substringRiskyInternal(start, bytes.length);
    }

    public UTF8String substringRisky(int start, int end) {
        return substringRiskyInternal(start, end);
    }

    public UTF8String substringRiskyInternal(int start, int end) {
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

    @Override
    public String toString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}