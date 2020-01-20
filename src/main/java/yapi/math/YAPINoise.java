package yapi.math;

import yapi.quick.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YAPINoise {

    private long seed;
    private String generationPoints = "                 +   +    +   +                                                  +   +    +   +                                                                  +   +    +   +                                                  +   +    +   +                 ";
    private char[] generationPointsChars = generationPoints.toCharArray();
    private int[][] voxels = new int[0][0];

    public YAPINoise(long seed) {
        this.seed = seed;
        if (this.seed <= -1) {
            this.seed = NumberRandom.getInstance().getLong();
        }
        createVoxels();
    }

    public YAPINoise(long seed, String generationPoints) {
        this.seed = seed;
        if (this.seed <= -1) {
            this.seed = NumberRandom.getInstance().getLong();
        }
        generationPointsChars = generationPoints.toCharArray();
        createVoxels();
    }

    public static void main(String[] args) {
        if (true) {
            for (int i = 0; i < 100000; i++) {
                YAPINoise yapiNoise = new YAPINoise(0);
                yapiNoise.createChunk(0, 0);
            }
        }

        Timer timer = new Timer();
        YAPINoise yapiNoise = new YAPINoise(0);
        timer.start();
        double[][] doubles = yapiNoise.createChunk(0, 0);
        timer.stop();
        System.out.println(Arrays.deepToString(doubles));
        System.out.println((timer.getTime() / 100) / 10.0 + "µs");
    }

    private void createVoxels() {
        List<int[]> temp = new ArrayList<>();

        for (int i = 0; i < generationPointsChars.length; i++) {
            if (generationPointsChars[i] != ' ') {
                int xL = i / 16;
                int yL = i - (i / 16) * 16;
                temp.add(new int[]{xL, yL});
            }
        }

        voxels = new int[temp.size()][2];
        for (int i = 0; i < temp.size(); i++) {
            voxels[i] = temp.get(i);
        }
    }

    public double[][] createChunk(int x, int y) {
        double[][] left = generateChunk(x - 1, y);
        double[][] right = generateChunk(x + 1, y);
        double[][] down = generateChunk(x, y - 1);
        double[][] up = generateChunk(x, y + 1);
        double[][] current = generateChunk(x, y);
        normalize(current, up, down, left, right);
        return current;
    }

    private double[][] generateChunk(int x, int y) {
        double[][] doubles = createEmptyChunk();

        NumberRandom numberRandom = new NumberRandom(generateSeed(x, y));

        for (int i = 0; i < voxels.length; i++) {
            doubles[voxels[i][0]][voxels[i][1]] = numberRandom.getDouble(1);
        }

        return normalizeIntern(doubles);
    }

    private double[][] createEmptyChunk() {
        double[][] doubles = new double[16][16];
        for (int i = 0; i < doubles.length; i++) {
            Arrays.fill(doubles[i], -1);
        }
        return doubles;
    }

    private long generateSeed(int x, int y) {
        long s = seed;
        if (y % 100 != 0) {
            s += (x * 16 * seed) / y % 100;
        }
        if (x % 100 != 0) {
            s += (y * 16 * seed) / x % 100;
        }
        s += x * y;
        if (seed % 100 != 0) {
            s += x * y * 256 / (seed % 100);
        }
        s += x + y;
        return s;
    }

    private double[][] normalizeIntern(double[][] doubles) {
        int lastY;
        for (int x = 1; x < doubles.length - 1; x++) {
            if (doubles[x][1] == -1) {
                continue;
            }
            lastY = 1;
            int fY = 1;
            for (int y = 1; y < doubles.length - 1; y++) {
                if (doubles[x][y] != -1 && fY != y) {
                    normalizeIntern(doubles, x, lastY, x, y);
                    lastY = y;
                }
            }
        }
        int lastX;
        for (int y = 1; y < doubles.length - 1; y++) {
            if (doubles[1][y] == -1) {
                continue;
            }
            lastX = 1;
            int fX = 1;
            for (int x = 1; x < doubles.length - 1; x++) {
                if (doubles[x][y] != -1 && fX != x) {
                    normalizeIntern(doubles, lastX, y, x, y);
                    lastX = x;
                }
            }
        }
        return doubles;
    }

    private void normalizeIntern(double[][] doubles, int x1, int y1, int x2, int y2) {
        double d1 = doubles[x1][y1];
        double d2 = doubles[x2][y2];

        double step = (d2 - d1) / distance(x1, y1, x2, y2);

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                doubles[x][y] = d1 + step * distance(x1, y1, x, y);
            }
        }
    }

    private void normalize(double[][] current, double[][] up, double[][] down, double[][] left, double[][] right) {

    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

}
