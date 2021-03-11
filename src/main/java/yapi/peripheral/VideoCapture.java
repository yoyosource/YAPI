// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.peripheral;

import yapi.compression.image.ImageCompression;
import yapi.datastructures.circular.CircularQueue;
import yapi.manager.worker.WorkerPool;
import yapi.runtime.ThreadUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class VideoCapture {

    private final GraphicsDevice graphicsDevice;
    private boolean running = false;
    private double compression;

    private final CircularQueue<byte[]> bytes = new CircularQueue<>(10);
    private int count = 0;

    private final Thread thread = new Thread(() -> {
        WorkerPool workerPool = new WorkerPool(60);
        LinkedList<BufferedImage> bufferedImages = new LinkedList<>();
        while (running) {
            while (workerPool.openTasks() > 4) {
                ThreadUtils.sleep(10);
            }
            try {
                bufferedImages.addLast(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
            } catch (AWTException e) {
                e.printStackTrace();
            }
            workerPool.work(() -> {
                BufferedImage image = bufferedImages.removeFirst();
                // Image image = PeripheralUtils.takeScreenShot(graphicsDevice);
                if (image != null) {
                    long time = System.currentTimeMillis();
                    ImageCompression imageCompression = new ImageCompression(image);
                    if (compression != -1) {
                        imageCompression.lossyCompress(compression);
                    }
                    byte[] imageBytes = imageCompression.compress();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/jojo/Desktop/capture", "image-" + count++))) {
                        fileOutputStream.write(imageBytes);
                    } catch (IOException e) {

                    }
                    System.out.println((System.currentTimeMillis() - time) + " " + imageBytes);
                }
            });
            ThreadUtils.sleep(16);

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