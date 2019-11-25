package yapi.conjecture;

import yapi.datastructures.LongTree;

import java.util.ArrayList;
import java.util.List;

public class Collatz {

    /**
     *
     * @since Version 1.1
     *
     * @param i
     * @return
     */
    public static List<Long> compute(long i) {
        List<Long> longs = new ArrayList<>();
        if (i <= 0) {
            return longs;
        }
        while (i != 1) {
            longs.add(i);
            if (i % 2 == 0) {
                i = i / 2;
            } else {
                i = i * 3 + 1;
            }
        }
        longs.add(i);
        return longs;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param i
     * @return
     */
    public static List<Long> computeFast(long i) {
        List<Long> longs = new ArrayList<>();
        if (i <= 0) {
            return longs;
        }
        while (i != 1) {
            longs.add(i);
            if (i % 2 == 0) {
                i = i / 2;
            } else {
                i = i * 3 + 1;
                longs.add(i);
                i = i / 2;
            }
        }
        longs.add(i);
        return longs;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param i
     * @return
     */
    public static List<Byte> computeCompressed(long i) {
        List<Byte> bytes = new ArrayList<>();
        if (i <= 0) {
            return bytes;
        }
        StringBuilder st = new StringBuilder();
        long times = 1;
        while (i != 1) {
            if (st.length() == 8) {
                bytes.add(createByte(st));
                st = new StringBuilder();
            }
            if (i % 2 == 0) {
                i = i / 2;
                st.append('0');
            } else {
                i = i * 3 + 1;
                st.append('1');
            }
            times++;
        }
        if (st.length() > 0) {
            bytes.add(createByte(st));
            System.out.println(st);
        }
        System.out.println(times);
        return bytes;
    }

    private static byte createByte(StringBuilder st) {
        char[] chars = st.toString().toCharArray();
        int i = 0;
        int max = 1;
        for (char c : chars) {
            if (c == '1') {
                i += max;
            }
            max *= 2;
        }
        //System.out.println(st + " -> " + i);
        return (byte)i;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param depth
     * @return
     */
    public static LongTree reverseCompute(int depth) {
        LongTree longTree = new LongTree(1);
        if (depth > 0) {
            longTree.add(reverseCompute(depth, longTree));
        }
        return longTree;
    }

    private static LongTree[] reverseCompute(int depth, LongTree last) {
        LongTree[] longTrees = new LongTree[2];
        if (true) {
            LongTree longTree = new LongTree(last.getL() * 2);
            longTrees[0] = longTree;
            if (depth > 0) {
                last.add(reverseCompute(depth - 1, longTree));
            }
        }
        if ((last.getL() - 1) / 3 == (last.getL() - 1.0) / 3.0) {
            LongTree longTree = new LongTree((last.getL() - 1) / 3);
            longTrees[1] = longTree;
            if (depth > 0) {
                last.add(reverseCompute(depth - 1, longTree));
            }
        }
        return longTrees;
    }

}
