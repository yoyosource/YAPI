package yapi.utils;

import yapi.exceptions.NoStringException;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String toHex(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append(String.format("%02X", b));
        }
        return st.toString();
    }

    public static byte[] toBytes(String string) {
        char[] chars = string.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    public static String toString(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append((char)b);
        }
        return st.toString();
    }

    public static String toString(char[] chars) {
        StringBuilder st = new StringBuilder();
        for (char c : chars) {
            st.append(c);
        }
        return st.toString();
    }

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

    public static String[] splitWords(String string) {
        return splitString(string, new String[]{" ", "\n", "\t", ",", ".", "-", "!", "?", ";"}, false, false, false);
    }

    public static String[] splitWords(String string, boolean reviveSplitted) {
        return splitString(string, new String[]{" ", "\n", "\t", ",", ".", "-", "!", "?", ";"}, reviveSplitted, false, false);
    }

    public static String[] splitString(String string, String[] splitStrings) {
        return splitString(string, splitStrings, false, false, false);
    }

    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted) {
        return splitString(string, splitStrings, reviveSplitted, false, false);
    }

    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
        return splitString(string, splitStrings, reviveSplitted, addToLast, false);

    }

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
