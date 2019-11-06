package yapi.math;

public class NumberRandom {

    private long seed = System.currentTimeMillis();
    private long number = seed;
    private long number1 = 8723465262572736L;
    private long number2 = 7346589735676528756L;

    public NumberRandom() {

    }

    public NumberRandom(long seed) {
        this.seed = seed;
        number = seed;
    }

    public long getSeed() {
        return seed;
    }

    private long nextNumber() {
        long l = number * number1 + number2;
        number1 += number2 * l;
        number = l;
        return l;
    }

    public double getDouble() {
        double d = nextNumber() / (double)Long.MAX_VALUE;
        return d < 0 ? -d : d;
    }

    public int getInt() {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        return i < 0 ? -i : i;
    }

    public int getInt(int max) {
        int i = (int)(nextNumber() / Integer.MAX_VALUE);
        i = i % max;
        return i < 0 ? -i : i;
    }

    public long getLong() {
        long l = nextNumber();
        return l < 0 ? -l : l;
    }

    public long getLong(long max) {
        long l = nextNumber();
        l = l % max;
        return l < 0 ? -l : l;
    }

    public float getFloat() {
        float f = nextNumber() / Float.MAX_VALUE;
        return f < 0 ? -f : f;
    }

    public float getFloat(float max) {
        float f = nextNumber() / Float.MAX_VALUE;
        f = f % max;
        return f < 0 ? -f : f;
    }

}
