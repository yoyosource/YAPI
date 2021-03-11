package yapi.compression.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImageDecompression {

    private final BufferedImage bufferedImage;

    private final int type;
    private final int numBands;
    private final int height;
    private final int width;
    private final CoordinateManager coordinateManager;

    private final Random random = new Random(0);

    private final List<Long> colorReferenceList = new ArrayList<>();

    private final boolean ignoreRepetition;
    private final boolean derivate;

    public ImageDecompression(byte[] bytes) {
        this(bytes, false);
    }

    public ImageDecompression(byte[] bytes, boolean ignoreRepetition) {
        this(bytes, false, true);
    }

    public ImageDecompression(byte[] bytes, boolean ignoreRepetition, boolean derivate) {
        type = bytes[0] & 0xFF;
        numBands = bytes[1] & 0xFF;
        int[] height = variableBytesToInt(bytes, 2);
        int[] width = variableBytesToInt(bytes, height[0]);
        this.height = height[1];
        this.width = width[1];

        int index = width[0];
        coordinateManager = new CoordinateManager(bytes[index++] & 0xFF, this.width, this.height);

        int referenceColorCount = bytes[index++] & 0xFF;
        while (referenceColorCount > 0) {
            int[] ints = new int[numBands];
            for (int i = 0; i < numBands; i++) {
                ints[i] = bytes[index + i] & 0xFF;
            }
            colorReferenceList.add(intsToLong(ints));
            index += numBands;
            referenceColorCount--;
        }

        bufferedImage = new BufferedImage(this.width, this.height, type);
        this.ignoreRepetition = ignoreRepetition;
        this.derivate = derivate;
        readImage(bytes, index);
    }

    private void readImage(byte[] bytes, int index) {
        int[] pixel = new int[numBands];

        for (int i = index; i < bytes.length; i++) {
            int bi = bytes[i] & 0xFF;
            if (bi == 0) {
                pixel = new int[numBands];
                int indicator = bytes[i + 1] & 0xFF;
                if (indicator < 250) {
                    pixel = longToInts(colorReferenceList.get(bytes[i + 1] & 0xFF));
                    setPixel(pixel, false, false);
                    i += 1;
                    continue;
                }
                switch (indicator) {
                    case 251:
                        for (int count = 0; count < numBands; count++) {
                            pixel[count] = bytes[i + 2 + count] & 0xFF;
                        }
                        i += numBands + 1;
                        break;
                    case 252:
                        for (int count = 0; count < 3; count++) {
                            pixel[count] = bytes[i + 2 + count] & 0xFF;
                        }
                        if (numBands == 4) {
                            pixel[3] = 0xFF;
                        }
                        i += 3 + 1;
                        break;
                    case 253:
                        pixel[0] = bytes[i + 2];
                        pixel[1] = bytes[i + 2];
                        pixel[2] = bytes[i + 2];
                        if (numBands == 4) {
                            pixel[3] = bytes[i + 3];
                        }
                        i += 3;
                        break;
                    case 254:
                        pixel[0] = bytes[i + 2];
                        pixel[1] = bytes[i + 2];
                        pixel[2] = bytes[i + 2];
                        if (numBands == 4) {
                            pixel[3] = 0xFF;
                        }
                        i += 2;
                        break;
                }
                setPixel(pixel, false, false);
            } else {
                int[] counts = variableBytesToInt(bytes, i);
                i = counts[0] - 1;
                for (int count = 0; count < counts[1]; count++) {
                    setPixel(pixel, ignoreRepetition, counts[1] > 8);
                }
            }
        }
    }

    private void setPixel(int[] pixel, boolean ignoreRepetition, boolean random) {
        if (!ignoreRepetition) {
            bufferedImage.getRaster().setPixel(coordinateManager.getX(), coordinateManager.getY(), random ? derivatePixel(pixel) : pixel);
        }
        coordinateManager.next();
    }

    private int[] derivatePixel(int[] pixel) {
        if (!derivate) return pixel;
        int dr = this.random.nextInt(5) - 2;
        int dg = this.random.nextInt(5) - 2;
        int db = this.random.nextInt(5) - 2;

        int[] nPixel = Arrays.copyOf(pixel, pixel.length);
        nPixel[0] = nPixel[0] + dr;
        if (nPixel[0] > 255 || nPixel[0] < 0) nPixel[0] = nPixel[0] - dr;

        nPixel[1] = nPixel[1] + dg;
        if (nPixel[1] > 255 || nPixel[1] < 0) nPixel[1] = nPixel[1] - dg;

        nPixel[2] = nPixel[2] + db;
        if (nPixel[2] > 255 || nPixel[2] < 0) nPixel[2] = nPixel[2] - db;

        return nPixel;
    }

    public BufferedImage getImage() {
        return bufferedImage;
    }

    private int[] variableBytesToInt(byte[] bytes, int index) {
        int[] size = new int[]{(bytes[index] & 0xFF), 0};
        int multiplier = 1;
        for (int i = 0; i < size[0]; i++) {
            size[1] = size[1] + (bytes[index + i + 1] & 0xFF) * multiplier;
            multiplier *= 256;
        }
        size[0] = index + size[0] + 1;
        return size;
    }

    private long intsToLong(int[] ints) {
        long l = 0;
        for (int i = 0; i < ints.length; i++) {
            l = l << 8 | ints[i];
        }
        return l;
    }

    private int[] longToInts(long l) {
        int[] ints = new int[numBands];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ((byte) l) & 0xFF;
            l >>>= 8;
        }
        for (int i = 0; i < ints.length / 2; i++) {
            int temp = ints[i];
            ints[i] = ints[ints.length - 1 - i];
            ints[ints.length - 1 - i] = temp;
        }
        return ints;
    }

}
