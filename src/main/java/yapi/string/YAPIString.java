// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.internal.runtimeexceptions.string.YAPIStringException;
import yapi.quick.Timer;

import java.util.Arrays;
import java.util.Objects;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class YAPIString {

    private char[] chars;
    private byte[] bytes;

    private boolean reversed = false;

    public YAPIString(String s) {
        chars = s.toCharArray();
        bytes = s.getBytes();
    }

    public YAPIString(StringBuilder st) {
        chars = new char[st.length()];
        st.getChars(0, st.length(), chars, 0);
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString(StringBuffer st) {
        chars = new char[st.length()];
        st.getChars(0, st.length(), chars, 0);
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString(CharSequence st) {
        this(st.toString());
    }

    public YAPIString(YAPIString yapiString) {
        chars = yapiString.getChars();
        bytes = StringFormatting.toBytes(chars);
    }

    private YAPIString() {
        chars = new char[0];
        bytes = new byte[0];
    }

    public YAPIString(char[] chars) {
        this.chars = Arrays.copyOf(chars, chars.length);
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
        chars = StringFormatting.toChars(bytes);
    }

    private YAPIString(String s, boolean reversed) {
        chars = s.toCharArray();
        bytes = s.getBytes();
        this.reversed = reversed;
    }

    public YAPIString reverse() {
        reversed = !reversed;
        return this;
    }

    public int length() {
        return chars.length;
    }

    public boolean isEmpty() {
        return chars.length == 0;
    }

    public boolean isBlank() {
        for (int i = 0; i < length(); i++) {
            if (getChar(i) != ' ' && getChar(i) != '\t' && !Character.isWhitespace(getChar(i))) {
                return false;
            }
        }
        return true;
    }

    public char getChar(int i) {
        if (reversed) {
            return chars[chars.length - i - 1];
        } else {
            return chars[i];
        }
    }

    public boolean isNotEmpty() {
        return chars.length != 0;
    }

    public char getCharMapped(int i) {
        if (reversed) {
            i = i % chars.length;
            return chars[chars.length - i - 1];
        } else {
            return chars[i % chars.length];
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

    public char charAt(int i) {
        return getChar(i);
    }

    public byte getByteMapped(int i) {
        if (reversed) {
            i = i % bytes.length;
            return bytes[bytes.length - i - 1];
        } else {
            return bytes[i % bytes.length];
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

    public byte byteAt(int i) {
        return getByte(i);
    }

    public boolean startsWith(char... chars) {
        if (length() < chars.length) {
            return false;
        }
        for (int i = 0; i < chars.length; i++) {
            if (getChar(i) != chars[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(byte... bytes) {
        if (length() < bytes.length) {
            return false;
        }
        for (int i = 0; i < bytes.length; i++) {
            if (getByte(i) != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(String s) {
        if (length() < s.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (getChar(i) != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(YAPIString yapiString) {
        if (length() < yapiString.length()) {
            return false;
        }
        for (int i = 0; i < yapiString.length(); i++) {
            if (getChar(i) != yapiString.getChar(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(char... chars) {
        if (length() < chars.length) {
            return false;
        }
        for (int i = 1; i <= chars.length; i++) {
            if (getChar(length() - i) != chars[chars.length - i]) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(byte... bytes) {
        if (length() < bytes.length) {
            return false;
        }
        for (int i = 1; i <= bytes.length; i++) {
            if (getChar(length() - i) != bytes[chars.length - i]) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(String s) {
        if (length() < s.length()) {
            return false;
        }
        for (int i = 1; i <= s.length(); i++) {
            if (getChar(length() - i) != s.charAt(s.length() - i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(YAPIString yapiString) {
        if (length() < yapiString.length()) {
            return false;
        }
        for (int i = 1; i <= yapiString.length(); i++) {
            if (getChar(length() - i) != yapiString.getChar(yapiString.length() - i)) {
                return false;
            }
        }
        return true;
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
        if (r) {
            reverse();
            return new YAPIString(st).reverse();
        }
        return new YAPIString(st);
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
            return new YAPIString(st).reverse();
        }
        throw new IllegalArgumentException();
    }

    public void insert(char[] toInsert, int index) {
        char[] cs = new char[chars.length + toInsert.length];
        int l = toInsert.length;
        System.arraycopy(chars, 0, cs, 0, index);
        System.arraycopy(toInsert, 0, cs, index, l);
        for (int i = 0; i < chars.length - index; i++) {
            cs[i + index + l] = chars[i + index];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void insert(String s, int index) {
        if (index < 0) {
            throw new YAPIStringException("Index needs to be positive");
        }
        char[] cs = new char[chars.length + s.length()];
        int l = s.length();
        System.arraycopy(chars, 0, cs, 0, index);
        System.arraycopy(s.toCharArray(), 0, cs, index, l);
        for (int i = 0; i < chars.length - index; i++) {
            cs[i + index + l] = chars[i + index];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void insert(StringBuilder st, int index) {
        if (index < 0) {
            throw new YAPIStringException("Index needs to be positive");
        }
        insert(new YAPIString(st), index);
    }

    public void insert(YAPIString yapiString, int index) {
        if (index < 0) {
            throw new YAPIStringException("Index needs to be positive");
        }
        char[] cs = new char[chars.length + yapiString.length()];
        int l = yapiString.length();
        System.arraycopy(chars, 0, cs, 0, index);
        System.arraycopy(yapiString.chars, 0, cs, index, l);
        for (int i = 0; i < chars.length - index; i++) {
            cs[i + index + l] = chars[i + index];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void insertOrientation(YAPIString yapiString, int index) {
        if (index < 0) {
            throw new YAPIStringException("Index needs to be positive");
        }
        char[] cs = new char[chars.length + yapiString.length()];
        int l = yapiString.length();
        System.arraycopy(chars, 0, cs, 0, index);
        for (int i = 0; i < yapiString.length(); i++) {
            cs[i + index] = yapiString.getChar(i);
        }
        for (int i = 0; i < chars.length - index; i++) {
            cs[i + index + l] = chars[i + index];
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

    public void insert(YAPIString yapiString, int index, boolean ignoreOrientation) {
        if (ignoreOrientation) {
            insert(yapiString, index);
        } else {
            insertOrientation(yapiString, index);
        }
    }

    public void append(char c) {
        char[] cs = Arrays.copyOf(chars, chars.length + 1);
        cs[cs.length - 1] = c;
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void append(char... append) {
        if (append.length == 1) {
            append(append[0]);
            return;
        }
        char[] cs = Arrays.copyOf(chars, chars.length + append.length);
        System.arraycopy(append, 0, cs, chars.length, append.length);
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void append(String s) {
        append(s.toCharArray());
    }

    public void append(StringBuilder st) {
        if (st.length() == 0) {
            return;
        }
        if (st.length() == 1) {
            append(st.charAt(0));
            return;
        }
        char[] cs = Arrays.copyOf(chars, chars.length + st.length());
        st.getChars(0, st.length(), cs, chars.length);
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void append(YAPIString yapiString) {
        append(yapiString.chars);
    }

    public void append(byte b) {
        byte[] bs = Arrays.copyOf(bytes, bytes.length + 1);
        bs[bs.length - 1] = b;
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

    public void append(byte[] append) {
        if (append.length == 1) {
            append(append[0]);
            return;
        }
        byte[] bs = Arrays.copyOf(bytes, bytes.length + append.length);
        System.arraycopy(append, 0, bs, bytes.length, append.length);
        bytes = bs;
        chars = StringFormatting.toChars(bytes);
    }

    public void deleteLast() {
        chars = Arrays.copyOf(chars, chars.length - 1);
        bytes = StringFormatting.toBytes(chars);
    }

    public void deleteFirst(int size) {
        if (size < 0) {
            throw new YAPIStringException("Size cannot be negative");
        }
        char[] cs = new char[chars.length - size];
        for (int i = size; i < chars.length; i++) {
            cs[i - size] = chars[i];
        }
        chars = cs;
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

    public void deleteLast(int size) {
        if (size < 0) {
            throw new YAPIStringException("Size cannot be negative");
        }
        chars = Arrays.copyOf(chars, chars.length - size);
        bytes = StringFormatting.toBytes(chars);
    }

    public int occurrences(StringBuilder st) {
        return occurrences(new YAPIString(st));
    }

    public boolean contains(String s) {
        return contains(new YAPIString(s));
    }

    public int occurrences(YAPIString yapiString) {
        // TODO: fix abcabcabd -> abcabd
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

    public boolean contains(StringBuilder st) {
        return contains(new YAPIString(st));
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

    public boolean contains(YAPIString yapiString) {
        // TODO: Done
        int index = 0;
        int jumpIndex = -1;
        int i = 0;
        while (i <= chars.length) {
            if (chars.length == i) {
                if (jumpIndex == -1) {
                    break;
                } else {
                    i = jumpIndex;
                    jumpIndex = -1;
                }
            }
            if (chars[i] == yapiString.getChar(index)) {
                index++;
            } else if (index > 0) {
                index = 0;
                if (jumpIndex != -1) {
                    i = jumpIndex;
                    jumpIndex = -1;
                }
            }
            if (chars[i] == yapiString.getChar(0) && index > 1 && i + yapiString.length() - 1 < chars.length && jumpIndex == -1 && chars[i + yapiString.length() - 1] == yapiString.getChar(yapiString.length() - 1)) {
                jumpIndex = i - 1;
            }
            i++;

            if (index == yapiString.length()) {
                return true;
            }
        }
        return false;
    }

    public int occurrences(byte... bs) {
        // TODO: fix abcabcabd -> abcabd
        int count = 0;
        int index = 0;
        for (int i = 0; i < length(); i++) {
            if (bytes[i] == bs[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == bs.length) {
                count++;
                index = 0;
            }
        }
        return count;
    }

    public boolean contains(byte... bs) {
        // TODO: fix abcabcabd -> abcabd
        int index = 0;
        for (int i = 0; i < length(); i++) {
            if (bytes[i] == bs[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == bs.length) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(String s) {
        return indexOf(s, 0);
    }

    public int indexOf(String s, int start) {
        // TODO: fix abcabcabd -> abcabd
        int index = 0;
        if (start >= length()) {
            return -1;
        }
        for (int i = start; i < length(); i++) {
            if (s.charAt(index) == getChar(i)) {
                index++;
            } else {
                index = 0;
            }

            if (index == s.length()) {
                return i - s.length();
            }
        }
        return -1;
    }

    public int indexOf(YAPIString yapiString) {
        return indexOf(yapiString.chars, 0);
    }

    public int indexOf(YAPIString yapiString, int start) {
        return indexOf(yapiString.chars, start);
    }

    public int indexOf(char... chars) {
        return indexOf(chars, 0);
    }

    public int indexOf(char[] chars, int start) {
        // TODO: fix abcabcabd -> abcabd
        int index = 0;
        if (start >= length()) {
            return -1;
        }
        for (int i = start; i < length(); i++) {
            if (getChar(i) == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                return i - chars.length;
            }
        }
        return -1;
    }

    public int indexOf(char c) {
        return indexOf(c, 0);
    }

    public int indexOf(char c, int start) {
        if (start >= length()) {
            return -1;
        }
        for (int i = start; i < length(); i++) {
            if (getChar(i) == c) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(byte... bytes) {
        return indexOf(bytes, 0);
    }

    public int indexOf(byte[] bytes, int start) {
        // TODO: fix abcabcabd -> abcabd
        if (start >= length()) {
            return -1;
        }
        int index = 0;
        for (int i = start; i < bytes.length; i++) {
            if (getByte(i) == bytes[i]) {
                index++;
            } else {
                index = 0;
            }

            if (index == bytes.length) {
                return i - bytes.length;
            }
        }
        return -1;
    }

    public int indexOf(byte b) {
        return indexOf(b, 0);
    }

    public int indexOf(byte b, int start) {
        if (start >= length()) {
            return -1;
        }
        for (int i = start; i < length(); i++) {
            if (getByte(i) == b) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(String s) {
        return lastIndexOf(s, length());
    }

    public int lastIndexOf(String s, int start) {
        // TODO: fix abcabcabd -> abcabd
        if (start < 0) {
            return -1;
        }
        int off = Math.min(start, length() - 1);
        int index = s.length() - 1;
        for (; off >= 0; off--) {
            if (getChar(off) == s.charAt(index)) {
                index--;
            } else {
                index = s.length() - 1;
            }

            if (index == 0) {
                return off;
            }
        }
        return -1;
    }

    public int lastIndexOf(YAPIString yapiString) {
        return lastIndexOf(yapiString.chars, length());
    }

    public int lastIndexOf(YAPIString yapiString, int start) {
        return lastIndexOf(yapiString.chars, start);
    }

    public int lastIndexOf(char... chars) {
        return lastIndexOf(chars, length());
    }

    public int lastIndexOf(char[] chars, int start) {
        // TODO: fix abcabcabd -> abcabd
        if (start < 0) {
            return -1;
        }
        int off = Math.min(start, length() - 1);
        int index = chars.length - 1;
        for (; off >= 0; off--) {
            if (getChar(off) == chars[index]) {
                index--;
            } else {
                index = chars.length - 1;
            }

            if (index == 0) {
                return off;
            }
        }
        return -1;
    }

    public int lastIndexOf(char c) {
        return lastIndexOf(c, length());
    }

    public int lastIndexOf(char c, int start) {
        if (start < 0) {
            return -1;
        }
        int off = Math.min(start, length() - 1);
        for (; off >= 0; off--) {
            if (getChar(off) == c) {
                return off;
            }
        }
        return -1;
    }

    public int lastIndexOf(byte... bytes) {
        return lastIndexOf(bytes, length());
    }

    public int lastIndexOf(byte[] bytes, int start) {
        // TODO: fix abcabcabd -> abcabd
        if (start < 0) {
            return -1;
        }
        int off = Math.min(start, length() - 1);
        int index = bytes.length - 1;
        for (; off >= 0; off--) {
            if (getByte(off) == bytes[index]) {
                index--;
            } else {
                index = bytes.length - 1;
            }

            if (index == 0) {
                return off;
            }
        }
        return -1;
    }

    public int lastIndexOf(byte b) {
        for (int i = length() - 1; i >= 0; i--) {
            if (getByte(i) == b) {
                return i;
            }
        }
        return -1;
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

    public int lastIndexOf(byte b, int start) {
        if (start < 0) {
            return -1;
        }
        int off = Math.min(start, length() - 1);
        for (; off >= 0; off--) {
            if (getByte(off) == b) {
                return off;
            }
        }
        return -1;
    }

    public YAPIString longestCommonPrefix(YAPIString... yapiStrings) {
        if (yapiStrings.length == 0) {
            throw new YAPIStringException("At least one input is needed");
        }
        YAPIString output = null;
        for (int i = 0; i < yapiStrings.length; i++) {
            output = longestCommonPrefix(yapiStrings[i]);
            if (output.length() == 0) {
                return output;
            }
        }
        return output;
    }

    public YAPIString longestCommonSuffix(YAPIString yapiString) {
        int length = Math.min(yapiString.length(), length());
        StringBuilder st = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int tI = length() - i;
            int yI = yapiString.length() - i;
            if (yapiString.getChar(yI) != getChar(tI)) {
                return new YAPIString(st).reverse();
            }
            st.append(getChar(tI));
        }
        return new YAPIString(st);
    }

    public YAPIString longestCommonSuffix(YAPIString... yapiStrings) {
        if (yapiStrings.length == 0) {
            throw new YAPIStringException("At least one input is needed");
        }
        YAPIString output = null;
        for (int i = 0; i < yapiStrings.length; i++) {
            output = longestCommonSuffix(yapiStrings[i]);
            if (output.length() == 0) {
                return output;
            }
        }
        return output;
    }

    public int longestCommonSubstring(YAPIString yapiString) {
        return lcss(yapiString, length(), yapiString.length());
    }

    private int lcss(YAPIString yapiString, int m, int n) {
        int[][] tree = new int[m + 1][n + 1];
        int result = 0;

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    tree[i][j] = 0;
                } else if (getChar(i - 1) == yapiString.getChar(j - 1)) {
                    tree[i][j] = tree[i - 1][j - 1] + 1;
                    result = Integer.max(result, tree[i][j]);
                } else {
                    tree[i][j] = 0;
                }
            }
        }
        return result;
    }

    public int longestCommonSubsequence(YAPIString yapiString) {
        return lcs(yapiString, length(), yapiString.length());
    }

    private int lcs(YAPIString yapiString, int m, int n) {
        int[][] tree = new int[m+1][n+1];
        for (int i=0; i<=m; i++) {
            for (int j=0; j<=n; j++) {
                if (i == 0 || j == 0) {
                    tree[i][j] = 0;
                } else if (getChar(i-1) == yapiString.getChar(j-1)) {
                    tree[i][j] = tree[i - 1][j - 1] + 1;
                } else {
                    tree[i][j] = Math.max(tree[i - 1][j], tree[i][j - 1]);
                }
            }
        }
        return tree[m][n];
    }

    public void toLowerCase() {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char)(chars[i] + ' ');
            }
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void toUpperCase() {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = (char)(chars[i] - ' ');
            }
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void replace(char oldChar, char newChar) {
        if (oldChar == newChar) {
            return;
        }
        for (int i = 0; i < length(); i++) {
            if (chars[i] == oldChar) {
                chars[i] = newChar;
            }
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void replace(String oldS, String newS) {
        replace(oldS.toCharArray(), newS.toCharArray());
    }

    public void replace(YAPIString oldString, YAPIString newString) {
        replace(oldString.chars, newString.chars);
    }

    public void replace(char[] oldChars, char[] newChars) {
        if (Arrays.equals(oldChars, newChars)) {
            return;
        }
        int i;
        while ((i = indexOf(oldChars)) != -1) {
            for (int j = 0; j < oldChars.length; j++) {
                delete(i);
            }
            insert(newChars, i);
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void replaceFirst(char oldChar, char newChar) {
        if (oldChar == newChar) {
            return;
        }
        int i = indexOf(oldChar);
        if (i != -1) {
            chars[i] = newChar;
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void replaceLast(char oldChar, char newChar) {
        if (oldChar == newChar) {
            return;
        }
        int i = lastIndexOf(oldChar);
        if (i != -1) {
            chars[i] = newChar;
        }
        bytes = StringFormatting.toBytes(chars);
    }

    public void trim() {
        int start = 0;
        for (int i = 0; i < length(); i++) {
            if (chars[i] == ' ') {
                start++;
            } else {
                break;
            }
        }
        if (start >= length()) {
            return;
        }
        int end = 0;
        for (int i = length() - 1; i >= 0; i--) {
            if (chars[i] == ' ') {
                end++;
            } else {
                break;
            }
        }
        char[] cs = new char[length() - start - end];
        for (int i = start; i < length() - end; i++) {
            cs[i - start] = chars[i];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void strip() {
        int start = 0;
        for (int i = 0; i < length(); i++) {
            if (Character.isWhitespace(chars[i])) {
                start++;
            } else {
                break;
            }
        }
        if (start >= length()) {
            return;
        }
        int end = 0;
        for (int i = length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(chars[i])) {
                end++;
            } else {
                break;
            }
        }
        char[] cs = new char[length() - start - end];
        for (int i = start; i < length() - end; i++) {
            cs[i - start] = chars[i];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void stripLeading() {
        int start = 0;
        for (int i = 0; i < length(); i++) {
            if (Character.isWhitespace(chars[i])) {
                start++;
            } else {
                break;
            }
        }
        if (start >= length()) {
            return;
        }
        char[] cs = new char[length() - start];
        for (int i = start; i < length(); i++) {
            cs[i - start] = chars[i];
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public void stripTrailing() {
        int end = 0;
        for (int i = length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(chars[i])) {
                end++;
            } else {
                break;
            }
        }
        if (end >= length()) {
            return;
        }
        chars = Arrays.copyOf(chars, length() - end);
        bytes = StringFormatting.toBytes(chars);
    }

    public void indent(int indent) {
        // TODO: End Implemetation
        if (indent == 0) {
            return;
        }
        if (indent > 0) {

        } else {

        }
    }

    public static void main(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long time = 0;
        int cycles = 1000000;
        for (int i = 1; i <= cycles; i++) {
            YAPIString yapiString = new YAPIString("HelloWorld");
            Timer timer = new Timer();
            timer.start();
            yapiString.repeat(10000);
            timer.stop();
            time += timer.getTime() / 1000;
            if (i % (cycles / 10) == 0) {
                System.out.println(i + " " + time);
            }
        }
        System.out.println(time / (double)cycles + "µs");

        // length:10 repeat:10    cycles:10000000 time:   0.0908675 µs
        // length:10 repeat:100   cycles:10000000 time:   1.4532998 µs
        // length:10 repeat:1000  cycles:10000000 time:  14.248193  µs
        // length:10 repeat:10000 cycles:1000000  time: 254.308807  µs
    }

    public int[] indentionLevel() {
        int[] ints = new int[occurrences('\n') + 1];
        int index = 0;
        int indention = 0;
        boolean tracking = true;
        for (int i = 0; i < length(); i++) {
            if (getChar(i) == ' ' && tracking) {
                indention++;
            } else if (getChar(i) == '\n') {
                tracking = true;
                ints[index++] = indention;
                indention = 0;
            } else if (getChar(i) != ' ') {
                tracking = false;
            }
        }
        if (indention != 0) {
            ints[index] = indention;
        }
        return ints;
    }

    public void repeat(int count) {
        if (count < 2) {
            return;
        }
        char[] cs = new char[chars.length * count];
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < count; j++) {
                cs[i + j * chars.length] = chars[i];
            }
        }
        chars = cs;
        bytes = StringFormatting.toBytes(chars);
    }

    public YAPIString[] lines() {
        StringBuilder st = new StringBuilder();
        int index = 0;
        YAPIString[] yapiStrings = new YAPIString[occurrences('\n')];
        for (int i = 0; i < length(); i++) {
            if (getChar(i) == '\n') {
                yapiStrings[index] = new YAPIString(st);
                st = new StringBuilder();
            } else {
                st.append(getChar(i));
            }
        }
        return yapiStrings;
    }

    public String toHex() {
        return toHexFromChars(false);
    }

    public String toHex(boolean spaces) {
        return toHexFromChars(spaces);
    }

    public YAPIString toHexYAPIString() {
        return toHexYAPIStringFromChars(false);
    }

    public YAPIString toHexYAPIString(boolean spaces) {
        return toHexYAPIStringFromChars(spaces);
    }

    public String toHexFromChars(boolean spaces) {
        return StringFormatting.toHex(chars, spaces);
    }

    public YAPIString toHexYAPIStringFromChars(boolean spaces) {
        return new YAPIString(toHexFromChars(spaces));
    }

    public String toHexFromBytes(boolean spaces) {
        return StringFormatting.toHex(bytes, spaces);
    }

    public YAPIString toHexYAPIStringFromBytes(boolean spaces) {
        return new YAPIString(toHexFromBytes(spaces));
    }

    public String hash() {
        return StringCrypting.hash(toString(), false);
    }

    public String hash(HashType hashType) {
        return StringCrypting.hash(toString(), hashType, false);
    }

    public String hash(boolean spaces) {
        return StringCrypting.hash(toString(), spaces);
    }

    public String hash(HashType hashType, boolean spaces) {
        return StringCrypting.hash(toString(), hashType, spaces);
    }

    public YAPIString copy() {
        return new YAPIString(this);
    }

    public YAPIString copy(boolean includeState) {
        if (!includeState) {
            return copy();
        }
        YAPIString yapiString = new YAPIString(Arrays.copyOf(chars, chars.length));
        if (reversed) {
            yapiString.reverse();
        }
        return yapiString;
    }

    public void join(YAPIString... yapiStrings) {
        for (YAPIString yapiString : yapiStrings) {
            append(yapiString);
        }
    }

    public void join(YAPIString delimiter, YAPIString... yapiStrings) {
        for (int i = 0; i < yapiStrings.length; i++) {
            if (i != 0) {
                append(delimiter);
            }
            append(yapiStrings[i]);
        }
    }

    public boolean equals(YAPIString yapiString) {
        if (this == yapiString) {
            return true;
        }
        if (length() != yapiString.length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            if (getChar(i) != yapiString.getChar(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean equalsLossy(YAPIString yapiString) {
        if (this == yapiString) {
            return true;
        }
        if (length() != yapiString.length()) {
            return false;
        }
        return Arrays.equals(chars, yapiString.chars);
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            st.append(getChar(i));
        }
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIString)) return false;
        YAPIString that = (YAPIString) o;
        return reversed == that.reversed &&
                Arrays.equals(chars, that.chars) &&
                Arrays.equals(bytes, that.bytes);
    }

    public boolean equalsIgnoreCase(YAPIString yapiString) {
        if (length() != yapiString.length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            char c1 = yapiString.getChar(i);
            char c2 = getChar(i);
            if (c1 >= 'A' && c1 <= 'Z') {
                c1 += ' ';
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                c2 += ' ';
            }
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reversed);
        result = 31 * result + Arrays.hashCode(chars);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}