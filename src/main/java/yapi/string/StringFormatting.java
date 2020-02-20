// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.internal.exceptions.string.NoStringException;
import yapi.math.NumberRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringFormatting {

    private static Map<String, String> specialMap = new HashMap<>();

    static {
        specialMap.put("until", "–");
        specialMap.put("until:long", "—");
        specialMap.put("underscore", "_");
        specialMap.put("upperscore", "¯");

        specialMap.put("arrow:right", "→");
        specialMap.put("arrow", "#arrow:right");
        specialMap.put("arrow:left", "←");
        specialMap.put("arrow:small:right", "›");
        specialMap.put("arrow:small:left", "‹");
        specialMap.put("arrow:double:right", "»");
        specialMap.put("arrow:double:left", "»");
        specialMap.put("arrow:up", "↑");
        specialMap.put("arrow:down", "↓");
        specialMap.put("arrow:both:right", "↔");
        specialMap.put("arrow:both:left", "↔");
        specialMap.put("arrow:both:up", "↕");
        specialMap.put("arrow:both:down", "↕");

        specialMap.put("dot", "•");
        specialMap.put("alpha", "α");
        specialMap.put("beta", "β");
        specialMap.put("gamma", "γ");
        specialMap.put("delta", "Δ");

        specialMap.put("copyright", "©");
        specialMap.put("tm", "™");
        specialMap.put("trademark", "#tm");
        specialMap.put("dot:dot:dot", "…");
        specialMap.put("born", "*");
        specialMap.put("died", "†");

        specialMap.put("pipe:up:down", "│");
        specialMap.put("pipe:down:up", "#pipe:up:down");
        specialMap.put("pipe:left:right", "─");
        specialMap.put("pipe:right:left", "#pipe:right:left");
        specialMap.put("pipe:down:right", "┌");
        specialMap.put("pipe:right:down", "#pipe:down:left");
        specialMap.put("pipe:down:left", "┐");
        specialMap.put("pipe:left:down", "#pipe:left:down");
        specialMap.put("pipe:up:right", "└");
        specialMap.put("pipe:right:up", "#pipe:up:right");
        specialMap.put("pipe:up:left", "┘");
        specialMap.put("pipe:left:up", "#pipe:up:left");
        specialMap.put("pipe:up:right:down", "├");
        specialMap.put("pipe:up:down:right", "#pipe:up:right:down");
        specialMap.put("pipe:right:up:down", "#pipe:up:right:down");
        specialMap.put("pipe:right:down:up", "#pipe:up:right:down");
        specialMap.put("pipe:down:right:up", "#pipe:up:right:down");
        specialMap.put("pipe:down:up:right", "#pipe:up:right:down");
        // rud
        // rdu
        // urd
        // udr
        // dru
        // dur
        specialMap.put("pipe:up:left:down", "┤");
        specialMap.put("pipe:up:left:down", "#pipe:up:right_down");

        specialMap.put("currency", "¤");
        specialMap.put("euro", "€");
        specialMap.put("cent", "¢");
        specialMap.put("yen", "¥");
        specialMap.put("pound", "£");
        specialMap.put("dollar", "$");

        specialMap.put("times", "×");
        specialMap.put("divide", "÷");
        specialMap.put("plus", "+");
        specialMap.put("minus", "-");
        specialMap.put("plus:minus", "±");
        specialMap.put("minus:plus", "±");
        specialMap.put("root", "√");
        specialMap.put("sum", "∑");
        specialMap.put("mult", "∏");
        specialMap.put("integral", "∫");
        specialMap.put("approximate", "≈");
        specialMap.put("equals", "=");
        specialMap.put("unequals", "≠");
        specialMap.put("not", "¬");

        specialMap.put("milli", "m");
        specialMap.put("micro", "µ");
        specialMap.put("percent", "%");
        specialMap.put("permillion", "‰");

        specialMap.put("infinite", "∞");

        // @>--}---
        // @>--,---
    }

    private StringFormatting() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatText(String s) {
        char[] chars = s.toCharArray();
        int and = 0;
        int i = 0;
        StringBuilder output = new StringBuilder();
        while (i < chars.length) {
            if (and == 2 && chars[i] == '{') {
                StringBuilder st = new StringBuilder();
                int bracket = 0;
                do {
                    if (chars[i] == '}') {
                        bracket--;
                    }
                    st.append(chars[i]);
                    if (chars[i] == '{') {
                        bracket++;
                    }
                    i++;
                } while (bracket != 0);
                i--;
                output.append(convertSpecial(st.toString()));
                and = 0;
            } else if (and == 2) {
                output.append(chars[i - 2]).append(chars[i - 1]);
            } else if (chars[i] == '&') {
                and++;
            } else {
                output.append(chars[i]);
                and = 0;
            }
            i++;
        }
        return output.toString();
    }

    public static String unformatText(String s) {
        char[] chars = s.toCharArray();
        StringBuilder st = new StringBuilder();
        for (char c : chars) {
            if (c < ' ' || c > '~' || c == '{') {
                st.append("&&{$x").append(String.format("%04X", (int)c)).append("}");
            } else {
                st.append(c);
            }
        }
        return st.toString();
    }

    private static String convertSpecial(String s) {
        if (!s.matches("(\\{[a-z]+(:[a-z]+)*?})|(\\{\\$x[0-9A-F]{4}})")) {
            return s;
        }
        s = s.substring(1, s.length() - 1);

        if (s.matches("\\$x[0-9A-F]{4}")) {
            s = s.substring(2);
            return (char)Integer.parseInt(s, 16) + "";
        }

        if (!specialMap.containsKey(s)) {
            return "&&{" + s + "}";
        }

        do {
            if (s.startsWith("#")) {
                s = s.substring(1);
            }
            if (specialMap.containsKey(s)) {
                if (s.equals(specialMap.get(s))) {
                    return "&&{" + s + "}";
                }
                s = specialMap.get(s);
            }
        } while (s.startsWith("#"));

        return s;
    }

    public static String insertSpaces(String s) {
        if (contains(s, ' ')) {
            return s;
        }
        char[] chars = s.toCharArray();
        int lastSpace = 0;
        StringBuilder st = new StringBuilder();
        NumberRandom numberRandom = new NumberRandom();
        for (int i = 0; i < chars.length; i++) {
            double d = (sigmoid(lastSpace - 2.0)) - (1 - sigmoid(chars.length - (double)i));
            if (numberRandom.getDouble(1) < d) {
                st.append(' ');
            }
            st.append(chars[i]);
        }

        return st.toString();
    }

    private static double sigmoid(double input) {
        return (1 / (1 + Math.exp(-input)));
    }

    /**
     *
     * @since Version 1
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        return toHex(bytes, false);
    }

    /**
     *
     * @param bytes
     * @param spaces
     * @return
     */
    public static String toHex(byte[] bytes, boolean spaces) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (byte b : bytes) {
            if (spaces && t) st.append(' ');
            st.append(String.format("%02X", b));
            t = true;
        }
        return st.toString();
    }

    /**
     *
     * @param chars
     * @return
     */
    public static String toHex(char[] chars) {
        return toHex(chars, false);
    }

    /**
     *
     * @param chars
     * @param spaces
     * @return
     */
    public static String toHex(char[] chars, boolean spaces) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (char b : chars) {
            if (spaces && t) st.append(' ');
            st.append(String.format("%04X", (int)b));
            t = true;
        }
        return st.toString();
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @return
     */
    public static byte[] toBytes(String string) {
        if (string.matches("[A-F0-9]{2}( [A-F0-9]{2})*")) {
            StringBuilder st = new StringBuilder();
            int index = 0;
            char[] chars = string.toCharArray();
            byte[] output = new byte[occurrences(string, ' ') + 1];
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == ' ') {
                    output[index++] = (byte)(Integer.parseInt(st.toString(), 16));
                    st = new StringBuilder();
                } else {
                    st.append(chars[i]);
                }
            }
            if (st.length() != 0) {
                output[index] = (byte)(Integer.parseInt(st.toString(), 16));
            }
            return output;
        } else {
            return string.getBytes();
        }
    }

    public static byte[] toBytes(char[] chars) {
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte)chars[i];
        }
        return bytes;
    }

    public static char[] toChars(String string) {
        if (string.matches("[A-F0-9]{2,4}( [A-F0-9]{2,4})*")) {
            StringBuilder st = new StringBuilder();
            int index = 0;
            char[] chars = string.toCharArray();
            char[] output = new char[occurrences(string, ' ') + 1];
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == ' ') {
                    output[index++] = (char)(Integer.parseInt(st.toString(), 16));
                    st = new StringBuilder();
                } else {
                    st.append(chars[i]);
                }
            }
            if (st.length() != 0) {
                output[index] = (char)(Integer.parseInt(st.toString(), 16));
            }
            return output;
        } else {
            return string.toCharArray();
        }
    }

    public static char[] toChars(byte[] bytes) {
        // Todo: Encoding?
        char[] chars = new char[bytes.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)bytes[i];
        }
        return chars;
    }

    /**
     *
     * @since Version 1
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        // Todo: Encoding?
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append((char)b);
        }
        return st.toString();
    }

    /**
     *
     * @since Version 1
     *
     * @param chars
     * @return
     */
    public static String toString(char[] chars) {
        StringBuilder st = new StringBuilder();
        for (char c : chars) {
            st.append(c);
        }
        return st.toString();
    }

    public static String checksum(String s) {
        return toHex(StringCrypting.hash(s, HashType.MD5));
    }

    public static String checksum(String s, HashType hashType) {
        return toHex(StringCrypting.hash(s, hashType));
    }

    public static String checksum(String s, boolean spaces) {
        return toHex(StringCrypting.hash(s, HashType.MD5), spaces);
    }

    public static String checksum(String s, HashType hashType, boolean spaces) {
        return toHex(StringCrypting.hash(s, hashType), spaces);
    }

    /**
     *
     * @param s
     * @param c
     * @return
     */
    public static boolean contains(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param s
     * @param c
     * @return
     */
    public static int occurrences(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param s
     * @param chars
     * @return
     */
    public static boolean contains(String s, char[] chars) {
        // Todo: Fix cotains Error
        int index = 0;
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                return true;
            }
        }

        return false;
    }

    public static boolean contains(String s, String t) {
        return contains(s, t.toCharArray());
    }

    /**
     *
     * @param s
     * @param chars
     * @return
     */
    public static int occurrences(String s, char[] chars) {
        int index = 0;
        int count = 0;
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                count++;
                index = 0;
            }
        }

        return count;
    }

    public static int occurrences(String s, String t) {
        return occurrences(s, t.toCharArray());
    }

    /**
     *
     * @since Version 1
     *
     * @param s
     * @param i
     * @return
     */
    public static String substring(String s, int i) {
        if (i < 0) {
            int j = s.length() + i;
            if (j < 0) {
                j = 0;
            }
            return s.substring(j);
        }
        return s.substring(i);
    }

    /**
     *
     * @since Version 1
     *
     * @param s
     * @param t
     * @return
     */
    public static String getLongestCommonPrefix(String s, String t) {
        int i = 1;
        while (t.startsWith(s.substring(0, i))) {
            if (i == s.length() - 1) {
                return s;
            }
            if (i == t.length() - 1) {
                return t;
            }
            i++;
        }
        return s.substring(0, i - 1);
    }

    /**
     *
     * @since Version 1
     *
     * @param s
     * @param t
     * @return
     */
    public static List<Integer> getOccurrences(String s, String t) {
        // Todo: Refactor Exceptions (Empty String)
        if (s == null) throw new NullPointerException();
        if (t == null) throw new NullPointerException();
        if (s.isEmpty()) throw new NoStringException("No String");
        if (t.isEmpty()) throw new NoStringException("No String");
        if (!s.contains(t)) return new ArrayList<>();

        List<Integer> occurrences = new ArrayList<>();
        char[] chars = t.toCharArray();
        int index = 0;
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                occurrences.add(index - chars.length);
                index = 0;
            }
        }

        return occurrences;
    }

}