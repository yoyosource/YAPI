package yapi.compression.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageDownScale {

    private final BufferedImage bufferedImage;
    private final int numBands;

    public ImageDownScale(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        numBands = bufferedImage.getRaster().getNumBands();
    }

    public BufferedImage scale(int scaleFactor) {
        BufferedImage scaledImage = new BufferedImage((int) Math.ceil((double) bufferedImage.getWidth() / scaleFactor), (int) Math.ceil((double) bufferedImage.getHeight() / scaleFactor), bufferedImage.getType());
        List<int[]> pixels = new ArrayList<>();
        for (int x = 0; x < scaledImage.getWidth(); x++) {
            for (int y = 0; y < scaledImage.getHeight(); y++) {
                int rx = x * scaleFactor;
                int ry = y * scaleFactor;

                getPixels(rx, ry, scaleFactor, pixels);
                int[] pixel = average(pixels);
                scaledImage.getRaster().setPixel(x, y, pixel);
            }
        }
        return scaledImage;
    }

    private void getPixels(int x, int y, int d, List<int[]> pixels) {
        pixels.clear();
        for (int rx = x; rx < x + d; rx++) {
            if (rx >= bufferedImage.getWidth()) continue;
            for (int ry = y; ry < y + d; ry++) {
                if (ry >= bufferedImage.getHeight()) continue;
                pixels.add(bufferedImage.getRaster().getPixel(rx, ry, new int[numBands]));
            }
        }
    }

    private int[] average(List<int[]> pixels) {
        int[] ints = new int[numBands];
        for (int[] pixel : pixels) {
            for (int i = 0; i < pixel.length; i++) {
                ints[i] = ints[i] + pixel[i];
            }
        }
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ints[i] / pixels.size();
        }
        return ints;
    }

}
