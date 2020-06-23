// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator;

import yapi.comparator.base.Base64Comparator;
import yapi.comparator.base.HexComparator;
import yapi.string.HashType;
import yapi.string.StringCrypting;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class StringComparators {

    private StringComparators() {
        throw new IllegalStateException("Utility class");
    }

    public static final Comparator<String> compareString = (o1, o2) -> {
        for (int i = 0; i < Math.min(o1.length(), o2.length()); i++) {
            int compare = Character.compare(o1.charAt(i), o2.charAt(i));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(o1.length(), o2.length());
    };

    public static final Comparator<String> compareStringUTF8 = (o1, o2) -> {
        byte[] bytes1 = o1.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = o2.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < Math.min(bytes1.length, bytes2.length); i++) {
            int compare = Byte.compare(bytes1[i], bytes2[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(bytes1.length, bytes2.length);
    };

    public static final Comparator<String> compareHexString = (o1, o2) -> {
        for (int i = 0; i < Math.min(o1.length(), o2.length()); i++) {
            int compare = HexComparator.compareHex.compare(o1.charAt(i), o2.charAt(i));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(o1.length(), o2.length());
    };

    public static final Comparator<String> compareBase64String = (o1, o2) -> {
        for (int i = 0 ; i < Math.min(o1.length(), o2.length()); i++) {
            int compare = Base64Comparator.compareBase.compare(o1.charAt(i), o2.charAt(i));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(o1.length(), o2.length());
    };

    public static final Comparator<String> compareStringReversed = (o1, o2) -> {
        for (int i = 0; i < Math.min(o1.length(), o2.length()); i++) {
            int x1 = o1.length() - i - 1;
            int x2 = o2.length() - i - 1;
            int compare = Character.compare(o1.charAt(x1), o2.charAt(x2));
            if (compare != 0) {
                return compare * -1;
            }
        }
        return Integer.compare(o2.length(), o1.length());
    };

    public static final Comparator<String> compareHashMD5 = (o1, o2) -> compareHexString.compare(StringCrypting.hash(o1, HashType.MD5, false), StringCrypting.hash(o2, HashType.MD5, false));

    public static final Comparator<String> compareHashSHA1 = (o1, o2) -> compareHexString.compare(StringCrypting.hash(o1, HashType.SHA1, false), StringCrypting.hash(o2, HashType.SHA1, false));

    public static final Comparator<String> compareHashSHA256 = (o1, o2) -> compareHexString.compare(StringCrypting.hash(o1, HashType.SHA256, false), StringCrypting.hash(o2, HashType.SHA256, false));

    public static final Comparator<String> compareHashSHA512 = (o1, o2) -> compareHexString.compare(StringCrypting.hash(o1, HashType.SHA512, false), StringCrypting.hash(o2, HashType.SHA512, false));

    public static final Comparator<String> compareCRC = (o1, o2) -> compareString.compare(StringCrypting.checksum(o1), StringCrypting.checksum(o2));

    public static final Comparator<String> compareCRCNumber = Comparator.comparingInt(o -> Integer.parseInt(StringCrypting.checksum(o), 16));

    public static final Comparator<String> compareLength = Comparator.comparingInt(String::length);

}