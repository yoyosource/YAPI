// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.normal;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Token> tokens = new ArrayList<>();

    private long time = 0;

    private static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
        char[] chars = string.toCharArray();
        if (chars.length == 0) {
            throw new NullPointerException("No String");
        }
        if (splitStrings.length == 0) {
            throw new NullPointerException("No Split Strings");
        }

        List<String> words = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int i = 0;
        int lastSplit = 0;

        boolean inString = false;
        boolean escape = false;

        while (i < chars.length) {
            int splitStringTest = 0;
            char c = chars[i];
            if (c == '"' && !escape) {
                inString = !inString;
            }
            if (inString && c == '\\') {
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

    public Lexer createTokens(String codes) {
        time = System.currentTimeMillis();
        if (!tokens.isEmpty()) {
            tokens.clear();
        }
        createTokensTec(codes);
        time = System.currentTimeMillis() - time;
        return this;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public long getTime() {
        return time;
    }

    private void createTokensTec(String codes) {
        String[] lines = codes.split("\n");
        int i = 0;
        for (String line : lines) {
            tokifyLine(line);
            if (i != lines.length - 1) {
                tokens.add(new Token("NNN", ""));
            }
            i++;
        }
    }

    private void tokifyLine(String line) {
        boolean string = false;
        boolean escape = false;

        char[] chars = line.toCharArray();

        StringBuilder st = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!string && !escape && c == ' ') {
                tokens.addAll(tokify(st.toString()));
                st = new StringBuilder();
            } else if (c == '"' || c == '\'') {
                if (!escape) {
                    string = !string;
                } else {
                    escape = false;
                }
                if (st.length() > 0 && st.charAt(0) == '"') {
                    st.append(c);
                    tokens.addAll(tokify(st.toString()));
                    st = new StringBuilder();
                    continue;
                }
                if (st.length() > 0) {
                    tokens.addAll(tokify(st.toString()));
                    st = new StringBuilder();
                }
                st.append(c);
            } else if (c == '\\' && string) {
                escape = true;
            } else {
                if (!string && !escape) {
                    boolean startLineBreak = i > 0 && chars[i - 1] != '\n';
                    boolean endLineBreak = i < chars.length - 1 && chars[i + 1] != '\n';
                    switch (c) {
                        case '{':
                            tokify(st);
                            tokify(c);
                            if (endLineBreak) {
                                tokens.add(new Token("NNN", ""));
                            }
                            st = new StringBuilder();
                            break;
                        case '}':
                            tokify(st);
                            if (startLineBreak) {
                                tokens.add(new Token("NNN", ""));
                            }
                            tokify(c);
                            if (endLineBreak) {
                                tokens.add(new Token("NNN", ""));
                            }
                            st = new StringBuilder();
                            break;
                        case '(':
                        case ')':
                        case '[':
                        case ']':
                        case ':':
                        case '+':
                        case '/':
                        case ',':
                            tokify(st);
                            tokify(c);
                            st = new StringBuilder();
                            break;
                        default:
                            st.append(c);
                            break;
                    }
                    continue;
                }
                if (escape) {
                    st.append("\\");
                }
                st.append(c);
                escape = false;
            }
        }

        if (st.length() > 0) {
            tokens.addAll(tokify(st.toString()));
        }
    }

    private void tokify(StringBuilder st) {
        tokens.addAll(tokify(st.toString()));
    }

    private void tokify(char c) {
        tokens.addAll(tokify(c + ""));
    }

    private List<Token> tokify(String s) {
        ArrayList<Token> ts = new ArrayList<>();

        if (s.length() == 0) {
            return ts;
        }

        s = s.trim();

        if (s.startsWith("'") && s.endsWith("'")) {
            s = s.replaceAll("\\\\n", "\n");
            if (s.substring(1, s.length() - 1).length() == 1) {
                ts.add(new Token("chr", s.substring(1, s.length() - 1)));
                return ts;
            }
            ts.add(new Token("str", s.substring(1, s.length() - 1)));
            return ts;
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.replaceAll("\\\\n", "\n");
            ts.add(new Token("str", s.substring(1, s.length() - 1)));
            return ts;
        }
        if (s.matches("-?(\\d+)(\\.(\\d+))")) {
            ts.add(new Token("num", Double.parseDouble(s)));
            return ts;
        }
        if (s.matches("-?(\\d+)D")) {
            ts.add(new Token("num", Double.parseDouble(s.substring(0, s.length() - 1))));
            return ts;
        }
        if (s.matches("-?(\\d+)L")) {
            ts.add(new Token("lon", Long.parseLong(s.substring(0, s.length() - 1))));
            return ts;
        }
        if (s.matches("-?(\\d+)")) {
            long l = Long.parseLong(s);
            if (l >= -2147483647 && l <= 2147483647) {
                ts.add(new Token("int", (int)l));
            } else {
                ts.add(new Token("lon", l));
            }
            return ts;
        }
        if (s.matches("(##|0x)[0-9a-f]+L")) {
            s = s.substring(2, s.length() - 1);
            long l = Long.parseLong(s, 16);
            ts.add(new Token("lon", l));
            return ts;
        }
        if (s.matches("(##|0x)[0-9a-f]+")) {
            s = s.substring(2);
            long l = Long.parseLong(s, 16);
            if (l >= -2147483647 && l <= 2147483647) {
                ts.add(new Token("int", (int)l));
            } else {
                ts.add(new Token("lon", l));
            }
            return ts;
        }
        if (s.matches("(true)|(false)")) {
            if (s.equals("true")) {
                ts.add(new Token("bol", true));
                return ts;
            } else {
                ts.add(new Token("bol", false));
                return ts;
            }
        }

        if (s.matches("[|]")) {
            ts.add(new Token("MAT", s));
            return ts;
        }
        if (s.matches("[+\\-*/%^]|(root)|(sin|cos|tan|asin|acos|atan)|(simoid|gauss|sig)|(ln|log)")) {
            ts.add(new Token("OPE", s));
            return ts;
        }
        if (s.matches("(==)|([<>][=]?)|(!=)|(equals|equalsIgnoreCase|contains|containsIgnoreCase)|(typeof|canbe)")) {
            ts.add(new Token("COM", s));
            return ts;
        }
        if (s.matches("(=)")) {
            ts.add(new Token("ASG", s));
            return ts;
        }
        if (s.matches("->")) {
            ts.add(new Token("RET", s));
            return ts;
        }
        if (s.matches("(&&)|(\\|\\|)|(!!)|(!&)|(x\\|)|(n\\|)|(xn)")) {
            ts.add(new Token("LOG", s));
            return ts;
        }

        if (s.matches("[()]")) {
            ts.add(new Token("STb", s));
            return ts;
        }
        if (s.matches("[\\[\\]]")) {
            ts.add(new Token("ACb", s));
            return ts;
        }
        if (s.matches("[{}]")) {
            ts.add(new Token("BLb", s));
            return ts;
        }
        if (s.equals(".") || s.equals(":") || s.equals(",")) {
            ts.add(new Token("SEP", s));
            return ts;
        }

        if (s.equals("*char") || s.equals("*character") || s.equals("*chr")) {
            ts.add(new Token("typ", "chr"));
            return ts;
        }
        if (s.equals("*boolean") || s.equals("*bool") || s.equals("*bol")) {
            ts.add(new Token("typ", "bol"));
            return ts;
        }
        if (s.equals("*number") || s.equals("*num")) {
            ts.add(new Token("typ", "num"));
            return ts;
        }
        if (s.equals("*integer") || s.equals("*int")) {
            ts.add(new Token("typ", "int"));
            return ts;
        }
        if (s.equals("*string") || s.equals("*str")) {
            ts.add(new Token("typ", "str"));
            return ts;
        }
        if (s.equals("*lon") || s.equals("*long")) {
            ts.add(new Token("typ", "lon"));
            return ts;
        }
        if (s.equals("*any")) {
            ts.add(new Token("typ", "any"));
            return ts;
        }

        String[] splitter = new String[]{"+", "-", "*", "/", "%", "^", "|", "(", ")", "root", "sin", "cos", "tan", "asin", "acos", "atan", "sigmoid", "gauss", "ln", "log", ",", ":", ".", "<", ">", "<=", ">=", "==", "!=", "="};
        int checks = 0;
        for (String check : splitter) {
            if (s.contains(check)) {
                checks++;
            }
        }

        if (checks > 0) {
            String[] strings = splitString(s, splitter, true, false);
            for (String s1 : strings) {
                ts.addAll(tokify(s1));
            }
        } else {
            ts.add(new Token("COD", s));
        }
        return ts;
    }

}