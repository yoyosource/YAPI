package yapi.compression.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static yapi.compression.image.ImageCompression.*;

public class ImageDownScale {

    public static void main(String[] args) throws Exception {
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/Bildschirmfoto 2021-01-05 um 11.34.18.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/Bildschirmfoto 2021-01-12 um 12.29.33.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/Bildschirmfoto 2021-01-12 um 13.58.51.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/Bildschirmfoto 2021-01-12 um 14.58.29.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/example-orig.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/example-shrunk.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/test.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/test2.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/test3.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/test4.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/test5.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/EmojisDiscord/DiscordYoyoNow.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/miny.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/bannerdesign3.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/vector-cartoon-stick-figure-drawing-conceptual-illustration-of-couple-of-man-and-pregnant-woman-walk.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/vector-cartoon-stick-figure-drawing-conceptual-illustration-of-couple-of-man-and-pregnant-woman-walk.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/stern.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/WEB_AROSA_Schiffe_Allgemein__c_A-ROSA-Flusschiff-_36_.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/spider_kurbis.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2._Weltkrieg_-_deutscher_panzer.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/IMG-20200912-WA0001.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/iu-2.jpeg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown2.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown3.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown4.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown5.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown6.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/Unknown7.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/programming-languages.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/monofont-1.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/wide.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-11_20.24.53.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/20200314_155143.jpg");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/20200314_155143-small.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/photo_2021-01-08_20-50-54.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/oh4LU0i70V-image0.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/PyKxTu2M0P-unknown.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/unknown8.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/Bildschirmfoto 2021-01-12 um 20.28.22.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Desktop/preview.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-12_20.39.31.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-12_20.46.25.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-12_21.14.09.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-12_21.49.27.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_12.45.04.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_12.50.18.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_12.54.11.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_12.55.13.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_12.55.25.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Library/Application Support/minecraft/screenshots/2021-01-13_13.15.37.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/313613c6aa91e82ecb5d78b9fe15bca4.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-13_13.35.33.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-13_13.35.16.png");
        // StringBuilder st = new StringBuilder("/Users/jojo/Downloads/2021-01-13_13.37.21.png");
        StringBuilder st = new StringBuilder("/Users/jojo/Downloads/bd30cb0514df0ca80730588f7a538562.png");
        int scaleFactor = 1;
        double lossyCompressFactor = LOSSY_DEFAULT;
        double detailFactor = DETAIL_HIGH;

        File file = new File(st.toString());
        st.insert(st.length() - 4, ".scaled");
        File fileScaled = new File(st.toString());
        st.insert(st.length() - 4, ".compressed");
        File fileScaledCompressed = new File(st.toString());
        st.insert(st.length() - 4, ".2");
        File fileScaledCompressed2 = new File(st.toString());

        ImageDownScale imageDownScale = new ImageDownScale(ImageIO.read(file));
        ImageIO.write(imageDownScale.scale(scaleFactor), "png", fileScaled);
        ImageCompression imageCompression = new ImageCompression(imageDownScale.scale(scaleFactor));
        imageCompression.detailCompress(detailFactor);
        imageCompression.lossyCompress(lossyCompressFactor);

        byte[] compress = imageCompression.compress();
        ImageDecompression imageDecompression = new ImageDecompression(compress, false, true);
        ImageIO.write(imageDecompression.getImage(), "png", fileScaledCompressed);

        imageDecompression = new ImageDecompression(compress, true, false);
        ImageIO.write(imageDecompression.getImage(), "png", fileScaledCompressed2);

        if (true) return;
        st = new StringBuilder();
        for (byte b : compress) {
            st.append(String.format("%02X", b)).append(" ");
        }
        System.out.println(st.toString());
    }

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
