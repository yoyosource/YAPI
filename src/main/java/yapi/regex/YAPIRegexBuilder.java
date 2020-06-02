// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.regex;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.internal.runtimeexceptions.YAPIRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class YAPIRegexBuilder {

    private static char[] characters = "()+*?{}|[].^$".toCharArray();
    private static List<Character> escapeNeededCharacters = new ArrayList<>();

    static {
        for (char c : characters) escapeNeededCharacters.add(c);
    }

    public static void main(String[] args) {
        YAPIRegexBuilder yapiRegexBuilder = new YAPIRegexBuilder(".");
    }

    public YAPIRegexBuilder(String regexString) {
        char[] regex = regexString.toCharArray();
        if (regex.length > 0 && (regex[0] == '+' || regex[0] == '*' || regex[0] == '?')) {
            throw new YAPIRuntimeException("Regex Exception");
        }
        System.out.println(Arrays.toString(regex));
    }

    /*
    () {} []
    */

}