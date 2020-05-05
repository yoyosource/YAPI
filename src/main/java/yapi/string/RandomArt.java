// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.internal.exceptions.StringException;
import yapi.math.NumberRandom;

public class RandomArt {

    public static void main(String[] args) {
        // 26  -> ::0"&§FE0$"80$&B0$"60$20%)
        // 103 -> ::0$=10$10§(10$10$10§$10$10$10$10§"1010$10$10%F§0"(20(10&10"=1010$10$1010$10"/FE010&10$1020"=10""1010")

        RandomArt r1 = new RandomArt(" Token ", NumberRandom.getInstance().getString(8).getBytes());
        RandomArt r2 = new RandomArt(" Key ", NumberRandom.getInstance().getString(8).getBytes());
        System.out.println(r1 + r1.toCompactString());
        System.out.println(r2 + r2.toCompactString());
    }

    private int xDim = 17;
    private int yDim = 9;

    private static final byte start = -1;
    private static final byte end = -2;

    private static boolean allowSizeChange= false;

    public static void setAllowSizeChange(boolean allowSizeChange) {
        RandomArt.allowSizeChange = allowSizeChange;
    }

    public static RandomArt getInstance(String s) {
        if (!s.contains("\n")) {
            throw new StringException();
        }
        String[] strings = s.split("\n");
        if (!valid(strings)) {
            throw new StringException();
        }

        byte[][] bytes = new byte[strings.length - 2][strings[0].length() - 2];
        for (int i = 1; i < strings.length - 1; i++) {
            String t = strings[i];
            for (int j = 1; j < t.length() - 1; j++) {
                bytes[i - 1][j - 1] = getByte(t.charAt(j));
                if (bytes[i - 1][j - 1] == -3) {
                    throw new StringException();
                }
            }
        }

        return new RandomArt(bytes, strings[0].length() - 2, strings.length - 2, "", "");
    }

    private static byte getByte(char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return (byte)i;
            }
        }
        if (c == 'E') {
            return end;
        }
        if (c == 'S') {
            return start;
        }
        return -3;
    }

    private static boolean valid(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (i == 0) {
                if (!strings[i].matches("\\+-+(\\[.+?\\]-+)?\\+")) {
                    return false;
                }
                continue;
            } else if (i == strings.length - 1) {
                if (!strings[i].matches("\\+-+(\\[.+?\\]-+)?\\+")) {
                    return false;
                }
            } else {
                if (!strings[i].matches("\\|.+?\\|")) {
                    return false;
                }
            }
            if (strings[i].length() % 2 != 1) {
                return false;
            }
            if (strings[i - 1].length() != strings[i].length()) {
                return false;
            }
        }
        return true;
    }

    private static final char[] chars = new char[]{
            ' ', '.', 'o', '+', '=',
            '*', 'B', 'O', 'X', '@',
            '%', '&', '#', '/', '^',
    };

    private byte[][] board = new byte[yDim][xDim];

    private String title = "";
    private String subtitle = "";

    public RandomArt(String title, int... bytes) {
        byte[] bts = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bts[i] = (byte)bytes[i];
        }

        this.title = title;

        generate(bts);
    }

    public RandomArt(String title, byte... bytes) {
        this.title = title;

        generate(bytes);
    }

    public RandomArt(String title, String subtitle, byte... bytes) {
        this.title = title;
        this.subtitle = subtitle;

        generate(bytes);
    }

    public RandomArt(String title, String subtitle, int... bytes) {
        byte[] bts = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bts[i] = (byte)bytes[i];
        }

        this.title = title;
        this.subtitle = subtitle;

        generate(bts);
    }

    private RandomArt(byte[][] bytes, int xDim, int yDim, String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.xDim = xDim;
        this.yDim = yDim;
        this.board = bytes;
    }

    private void generate(byte[] bytes) {
        if (allowSizeChange) {
            double t = Math.log(bytes.length) / Math.log(Math.E) - 5;
            if (t < 0) {
                t = 0;
            }
            xDim += (int) t * 2;
            yDim += (int) t;
            board = new byte[yDim][xDim];
        }
        int x = xDim / 2;
        int y = yDim / 2;

        board[y][x] = start;
        StringBuilder st = new StringBuilder();

        for (byte b : bytes) {
            for (int i = 0; i < yDim - 1; i += 2) {
                byte d = (byte)((b >> i) & 3);
                switch (d) {
                    case 0:
                    case 1:
                        if (y > 0) y--;
                        st.append("-");
                        break;
                    case 2:
                    case 3:
                        if (y < yDim - 1) y++;
                        st.append("+");
                        break;
                }
                switch (d) {
                    case 0:
                    case 2:
                        if (x > 0) x--;
                        st.append("-");
                        break;
                    case 1:
                    case 3:
                        if (x < xDim - 1) x++;
                        st.append("+");
                        break;
                }
                st.append(" ");
                if (board[y][x] >= 0) {
                    board[y][x]++;
                }
            }
        }
        String s = st.toString();
        s = s.replace("++ ", "→").replace("-- ", "←");
        s = s.replace("+- ", "↑").replace("-+ ", "↓");
        s = s.replace("←→", "↔").replace("↑↓", "↕").replace("←←", "«").replace("→→", "»");
        //System.out.println(s);

        if (board[yDim / 2][xDim / 2] == 0) {
            board[yDim / 2][xDim / 2] = start;
        }
        board[y][x] = end;
    }


    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        if (title.length() > xDim - 2) {
            writeTitle(st, title.substring(0, xDim - 2));
        } else {
            writeTitle(st, title);
        }

        for (byte[] bytes : board) {
            st.append("|");
            for (byte b : bytes) {
                int i = ((b < 0) ? b + 256 : b) % 256;
                if (b == start) {
                    st.append("S");
                } else if (b == end) {
                    st.append("E");
                } else if (i < chars.length) {
                    st.append(chars[b]);
                } else {
                    st.append(chars[chars.length - 1]);
                }
            }
            st.append("|\n");
        }

        if (subtitle.length() > xDim - 2) {
            writeTitle(st, subtitle.substring(0, xDim - 2));
        } else {
            writeTitle(st, subtitle);
        }
        return st.toString();
    }

    private void writeTitle(StringBuilder st, String title) {
        if (!title.isBlank()) {
            int extaChars = title.length() + 2 - xDim;
            if (extaChars > 0) {
                title = title.substring(0, xDim - extaChars);
            }
            title = "[" + title + "]";
        }

        int leftLen = (xDim - title.length()) / 2;
        int rightLen = xDim - title.length() - leftLen;
        st.append("+").append("-".repeat(leftLen)).append(title).append("-".repeat(rightLen)).append("+\n");
    }

    public String toCompactString() {
        byte[] bytes = new byte[xDim * yDim];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                bytes[i * board[i].length + j] = board[i][j];
            }
        }
        String s = StringFormatting.toHex(bytes, false);
        s = s.replace("0".repeat(xDim), "#").replace("0000000000000000", ";").replace("00000000", ":").replace("0000", ",").replace("00", ".");
        StringBuilder st = new StringBuilder();
        st.append(title).append("|").append(subtitle).append("|").append(replaceNumber(yDim - 9 + ""));
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                length = 1;
            } else {
                if (s.charAt(i - 1) == s.charAt(i)) {
                    length++;
                } else {
                    if (length <= (length + "").length() + 1) {
                        st.append((s.charAt(i - 1) + "").repeat(length));
                    } else {
                        st.append(s.charAt(i - 1)).append(replaceNumber(length - 1 + ""));
                    }
                    length = 1;
                }
            }
        }
        if (length <= (length + "").length() + 1) {
            st.append((s.charAt(s.length() - 1) + "").repeat(length));
        } else {
            st.append(s.charAt(s.length() - 1)).append(replaceNumber(length - 1 + ""));
        }
        return st.toString();
    }

    public String _toCompactString() {
        byte[] bytes = new byte[xDim * yDim];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                bytes[i * board[i].length + j] = board[i][j];
            }
        }
        return StringFormatting.toHex(bytes);
    }

    // 0123456789
    // GHIJKLMNOP

    private String replaceNumber(String s) {
        String s1 = "0123456789";
        String s2 = "!\"§$%&/()=";
        for (int i = 0; i < s1.length(); i++) {
            s = s.replace(s1.charAt(i), s2.charAt(i));
        }
        return s;
    }

}