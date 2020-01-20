package yapi.math;

public class NumberRandom {

    private static long currentSeed = 0;
    private static NumberRandom numberRandom = new NumberRandom();

    public static NumberRandom getInstance() {
        currentSeed += numberRandom.getInt();
        return new NumberRandom(currentSeed);
    }

    private long seed = System.currentTimeMillis();
    private long cSeed = seed;
    private long multiplier = 8723465262572736L;

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
        long constant = 7346589735676528756L;
        long l = cSeed * multiplier + constant;
        multiplier += constant * l;
        cSeed = l;
        return l;
    }

    private long nextNumberAbsolute() {
        long l = nextNumber();
        return (l < 0 ? -l : l);
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
     * @sinve Version 1.1
     *
     * @return
     */
    public double getDoubleSigned() {
        return nextNumber() / (double)Long.MAX_VALUE;
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
     * @sinve Version 1.1
     *
     * @param max
     * @return
     */
    public double getDoubleSigned(double max) {
        return (nextNumber() / (double)Long.MAX_VALUE) % max;
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
     * @sinve Version 1.1
     *
     * @return
     */
    public int getIntSigned() {
        return (int)(nextNumber() / Integer.MAX_VALUE);
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
     * @sinve Version 1.1
     *
     * @param max
     * @return
     */
    public int getIntSigned(int max) {
        return (int)((nextNumber() / Integer.MAX_VALUE) % max);
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
     * @sinve Version 1.1
     *
     * @return
     */
    public long getLongSigned() {
        return nextNumber();
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
     * @sinve Version 1.1
     *
     * @param max
     * @return
     */
    public long getLongSigned(long max) {
        return nextNumber() % max;
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
     * @sinve Version 1.1
     *
     * @return
     */
    public float getFloatSigned() {
        return (float)nextNumber() / Float.MAX_VALUE;
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
     * @sinve Version 1.1
     *
     * @param max
     * @return
     */
    public float getFloatSigned(float max) {
        return (float)nextNumber() / Float.MAX_VALUE % max;
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
        if (length < 0) {
            return "";
        }
        StringBuilder st = new StringBuilder();
        for (int l = 0; l < length; l++) {
            st.append((char)((nextNumberAbsolute() % 94) + 32));
        }
        return st.toString();
    }

}
