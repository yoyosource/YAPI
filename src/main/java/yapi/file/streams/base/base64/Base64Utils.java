// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.base64;

import yapi.math.base.BaseConversion;

public class Base64Utils {

    public static final String lookUpTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    public static final char padding = '=';

    public static char getChar(String s) {
        if (!s.matches("[01]+")) {
            throw new IllegalArgumentException();
        }
        if (s.length() != 6) {
            throw new IllegalArgumentException();
        }
        return lookUpTable.charAt(BaseConversion.fromBase2toInt(s));
    }

    public static String getString(char c) {
        if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '+' || c == '/')) {
            throw new IllegalArgumentException();
        }
        String t = BaseConversion.toBase2(lookUpTable.indexOf(c));
        return "0".repeat(6 - t.length()) + t;
    }

}