// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.array;

import java.util.*;

public class LinkedListUtils {

    private LinkedListUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Short> asList(short... input) {
        List<Short> output = new LinkedList<>();
        for (short var : input) output.add(var);
        return output;
    }

    public static List<Boolean> asList(boolean... input) {
        List<Boolean> output = new LinkedList<>();
        for (boolean var : input) output.add(var);
        return output;
    }

    public static List<Byte> asList(byte... input) {
        List<Byte> output = new LinkedList<>();
        for (byte var : input) output.add(var);
        return output;
    }

    public static List<Integer> asList(int... input) {
        List<Integer> output = new LinkedList<>();
        for (int var : input) output.add(var);
        return output;
    }

    public static List<Character> asList(char... input) {
        List<Character> output = new LinkedList<>();
        for (char var : input) output.add(var);
        return output;
    }

    public static List<Long> asList(long... input) {
        List<Long> output = new LinkedList<>();
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