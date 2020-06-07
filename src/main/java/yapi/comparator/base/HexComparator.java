// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.base;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HexComparator {

    private HexComparator() {
        throw new IllegalStateException("Utility class");
    }

    private static char validHex(char c) {
        if (c >= '0' && c <= '9') return c;
        if (c >= 'A' && c <= 'F') return c;
        if (c >= 'a' && c <= 'f') return c;
        throw new IllegalArgumentException("Input was not a HEX character");
    }

    private static Map<Character, Integer> weightMap = new HashMap<>();

    static {
        weightMap.put('0', 0);
        weightMap.put('1', 1);
        weightMap.put('2', 2);
        weightMap.put('3', 3);
        weightMap.put('4', 4);
        weightMap.put('5', 5);
        weightMap.put('6', 6);
        weightMap.put('7', 7);
        weightMap.put('8', 8);
        weightMap.put('9', 9);

        weightMap.put('A', 10);
        weightMap.put('B', 11);
        weightMap.put('C', 12);
        weightMap.put('D', 13);
        weightMap.put('E', 14);
        weightMap.put('F', 15);

        weightMap.put('a', 10);
        weightMap.put('b', 11);
        weightMap.put('c', 12);
        weightMap.put('d', 13);
        weightMap.put('e', 14);
        weightMap.put('f', 15);
    }

    public static final Comparator<Character> compareHex = Comparator.comparingInt(o -> weightMap.get(validHex(o)));

}