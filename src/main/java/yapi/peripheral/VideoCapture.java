// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.peripheral;

import yapi.compression.image.ImageCompression;
import yapi.datastructures.circular.CircularQueue;
import yapi.runtime.ThreadUtils;

import java.awt.*;

public class VideoCapture {

    private GraphicsDevice graphicsDevice;
    private boolean running = false;
    private double compression;

    private CircularQueue<byte[]> bytes = new CircularQueue<>(10);

    private Thread thread = new Thread(() -> {
        while (running) {
            Image image = PeripheralUtils.takeScreenShot(graphicsDevice);
            if (image != null) {
                ImageCompression imageCompression = new ImageCompression(image);
                if (compression == -1) {
                    bytes.add(imageCompression.getRaw());
                } else {
                    imageCompression.lossyCompress(compression);
                    bytes.add(imageCompression.compress());
                }
                System.out.println(bytes);
            }
            ThreadUtils.sleep(1000/60);
        }
    });

    public static void main(String[] args) {
        VideoCapture videoCapture = new VideoCapture(PeripheralUtils.getDefaultScreenDevice(), 8);
        videoCapture.startCapture();
    }

    /**
     *
     * @param graphicsDevice
     * @param compression compression is the strength of the compression
     *                    use -1 for no compression and positive values for compression
     *                    default values are between 0 and 10. More than 50 is not allowed
     */
    public VideoCapture(GraphicsDevice graphicsDevice, double compression) {
        this.graphicsDevice = graphicsDevice;
        if (compression <= -1 || compression > 50) {
            throw new IllegalArgumentException("Compression strength needs to be between -1 and 50");
        }
        this.compression = compression;
    }

    public void startCapture() {
        if (!thread.isAlive() && !running) {
            running = true;
            thread.setDaemon(true);
            thread.setName("Video Capture Thread");
            thread.start();
        }
    }

    public void stopCapture() {
        if (thread.isAlive() && running) {
            running = false;
        }
    }

}