// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.base;

import java.util.Comparator;

public class BASE64Comparator {

    private static char validBase(char c) {

        return c;
    }

    public static final Comparator<Character> compareBase = (o1, o2) -> {
        return 0;
    };

}