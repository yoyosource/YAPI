package yapi.compression.image;

import yapi.manager.worker.WorkerPool;
import yapi.runtime.ThreadUtils;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ImageCompression:
 * - ImageType (byte)
 * - NumBands (byte)
 * - ImageHeight (variable bytes {@link #intToVariableBytes(List, int)})
 * - ImageWidth (variable bytes {@link #intToVariableBytes(List, int)})
 * - ColorReferenceCount (byte) min: 0, max: 239
 * - OPTIONAL: ColorReferences (nBytes expressed by {NumBands})
 * - Picture:
 *   - Pixel:
 *     - 0x00
 *     - {Color Compression}
 *   - Repetition:
 *     - repetition (variable bytes {@link #intToVariableBytes(List, int)})
 *
 * Color Compression:
 * Number 0 - 250 > ColorReference to the header
 * Number 251     > Default color
 * Number 252     > RGB color
 * Number 253     > Gray Alpha color
 * Number 254     > Gray color
 */
public class ImageCompression {

    public static final double LOSSY_LOW = 3;
    public static final double LOSSY_DEFAULT = 5;
    public static final double LOSSY_MEDIUM = 8;
    public static final double LOSSY_HIGH = 17;
    public static final double LOSSY_NOTICABLE = 23;
    public static final double LOSSY_EXTREME = 50;

    public static final double DETAIL_LOW = 0.1;
    public static final double DETAIL_MEDIUM = 0.5;
    public static final double DETAIL_HIGH = 0.9;

    private static final Map<Integer, String> modeMapping = new HashMap<>();

    static {
        modeMapping.put(0, "lr:ud 0");
        modeMapping.put(1, "ud:lr 1");
        modeMapping.put(2, "rl:ud 2");
        modeMapping.put(3, "ud:rl 3");
        modeMapping.put(4, "lr:du 4");
        modeMapping.put(5, "du:lr 5");
        modeMapping.put(6, "rl:du 6");
        modeMapping.put(7, "du:rl 7");
    }

    private final BufferedImage bufferedImage;
    private final int numBands;

    public ImageCompression(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        numBands = bufferedImage.getRaster().getNumBands();
    }

    public void lossyCompress(double compressionLevel) {
        if (compressionLevel <= 0) return;
        for (int i = 0; i < 8; i++) {
            lossyCompress(compressionLevel, i);
        }
    }

    private void lossyCompress(double compressionLevel, int mode) {
        CoordinateManager coordinateManager = new CoordinateManager(mode, bufferedImage.getWidth(), bufferedImage.getHeight());
        int[] lastInts = null;
        while (coordinateManager.hasNext()) {
            int[] ints = bufferedImage.getRaster().getPixel(coordinateManager.getX(), coordinateManager.getY(), new int[numBands]);
            if (lastInts != null && getDistance(ints, lastInts) <= compressionLevel) {
                bufferedImage.getRaster().setPixel(coordinateManager.getX(), coordinateManager.getY(), lastInts);
            } else {
                lastInts = ints;
            }
            coordinateManager.next();
        }
    }

    public void detailCompress(double detailLevel) {
        if (detailLevel < 0 || detailLevel > 1) return;
        CoordinateManager coordinateManager = new CoordinateManager(0, bufferedImage.getWidth(), bufferedImage.getHeight());
        Map<Integer, Long> colorMap = new HashMap<>();

        WorkerPool workerPool = new WorkerPool(8);
        double d = (1 - detailLevel) / 80;
        Set<Integer> coordinates = new HashSet<>();
        while (coordinateManager.hasNext()) {
            int cx = coordinateManager.getX();
            int cy = coordinateManager.getY();
            coordinates.add(cy * bufferedImage.getWidth() + cx);
            coordinateManager.next();

            if (coordinates.size() < 16384) {
                continue;
            }
            Set<Integer> integers = new HashSet<>(coordinates);
            coordinates.clear();

            workerPool.work(() -> {
                for (int current : integers) {
                    int x = current % bufferedImage.getWidth();
                    int y = current / bufferedImage.getWidth();
                    double[] doubles = new double[numBands];
                    for (int dx = -4; dx <= 4; dx++) {
                        if (x + dx < 0 || x + dx >= bufferedImage.getWidth()) continue;
                        for (int dy = -4; dy <= 4; dy++) {
                            if (y + dy < 0 || y + dy >= bufferedImage.getHeight()) continue;

                            int[] pixel = bufferedImage.getRaster().getPixel(x + dx, y + dy, new int[numBands]);
                            if (dx == 0 && dy == 0) {
                                for (int i = 0; i < pixel.length; i++) {
                                    doubles[i] = doubles[i] + pixel[i] * detailLevel;
                                }
                            } else {
                                for (int i = 0; i < pixel.length; i++) {
                                    doubles[i] = doubles[i] + pixel[i] * d;
                                }
                            }
                        }
                    }
                    int[] ints = new int[numBands];
                    for (int i = 0; i < doubles.length; i++) {
                        ints[i] = (int) doubles[i];
                    }
                    colorMap.put(current, intsToLong(ints));
                }
            });
        }
        while (!workerPool.isDone()) {
            ThreadUtils.sleep(100);
        }
        workerPool.close();

        colorMap.forEach((integer, aLong) -> {
            int[] color = longToInts(aLong);
            int x = integer % bufferedImage.getWidth();
            int y = integer / bufferedImage.getWidth();
            bufferedImage.getRaster().setPixel(x, y, color);
        });
    }

    private double getDistance(int[] c1, int[] c2) {
        double color = 0;
        for (int i = 0; i < c1.length; i++) {
            color += (c1[i] - c2[i]) * (c1[i] - c2[i] + 0.0);
        }
        return Math.sqrt(color);
    }

    LinkedHashMap<Long, Integer> sortedPixels;

    public byte[] compress() {
        System.out.println(bufferedImage.getWidth() * bufferedImage.getHeight() * numBands);

        Map<Long, Integer> pixelCount = new HashMap<>();
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int[] pixel = bufferedImage.getRaster().getPixel(x, y, new int[numBands]);
                long l = intsToLong(pixel);
                if (!pixelCount.containsKey(l)) {
                    pixelCount.put(l, 0);
                }
                pixelCount.put(l, pixelCount.get(l) + 1);
            }
        }

        sortedPixels = new LinkedHashMap<>(){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest) {
                return size() > 250;
            }
        };
        pixelCount.entrySet().stream().filter(x -> x.getValue() > 1).sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedPixels.put(x.getKey(), x.getValue()));
        AtomicInteger atomicInteger = new AtomicInteger(0);
        sortedPixels.entrySet().forEach(longIntegerEntry -> longIntegerEntry.setValue(atomicInteger.getAndIncrement()));

        AtomicInteger mode = new AtomicInteger(0);
        AtomicReference<byte[]> best = new AtomicReference<>(null);

        WorkerPool workerPool = new WorkerPool(2);
        for (int i = 0; i < 8; i++) {
            int finalI = i;
            workerPool.work(() -> {
                byte[] current = compress(finalI);
                System.out.println("bytes before: " + bufferedImage.getWidth() * bufferedImage.getHeight() * numBands + "   bytes after: " + current.length + "   mode: " + modeMapping.get(finalI) + " (r=right, l=left, u=up, d=down)   bytes removed: " + (bufferedImage.getWidth() * bufferedImage.getHeight() * numBands - current.length) + "   percentage of original: " + (current.length / (bufferedImage.getWidth() * bufferedImage.getHeight() * numBands * 1.0) * 100) + "%");
                synchronized (best) {
                    if (best.get() == null || best.get().length > current.length) {
                        best.set(current);
                        mode.set(finalI);
                    }
                }
            });
        }
        workerPool.awaitClose();

        System.out.println();
        System.out.println("bytes before: " + bufferedImage.getWidth() * bufferedImage.getHeight() * numBands + "   bytes after: " + best.get().length + "   mode: " + modeMapping.get(mode.get()) + " (r=right, l=left, u=up, d=down)   bytes removed: " + (bufferedImage.getWidth() * bufferedImage.getHeight() * numBands - best.get().length) + "   percentage of original: " + (best.get().length / (bufferedImage.getWidth() * bufferedImage.getHeight() * numBands * 1.0) * 100) + "%");
        return best.get();
    }

    private byte[] compress(int mode) {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) bufferedImage.getType());
        byteList.add((byte) numBands);
        intToVariableBytes(byteList, bufferedImage.getHeight());
        intToVariableBytes(byteList, bufferedImage.getWidth());
        byteList.add((byte) mode);

        byteList.add((byte) sortedPixels.size());
        if (sortedPixels.size() > 0) {
            sortedPixels.forEach((key, value) -> {
                for (int pixelSubData : longToInts(key)) {
                    byteList.add((byte) pixelSubData);
                }
            });
        }

        int[] lastInts = null;
        int size = 0;

        CoordinateManager coordinateManager = new CoordinateManager(mode, bufferedImage.getWidth(), bufferedImage.getHeight());
        while (coordinateManager.hasNext()) {
            int[] ints = bufferedImage.getRaster().getPixel(coordinateManager.getX(), coordinateManager.getY(), new int[numBands]);
            coordinateManager.next();

            if (lastInts != null && Arrays.equals(ints, lastInts)) {
                size++;
                continue;
            }
            if (size > 0) {
                intToVariableBytes(byteList, size);
            }
            size = 0;
            byteList.add((byte) 0);
            long pixelData = intsToLong(ints);
            if (sortedPixels.containsKey(pixelData)) {
                byteList.add((byte) (int) sortedPixels.get(pixelData));
                lastInts = ints;
                continue;
            }
            if (numBands >= 3 && ints[0] == ints[1] && ints[0] == ints[2]) {
                if (numBands == 4 && ints[3] == 255) {
                    byteList.add((byte) 254);
                    byteList.add((byte) ints[0]);
                } else if (numBands == 4) {
                    byteList.add((byte) 253);
                    byteList.add((byte) ints[0]);
                    byteList.add((byte) ints[3]);
                } else {
                    byteList.add((byte) 254);
                    byteList.add((byte) ints[0]);
                }
            } else if (numBands == 4 && ints[3] == 255) {
                byteList.add((byte) 252);
                for (int i = 0; i < ints.length - 1; i++) {
                    byteList.add((byte) ints[i]);
                }
            } else {
                byteList.add((byte) 251);
                for (int i : ints) {
                    byteList.add((byte) i);
                }
            }
            lastInts = ints;
        }

        if (size > 0) {
            intToVariableBytes(byteList, size);
        }
        return bytes(byteList);
    }

    private void intToVariableBytes(List<Byte> bytes, int size) {
        List<Byte> sizes = new ArrayList<>();
        while (size != 0) {
            sizes.add((byte) size);
            size /= 256;
        }
        bytes.add((byte) sizes.size());
        bytes.addAll(sizes);
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

    private byte[] bytes(List<Byte> bytes) {
        byte[] iBytes = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            iBytes[i] = bytes.get(i);
        }
        return iBytes;
    }

}
