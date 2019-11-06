package yapi.math;

import java.math.BigDecimal;

public class NumberRandom {

    private long seed = System.currentTimeMillis();
    private long cSeed = seed;
    private long multiplier = 8723465262572736L;
    private long constant = 7346589735676528756L;

    public NumberRandom() {

    }

    public NumberRandom(long seed) {
        this.seed = seed;
        cSeed = seed;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public long getSeed() {
        return seed;
    }

    private long nextNumber() {
        long l = cSeed * multiplier + constant;
        multiplier += constant * l;
        cSeed = l;
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public double getDouble() {
        double d = nextNumber() / (double)Long.MAX_VALUE;
        return d < 0 ? -d : d;
    }

    /**
     *
     * @since Version 1
     *
     * @param max
     * @return
     */
    public double getDouble(double max) {
        double d = nextNumber() / (double)Long.MAX_VALUE;
        d = d % max;
        return d < 0 ? -d : d;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public int getInt() {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        return i < 0 ? -i : i;
    }

    /**
     *
     * @since Version 1
     *
     * @param max
     * @return
     */
    public int getInt(int max) {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        i = i % max;
        return i < 0 ? -i : i;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public long getLong() {
        long l = nextNumber();
        return l < 0 ? -l : l;
    }

    /**
     *
     * @since Version 1
     *
     * @param max
     * @return
     */
    public long getLong(long max) {
        long l = nextNumber();
        l = l % max;
        return l < 0 ? -l : l;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public float getFloat() {
        float f = nextNumber() / Float.MAX_VALUE;
        return f < 0 ? -f : f;
    }

    /**
     *
     * @since Version 1
     *
     * @param max
     * @return
     */
    public float getFloat(float max) {
        float f = nextNumber() / Float.MAX_VALUE;
        f = f % max;
        return f < 0 ? -f : f;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public char getChar() {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        i = i % 65536;
        return (char)(i < 0 ? -i : i);
    }

    /**
     *
     * @since Version 1
     *
     * @param max
     * @return
     */
    public char getChar(char max) {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        i = i % (int)max;
        return (char)(i < 0 ? -i : i);
    }

    /**
     *
     * @since Version 1
     *
     * @param length
     * @return
     */
    public String getString(int length) {
        StringBuilder st = new StringBuilder();
        for (int l = 0; l < length; l++) {
            int i = (int) (nextNumber() / Integer.MAX_VALUE);
            i = (i % 94) + 32;
            st.append((char)i);
        }
        return st.toString();
    }

}
