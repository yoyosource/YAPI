// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.array;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * For more ArrayUtilities use http://commons.apache.org/proper/commons-lang/
 */
public class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Short> asList(short... input) {
        List<Short> output = new ArrayList<>();
        for (short var : input) output.add(var);
        return output;
    }

    public static List<Boolean> asList(boolean... input) {
        List<Boolean> output = new ArrayList<>();
        for (boolean var : input) output.add(var);
        return output;
    }

    public static List<Byte> asList(byte... input) {
        List<Byte> output = new ArrayList<>();
        for (byte var : input) output.add(var);
        return output;
    }

    public static List<Integer> asList(int... input) {
        List<Integer> output = new ArrayList<>();
        for (int var : input) output.add(var);
        return output;
    }

    public static List<Character> asList(char... input) {
        List<Character> output = new ArrayList<>();
        for (char var : input) output.add(var);
        return output;
    }

    public static List<Long> asList(long... input) {
        List<Long> output = new ArrayList<>();
        for (long var : input) output.add(var);
        return output;
    }

    public static List<Double> asList(double... input) {
        List<Double> output = new ArrayList<>();
        for (double var : input) output.add(var);
        return output;
    }

    public static List<Float> asList(float... input) {
        List<Float> output = new ArrayList<>();
        for (float var : input) output.add(var);
        return output;
    }

    public static <T> List<T> asList(T... input) {
        List<T> output = new ArrayList<>();
        for (T var : input) output.add(var);
        return output;
    }

    public static short[] toShortArray(List<Short> input) {
        short[] output = new short[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static boolean[] toBooleanArray(List<Boolean> input) {
        boolean[] output = new boolean[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static byte[] toByteArray(List<Byte> input) {
        byte[] output = new byte[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static int[] toIntegerArray(List<Integer> input) {
        int[] output = new int[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static char[] toCharacterArray(List<Character> input) {
        char[] output = new char[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static long[] toLongArray(List<Long> input) {
        long[] output = new long[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static double[] toDoubleArray(List<Double> input) {
        double[] output = new double[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static float[] toFloatArray(List<Float> input) {
        float[] output = new float[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static <T> T[] toGenericArray(List<T> input) {
        T[] output = (T[])new Object[input.size()];
        for (int i = 0; i < input.size(); i++) output[i] = input.get(i);
        return output;
    }

    public static Short[] asArray(short... input) {
        return asList(input).toArray(new Short[0]);
    }

    public static Boolean[] asArray(boolean... input) {
        return asList(input).toArray(new Boolean[0]);
    }

    public static Byte[] asArray(byte... input) {
        return asList(input).toArray(new Byte[0]);
    }

    public static Integer[] asArray(int... input) {
        return asList(input).toArray(new Integer[0]);
    }

    public static Character[] asArray(char... input) {
        return asList(input).toArray(new Character[0]);
    }

    public static Long[] asArray(long... input) {
        return asList(input).toArray(new Long[0]);
    }

    public static Double[] asArray(double... input) {
        return asList(input).toArray(new Double[0]);
    }

    public static Float[] asArray(float... input) {
        return asList(input).toArray(new Float[0]);
    }

    public static <T> T[] asArray(T... input) {
        return asList(input).toArray((T[])new Object[0]);
    }

    public static Stream<Short> toStream(short... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Boolean> toStream(boolean... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Byte> toStream(byte... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Integer> toStream(int... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Character> toStream(char... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Long> toStream(long... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Double> toStream(double... input) {
        return Stream.of(asArray(input));
    }

    public static Stream<Float> toStream(float... input) {
        return Stream.of(asArray(input));
    }

    public static <T> Stream<T> toStream(T... input) {
        return Stream.of(asArray(input));
    }

    // System.arraycopy(toCopy,0, copyInto, index, toCopy.length);

    public static short[] copyInto(short[] toCopy, short[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static short[] copyInto(Short[] toCopy, short[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Short[] copyInto(short[] toCopy, Short[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Short[] copyInto(Short[] toCopy, Short[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static boolean[] copyInto(boolean[] toCopy, boolean[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static boolean[] copyInto(Boolean[] toCopy, boolean[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Boolean[] copyInto(boolean[] toCopy, Boolean[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Boolean[] copyInto(Boolean[] toCopy, Boolean[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static byte[] copyInto(byte[] toCopy, byte[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static byte[] copyInto(Byte[] toCopy, byte[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Byte[] copyInto(byte[] toCopy, Byte[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Byte[] copyInto(Byte[] toCopy, Byte[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static int[] copyInto(int[] toCopy, int[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static int[] copyInto(Integer[] toCopy, int[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Integer[] copyInto(int[] toCopy, Integer[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Integer[] copyInto(Integer[] toCopy, Integer[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static char[] copyInto(char[] toCopy, char[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static char[] copyInto(Character[] toCopy, char[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Character[] copyInto(char[] toCopy, Character[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Character[] copyInto(Character[] toCopy, Character[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static long[] copyInto(long[] toCopy, long[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static long[] copyInto(Long[] toCopy, long[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Long[] copyInto(long[] toCopy, Long[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Long[] copyInto(Long[] toCopy, Long[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static double[] copyInto(double[] toCopy, double[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static double[] copyInto(Double[] toCopy, double[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Double[] copyInto(double[] toCopy, Double[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Double[] copyInto(Double[] toCopy, Double[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static float[] copyInto(float[] toCopy, float[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static float[] copyInto(Float[] toCopy, float[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Float[] copyInto(float[] toCopy, Float[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static Float[] copyInto(Float[] toCopy, Float[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static <T> T[] copyInto(T[] toCopy, T[] copyInto, int index) {
        for (int i = 0; i < toCopy.length; i++) copyInto[i + index] = toCopy[i];
        return copyInto;
    }

    public static int size(Short[]... input) {
        int size = 0;
        for (Short[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Boolean[]... input) {
        int size = 0;
        for (Boolean[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Byte[]... input) {
        int size = 0;
        for (Byte[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Integer[]... input) {
        int size = 0;
        for (Integer[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Character[]... input) {
        int size = 0;
        for (Character[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Long[]... input) {
        int size = 0;
        for (Long[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Double[]... input) {
        int size = 0;
        for (Double[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static int size(Float[]... input) {
        int size = 0;
        for (Float[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static <T> int size(T[]... input) {
        int size = 0;
        for (T[] array : input) {
            size += array.length;
        }
        return size;
    }

    public static short[] flatten(short[]... input) {
        short[] output = new short[size(input)];
        int index = 0;
        for (short[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Short[] flatten(Short[]... input) {
        Short[] output = new Short[size(input)];
        int index = 0;
        for (Short[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static boolean[] flatten(boolean[]... input) {
        boolean[] output = new boolean[size(input)];
        int index = 0;
        for (boolean[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Boolean[] flatten(Boolean[]... input) {
        Boolean[] output = new Boolean[size(input)];
        int index = 0;
        for (Boolean[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static byte[] flatten(byte[]... input) {
        byte[] output = new byte[size(input)];
        int index = 0;
        for (byte[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Byte[] flatten(Byte[]... input) {
        Byte[] output = new Byte[size(input)];
        int index = 0;
        for (Byte[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static int[] flatten(int[]... input) {
        int[] output = new int[size(input)];
        int index = 0;
        for (int[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Integer[] flatten(Integer[]... input) {
        Integer[] output = new Integer[size(input)];
        int index = 0;
        for (Integer[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static char[] flatten(char[]... input) {
        char[] output = new char[size(input)];
        int index = 0;
        for (char[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Character[] flatten(Character[]... input) {
        Character[] output = new Character[size(input)];
        int index = 0;
        for (Character[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static long[] flatten(long[]... input) {
        long[] output = new long[size(input)];
        int index = 0;
        for (long[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Long[] flatten(Long[]... input) {
        Long[] output = new Long[size(input)];
        int index = 0;
        for (Long[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static double[] flatten(double[]... input) {
        double[] output = new double[size(input)];
        int index = 0;
        for (double[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Double[] flatten(Double[]... input) {
        Double[] output = new Double[size(input)];
        int index = 0;
        for (Double[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static float[] flatten(float[]... input) {
        float[] output = new float[size(input)];
        int index = 0;
        for (float[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static Float[] flatten(Float[]... input) {
        Float[] output = new Float[size(input)];
        int index = 0;
        for (Float[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

    public static <T> T[] flatten(T[]... input) {
        T[] output = (T[])new Object[size(input)];
        int index = 0;
        for (T[] array : input) {
            copyInto(array, output, index);
            index += array.length;
        }
        return output;
    }

}