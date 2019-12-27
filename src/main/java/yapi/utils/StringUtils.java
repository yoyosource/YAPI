package yapi.utils;

import yapi.exceptions.NoStringException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println(formatText("Hello &&{arrow} World"));
        System.out.println(unformatText(formatText("Hello &&{arrow} World")));
        System.out.println(formatText(unformatText(formatText("Hello &&{arrow} World"))));
    }

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
                output.append(convertSpecial(st.toString()));
            } else if (and == 2) {
                output.append(chars[i - 2]).append(chars[i - 1]);
            }
            if (chars[i] == '&') {
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
            if (c < ' ' || c > '~') {
                st.append("&&{$x").append(String.format("%02X", (int)c)).append("}");
            } else {
                st.append(c);
            }
        }
        return st.toString();
    }

    private static String convertSpecial(String s) {
        if (!s.matches("(\\{[a-z]+(:[a-z]+)*?})|(\\{\\$x[0-9A-F]+})")) {
            return s;
        }
        s = s.substring(1, s.length() - 1);

        if (s.matches("\\$x[0-9A-F]+")) {
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
     * @since Version 1
     *
     * @param string
     * @return
     */
    public static byte[] toBytes(String string) {
        char[] chars = string.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    /**
     *
     * @since Version 1
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
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
        if (s == null) throw new NullPointerException();
        if (t == null) throw new NullPointerException();
        if (s.isEmpty()) throw new NoStringException("No String");
        if (t.isEmpty()) throw new NoStringException("No String");
        if (!s.contains(t)) return new ArrayList<>();

        List<Integer> occurrences = new ArrayList<>();
        char[] charsS = s.toCharArray();
        char[] charsT = t.toCharArray();

        for (int i = 0; i <= charsS.length - charsT.length; i++) {
            boolean b = true;
            for (int j = 0; j < charsT.length; j++) {
                if (charsS[i + j] != charsT[j]) {
                    b = false;
                }
            }
            if (b) {
                occurrences.add(i);
            }
        }
        return occurrences;
    }

    public static byte[] hash(String s, String hashType) {
        if (!(hashType.equals("MD5") || hashType.equals("SHA-1") || hashType.equals("SHA-256"))) {
            hashType = "SHA-256";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType);
            return digest.digest((s + "").getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @return
     */
    public static String[] splitWords(String string) {
        return splitString(string, new String[]{" ", "\n", "\t", ",", ".", "-", "!", "?", ";"}, false, false, false);
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @param reviveSplitted
     * @return
     */
    public static String[] splitWords(String string, boolean reviveSplitted) {
        return splitString(string, new String[]{" ", "\n", "\t", ",", ".", "-", "!", "?", ";"}, reviveSplitted, false, false);
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @param splitStrings
     * @return
     */
    public static String[] splitString(String string, String[] splitStrings) {
        return splitString(string, splitStrings, false, false, false);
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @param splitStrings
     * @param reviveSplitted
     * @return
     */
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted) {
        return splitString(string, splitStrings, reviveSplitted, false, false);
    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @param splitStrings
     * @param reviveSplitted
     * @param addToLast
     * @return
     */
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
        return splitString(string, splitStrings, reviveSplitted, addToLast, false);

    }

    /**
     *
     * @since Version 1
     *
     * @param string
     * @param splitStrings
     * @param reviveSplitted
     * @param addToLast
     * @param splitInStrings
     * @return
     */
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast, boolean splitInStrings) {
        if (string == null) throw new NullPointerException();
        if (splitStrings == null) throw new NullPointerException();
        if (string.isEmpty()) throw new NoStringException("No String");
        if (splitStrings.length == 0) throw new NoStringException("No Split Strings");

        char[] chars = string.toCharArray();

        List<String> words = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int i = 0;
        int lastSplit = 0;

        boolean inString = false;
        boolean escape = false;

        while (i < chars.length) {
            int splitStringTest = 0;
            char c = chars[i];
            if (c == '"' && !escape && !splitInStrings) {
                inString = !inString;
            }
            if (inString && c == '\\' && !splitInStrings) {
                escape = true;
                i++;
                stringBuilder.append(c);
                continue;
            }
            if (inString) {
                i++;
                stringBuilder.append(c);
                escape = false;
                continue;
            }
            String s = "";
            for (String st : splitStrings) {
                StringBuilder sb = new StringBuilder();
                int index = i;
                int currentIndex = i;
                while (currentIndex < chars.length && currentIndex < index + st.length()) {
                    sb.append(chars[currentIndex]);
                    currentIndex++;
                }
                if (sb.toString().equals(st)) {
                    if (s.length() == 0) {
                        s = st;
                    }
                    splitStringTest++;
                }
            }

            if (splitStringTest == 0) {
                stringBuilder.append(c);
            } else {
                i += s.length() - 1;
                if (stringBuilder.length() == 0) {
                    if (reviveSplitted && !addToLast) {
                        words.add(s);
                    } else if (reviveSplitted && addToLast) {
                        words.add(stringBuilder + s);
                    }
                    stringBuilder = new StringBuilder();
                    lastSplit = i;
                    i++;
                    continue;
                }
                if (reviveSplitted) {
                    if (addToLast) {
                        words.add(stringBuilder.toString() + s);
                    } else {
                        words.add(stringBuilder.toString());
                        words.add(s);
                    }
                } else {
                    words.add(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                lastSplit = i;
            }
            i++;
        }
        if (lastSplit != string.length()) {
            if (stringBuilder.length() == 0) {
                return words.toArray(new String[0]);
            }
            words.add(stringBuilder.toString());
        }
        return words.toArray(new String[0]);
    }

}
