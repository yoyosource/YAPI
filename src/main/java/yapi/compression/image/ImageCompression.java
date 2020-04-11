// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.image;

import yapi.file.FileUtils;
import yapi.quick.Timer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageCompression {

    public static void main(String[] args) {
        try {
            BufferedImage bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_GrayScale_Vertical.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_GrayScale_Horizontal.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_GrayScale_Vertical2.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_GrayScale_Horizontal2.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_BlueGreen.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_BlueRed.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_GreenRed.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Colors.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Colors2.png"));
            bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Colors3.png"));
            //bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Colors4.png"));
            //bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Colors5.jpg"));
            //bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Diagonal.png"));
            //bufferedImage1 = ImageIO.read(FileUtils.fileFromResourceAsStream("picture/Test_Radial.png"));

            Timer timer = new Timer();
            timer.start();
            for (double i = 0; i < 100; i += 1) {
                ImageCompression imageCompression = new ImageCompression(bufferedImage1);
                imageCompression.lossyCompress(COMPRESSION_DEFAULT);
                imageCompression.compress();

                if (i == 0) {
                    System.out.println(imageCompression.getCompression() + " " + imageCompression.width + " " + imageCompression.height);
                    System.out.println(imageCompression.length + " " + imageCompression.lengthAfterCompression);
                }
                /*
                System.out.println();
                System.out.println(i);
                System.out.println(imageCompression.getCompression() + " " + imageCompression.width + " " + imageCompression.height);
                System.out.println(imageCompression.length + " " + imageCompression.lengthAfterCompression);
                BufferedImage bufferedImage2 = imageCompression.getBufferedImage();

                //ImageIO.write(bufferedImage2, "png", new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/picture/output/Output_" + (int)(i * 10) + ".png"));
                ImageIO.write(imageCompression.bufferedImage, "png", new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/picture/output/Output_" + i + ".png"));
                */
            }
            timer.stop();
            System.out.println(timer.getTime() / 1000 / 100 + "µs");
        } catch (IOException e) {

        }
    }

    public static final double COMPRESSION_LOW = 3;
    public static final double COMPRESSION_DEFAULT = 5;
    public static final double COMPRESSION_HIGH = 8;
    public static final double COMPRESSION_NOTICABLE = 20;
    public static final double COMPRESSION_EXTREME = 50;

    private int width = 0;
    private int height = 0;

    private BufferedImage bufferedImage;
    private int[] colors = new int[0];

    private int type = 0;
    private int depth = 3;

    private int length;
    private int lengthAfterCompression;

    public ImageCompression(Image image) {
        bufferedImage = (BufferedImage)image;
        createData();
    }

    public ImageCompression(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        createData();
    }

    private void createData() {
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        colors = bufferedImage.getRaster().getPixels(0, 0, width, height, (int[])null);
        this.type = bufferedImage.getType();

        if (!(type >= 0 && type <= 7)) {
            throw new IllegalStateException("Image type can only be 0, 1..7");
        }
        if (type == 2 || type == 3 || type == 6 || type == 7) {
            depth = 4;
        }
        length = colors.length;
        lengthAfterCompression = colors.length;
    }

    private double getDistance(int[] c1, int[] c2) {
        double color = 0;
        for (int i = 0; i < c1.length; i++) {
            color += (c1[i] - c2[i]) * (c1[i] - c2[i] + 0.0);
        }
        return Math.sqrt(color);
    }

    private void setColor(int index, int prevIndex, double maxDistance) {
        int[] c1 = getColor(index);
        int[] c2 = getColor(prevIndex);
        double distance = getDistance(c1, c2);
        if (distance < maxDistance) {
            setColor(index, c2);
        } else {
            setColor(index, c1);
        }
    }

    private int[] getColor(int index) {
        int[] ints = new int[depth];
        for (int i = 0; i < depth; i++) {
            ints[i] = colors[index + i];
        }
        return ints;
    }

    private void setColor(int index, int[] cs) {
        for (int i = 0; i < cs.length; i++) {
            colors[index + i] = cs[i];
        }
    }

    public void reset() {
        createData();
    }

    public void lossyCompress(double maxDistance) {
        for (int i = depth; i < colors.length / depth; i++) {
            setColor(i * depth, (i - 1) * depth, maxDistance);
        }
    }

    public void blockCompress(double maxDistance) {

    }

    public byte[] compress() {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == 1) {
                colors[i]++;
            }
        }
        List<Byte> bytes = new ArrayList<>();
        System.out.println(width + " " + height);
        int repetitionLength = 0;
        int[] lastInts = new int[0];
        for (int i = 0; i < colors.length / depth; i++) {
            if (i == 0) {
                int[] ints = getColor(i * depth);
                add(bytes, ints);
                lastInts = ints;
                continue;
            }
            int[] currentColor = getColor(i * depth);
            if (Arrays.equals(currentColor, lastInts)) {
                repetitionLength++;
                if (repetitionLength == 255) {
                    addLength(bytes, length);
                    lastInts = new int[0];
                    repetitionLength = 0;
                }
            } else {
                if (repetitionLength > 0) {
                    addLength(bytes, length);
                }
                add(bytes, currentColor);
                lastInts = currentColor;
            }
        }

        lengthAfterCompression = bytes.size();
        return toByteArray(bytes);
    }

    public static BufferedImage decompress(byte[] bytes) {
        return null;
    }

    private void addLength(List<Byte> bytes, int i) {
        bytes.add((byte)1);
        bytes.add((byte)i);
    }

    private void add(List<Byte> bytes, int... ints) {
        for (int i = 0; i < ints.length; i++) {
            bytes.add((byte)ints[i]);
        }
    }

    private byte[] toByteArray(List<Byte> bytes) {
        byte[] bts = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bts[i] = bytes.get(i);
        }
        return bts;
    }

    public BufferedImage getBufferedImage() {
        BufferedImage image;
        if (type == 0) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        } else {
            image = new BufferedImage(width, height, type);
        }
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, colors);
        return image;
    }

    public double getCompression() {
        return lengthAfterCompression / (double)length;
    }

    public byte[] getRaw() {
        int[] ints = bufferedImage.getRaster().getPixels(0, 0, width, height, (int[])null);
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)ints[i];
        }
        return bytes;
    }

}