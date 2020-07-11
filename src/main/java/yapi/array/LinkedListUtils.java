// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.array;

import java.util.*;

public class LinkedListUtils {

    private LinkedListUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static LinkedList<Short> asList(short... input) {
        LinkedList<Short> output = new LinkedList<>();
        for (short var : input) output.add(var);
        return output;
    }

    public static LinkedList<Boolean> asList(boolean... input) {
        LinkedList<Boolean> output = new LinkedList<>();
        for (boolean var : input) output.add(var);
        return output;
    }

    public static LinkedList<Byte> asList(byte... input) {
        LinkedList<Byte> output = new LinkedList<>();
        for (byte var : input) output.add(var);
        return output;
    }

    public static LinkedList<Integer> asList(int... input) {
        LinkedList<Integer> output = new LinkedList<>();
        for (int var : input) output.add(var);
        return output;
    }

    public static LinkedList<Character> asList(char... input) {
        LinkedList<Character> output = new LinkedList<>();
        for (char var : input) output.add(var);
        return output;
    }

    public static LinkedList<Long> asList(long... input) {
        LinkedList<Long> output = new LinkedList<>();
        for (long var : input) output.add(var);
        return output;
    }

    public static short[] toShortArray(LinkedList<Short> input) {
        short[] output = new short[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static boolean[] toBooleanArray(LinkedList<Boolean> input) {
        boolean[] output = new boolean[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static byte[] toByteArray(LinkedList<Byte> input) {
        byte[] output = new byte[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static int[] toIntegerArray(LinkedList<Integer> input) {
        int[] output = new int[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static char[] toCharacterArray(LinkedList<Character> input) {
        char[] output = new char[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static long[] toLongArray(LinkedList<Long> input) {
        long[] output = new long[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

}