// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.os;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OSCheck {

    private static OSType type = initType();
    private static String otherType = "";

    private static List<OSChecker> checkers = new ArrayList<>();

    public static void addOSType(OSChecker checker) {
        checkers.add(checker);
    }

    public static void reCheck() {
        type = initType();
    }

    private static OSType initType() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.contains("mac") || os.contains("darwin")) {
            return OSType.MAC_OS;
        } else if (os.contains("win")) {
            return OSType.WINDOWS;
        } else if (os.contains("nux")) {
            return OSType.LINUX;
        }

        for (OSChecker osChecker : checkers) {
            String s = osChecker.check(os);
            if (s == null || s.trim().isEmpty()) {
                continue;
            }
            otherType = s.trim().toLowerCase();
            return OSType.OTHER;
        }
        otherType = "";
        return OSType.OTHER;
    }

    public static OSType getType() {
        return type;
    }

    public static String getOtherType() {
        return otherType;
    }
}