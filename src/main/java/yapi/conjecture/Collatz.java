package yapi.conjecture;

import java.util.ArrayList;
import java.util.List;

public class Collatz {

    private Collatz() {
        throw new IllegalStateException();
    }

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
    public static List<Integer> reverseCompute(int depth) {
        List<Integer> integers = new ArrayList<>();
        List<Integer> nextLayer = new ArrayList<>();
        nextLayer.add(1);
        for (int i = 0; i < depth; i++) {
            List<Integer> currentLayer = new ArrayList<>();
            currentLayer.addAll(nextLayer);
            nextLayer.clear();
            while (!currentLayer.isEmpty()) {
                int num = currentLayer.remove(0);
                if (!integers.contains(num)) {
                    integers.add(num);
                }
                nextLayer.add(num * 2);
                double t = (num - 1) / 3.0;
                if (t > 0 && (int)t == t) {
                    nextLayer.add((int)t);
                }
            }
        }
        return integers;
    }

}
