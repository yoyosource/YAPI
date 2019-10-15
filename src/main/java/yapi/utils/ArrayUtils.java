package yapi.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> void fillWith(T[] ts, T t) {
        for (int i = 0; i < ts.length; i++) {
            ts[i] = t;
        }
    }

    public static void fillWith(int[] ints, int j) {
        for (int i = 0; i < ints.length; i++) {
            ints[i] = j;
        }
    }

    public static void fillWith(double[] doubles, double j) {
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = j;
        }
    }

    public static void fillWith(long[] longs, long j) {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = j;
        }
    }

    public static void fillWith(char[] chars, char j) {
        for (int i = 0; i < chars.length; i++) {
            chars[i] = j;
        }
    }

    public static void fillWith(String[] strings, String j) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = j;
        }
    }

    public static void fillWith(byte[] bytes, byte j) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = j;
        }
    }

    public static void fillWith(boolean[] booleans, boolean j) {
        for (int i = 0; i < booleans.length; i++) {
            booleans[i] = j;
        }
    }

    public static void fillWith(short[] shorts, short j) {
        for (int i = 0; i < shorts.length; i++) {
            shorts[i] = j;
        }
    }

    public static <T> boolean contains(T[] ts, T t) {
        for (int i = 0; i < ts.length; i++) {
            if (ts[i].equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> int indexOf(T[] ts, T t) {
        for (int i = 0; i < ts.length; i++) {
            if (ts[i].equals(t)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(T[] ts, T t) {
        for (int i = ts.length - 1; i >= 0; i--) {
            if (ts[i].equals(t)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(int[] ints, int j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(int[] ints, int j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(int[] ints, int j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(double[] ints, double j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(double[] ints, double j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] ints, double j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(long[] ints, long j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(long[] ints, long j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(long[] ints, long j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(char[] ints, char j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(char[] ints, char j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(char[] ints, char j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(String[] ints, String j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i].equals(j)) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(String[] ints, String j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i].equals(j)) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(String[] ints, String j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i].equals(j)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(byte[] ints, byte j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(byte[] ints, byte j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(byte[] ints, byte j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(boolean[] ints, boolean j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(boolean[] ints, boolean j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] ints, boolean j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(short[] ints, short j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(short[] ints, short j) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(short[] ints, short j) {
        for (int i = ints.length - 1; i >= 0; i--) {
            if (ints[i] == j) {
                return i;
            }
        }
        return -1;
    }

}
