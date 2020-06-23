// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.base;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Base64Comparator {

    private Base64Comparator() {
        throw new IllegalStateException("Utility class");
    }

    private static char validBase(char c) {
        if (c >= 'A' && c <= 'Z') return c;
        if (c >= 'a' && c <= 'z') return c;
        if (c >= '0' && c <= '9') return c;
        if (c == '+') return c;
        if (c == '/') return c;
        throw new IllegalArgumentException("Input was not a HEX character");
    }

    private static Map<Character, Integer> weightMap = new HashMap<>();

    static {
        String weight = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        for (int i = 0; i < weight.length(); i++) {
            weightMap.put(weight.charAt(i), i);
        }
    }

    public static final Comparator<Character> compareBase = Comparator.comparingInt(o -> weightMap.get(validBase(o)));

}