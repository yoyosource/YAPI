package yapi.string;

import yapi.exceptions.NoStringException;

import java.util.ArrayList;
import java.util.List;

public class StringSplitting {

    private StringSplitting() {
        throw new IllegalStateException();
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

    public static String merge(String[] strings, String s) {
        boolean b = false;
        StringBuilder st = new StringBuilder();
        for (String t : strings) {
            if (b) {
                st.append(s);
            }
            st.append(t);
            b = true;
        }
        return st.toString();
    }

}
