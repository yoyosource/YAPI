// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.string;

import yapi.datastructures.circular.CircularBuffer;

public class StringCompression {

    public static void main(String[] args) {
        compress("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    }

    public static void compress(String s) {
        CircularBuffer<Character> characterCircularBuffer = new CircularBuffer<>(65536);
        StringBuilder st = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            characterCircularBuffer.add(c);

            int index = characterCircularBuffer.indexOf(st);
            if (index < 0) {
                if (st.length() > 3) {
                    st.deleteCharAt(st.length() - 1);
                    index = characterCircularBuffer.indexOf(st);
                    System.out.println(index + ":" + st.toString() + ":");
                }
                st = new StringBuilder();
                st.append(c);
            } else {
                st.append(c);
            }
        }
    }

}