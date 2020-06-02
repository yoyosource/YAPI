// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.binary;

public class BinaryUtils {

    public static int fromBinary(String s) {
        return Integer.parseInt(s, 2);
    }

    public static String toBinary(int i) {
        int x = 256;
        StringBuilder st = new StringBuilder();
        while (i != 1) {
            if (i >= x) {
                st.append('1');
                i -= x;
            } else {
                st.append('0');
            }
            x /= 2;
        }
        return st.toString();
    }

}