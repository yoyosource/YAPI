// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageDecompression {

    public static void main(String[] args) throws IOException {
        ImageCompression imageCompression = new ImageCompression(ImageIO.read(new File("/Users/jojo/Desktop/Bildschirmfoto 2021-01-05 um 11.34.18.png")));
        imageCompression.lossyCompress(ImageCompression.COMPRESSION_HIGH);
        byte[] bytes = imageCompression.compress();
        System.out.println(bytes.length);

        ImageDecompression imageDecompression = new ImageDecompression(bytes);
        ImageIO.write(imageDecompression.getImage(), "png", new File("test.png"));
    }

    private int type;
    private int width;
    private int height;

    private int parse = 0;

    private BufferedImage image;

    public ImageDecompression(byte[] bytes) {
        int index = parseHeader(bytes) + 1;
        parseBody(bytes, index);
    }

    private int parseHeader(byte[] bytes) {
        int index = 0;
        type = bytes[index++];

        index = parseDim(bytes, index);
        index = parseDim(bytes, index);

        // System.out.println(type + " " + width + " " + height);

        return index;
    }

    private void parseBody(byte[] bytes, int index) {
        boolean b = false;
        List<Byte> bts = new ArrayList<>();
        for (int i = index; i < bytes.length; i++) {
            if (b) {
                byte b1 = bts.get(bts.size() - 3);
                byte b2 = bts.get(bts.size() - 2);
                byte b3 = bts.get(bts.size() - 1);

                int count = bytes[i] & 0xFF;
                while (count > 0) {
                    bts.add(b1);
                    bts.add(b2);
                    bts.add(b3);
                    count--;
                }
                b = false;
            } else {
                if (bytes[i] == 1) {
                    b = true;
                    continue;
                }
                bts.add(bytes[i]);
            }
        }

        // System.out.println(bts.size());
        image = new BufferedImage(width, height, type);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, toByteArray(bts));
    }

    private int[] toByteArray(List<Byte> bytes) {
        int[] bts = new int[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bts[i] = bytes.get(i);
        }
        return bts;
    }

    private int parseDim(byte[] bytes, int index) {
        int m = 1;
        for (int i = 0; i < bytes[index]; i++) {
            if (parse == 0) {
                width += (bytes[i + index + 1]) * m;
            } else {
                height += (bytes[i + index + 1]) * m;
            }
            m *= 255;
        }
        parse++;
        return index + (bytes[index] + 1);
    }

    public BufferedImage getImage() {
        return image;
    }

}