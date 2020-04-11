// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.base;

import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatConversionException;

public enum BaseConversionType {

    BASE_64_SYMMETRIC ("0123456789abcdefghijklABCDEFGHIJKLmnopqrstuvwxyzMNOPQRSTUVWXYZ+/", "64S"),
    BASE_64_MIRRORED  ("0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz+/", "64M"),
    BASE_64_NORMAL    ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/", "64N"),

    BASE_32_SYMMETRIC ("0123456789abcdefghijkABCDEFGHIJK", "32S"),
    BASE_32_MIRRORED  ("0123456789AaBbCcDdEeFfGgHhIiJjKk", "32M"),
    BASE_32_LOWERCASE ("0123456789abcdefghijklmnopqrstuv", "32L"),
    BASE_32_UPPERCASE ("0123456789ABCDEFGHIJKLMNOPQRSTUV", "32U"),
    BASE_32_SPECIAL   ("0123456789!\"§$%&/()=?¬˙#£ﬁ^\\˜·¯@", "32P"),

    BASE_16_LOWERCASE ("01234567890abcdef", "32L"),
    BASE_16_UPPERCASE ("01234567890ABCDEF", "16U"),

    BASE_8_NUMBER     ("01234567", "8N"),
    BASE_8_LOWERCASE  ("abcdefgh", "8L"),
    BASE_8_UPPERCASE  ("ABCDEFGH", "8U"),
    BASE_8_SPECIAL    ("+/=#*()&", "8S");

    private String base;
    private String shortName;

    BaseConversionType(String s, String shortName) {
        this.base = s;
        this.shortName = shortName;
    }

    char[] getCharArray() {
        return base.toCharArray();
    }

    Map<Character, Integer> getIndexArray() {
        Map<Character, Integer> integerMap = new HashMap<>();
        for (int i = 0; i < base.length(); i++) {
            integerMap.put(base.charAt(i), i);
        }
        return integerMap;
    }

    public static BaseConversionType getBaseConversionType(String name) {
        BaseConversionType[] baseConversionTypes = values();
        for (int i = 0; i < baseConversionTypes.length; i++) {
            if (baseConversionTypes[i].shortName.equals(name)) {
                return baseConversionTypes[i];
            }
        }
        throw new UnknownFormatConversionException("Unknown Type");
    }

}