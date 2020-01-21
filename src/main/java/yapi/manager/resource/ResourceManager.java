package yapi.manager.resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager {

    private Map<String, BufferedImage> imageMap = new ConcurrentHashMap<>();
    private Map<String, byte[]> byteMap = new ConcurrentHashMap<>();

    private List<String> loading = new ArrayList<>();

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public byte[] getBytes(String name) {
        return byteMap.get(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public String getString(String name) {
        StringBuilder st = new StringBuilder();
        byte[] bytes = byteMap.get(name);
        for (int i = 0; i < bytes.length; i++) {
            st.append((char) bytes[i]);
        }
        return st.toString();
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public BufferedImage getImage(String name) {
        return imageMap.get(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param path
     * @param name
     */
    private void load(String path, String name) {
        if (loading.contains(name)) {
            return;
        }
        loading.add(name);

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        try (InputStream inputStream = ResourceManager.class.getResourceAsStream(path)) {
            if (path.endsWith(".jpg") || path.endsWith(".png")) {
                BufferedImage image = ImageIO.read(inputStream);
                imageMap.put(name, image);
            } else {
                byteMap.put(name, inputStream.readAllBytes());
            }
        } catch (Exception e) {
            // Ignored
        }
        loading.remove(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param path
     * @param name
     */
    public void loadAsync(String path, String name) {
        Runnable r = () -> load(path, name);
        Thread t = new Thread(r);
        t.setName("ResourceManager: Load > " + name);
        t.start();
    }

    /**
     *
     * @since Version 1.1
     *
     * @param path
     * @param name
     */
    public void loadSync(String path, String name) {
        load(path, name);
    }

    private Thread load(List<QueueEntry> queueEntries) {
        Runnable r = () -> {
            while (!queueEntries.isEmpty()) {
                QueueEntry queueEntry = queueEntries.remove(0);
                if (queueEntry.isAsync()) {
                    loadAsync(queueEntry.getPath(), queueEntry.getName());
                } else {
                    loadSync(queueEntry.getPath(), queueEntry.getName());
                }
            }
        };
        Thread t = new Thread(r);
        t.setName("ResourceManager: Load Manager");
        t.start();
        return t;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param loadingQueue
     */
    public void load(LoadingQueue loadingQueue) {
        List<QueueEntry> queueEntries = new ArrayList<>();
        while (loadingQueue.hasNext()) {
            queueEntries.add(loadingQueue.next());
        }
        load(queueEntries);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param loadingQueue
     */
    public void awaitLoad(LoadingQueue loadingQueue) {
        List<QueueEntry> queueEntries = new ArrayList<>();
        while (loadingQueue.hasNext()) {
            queueEntries.add(loadingQueue.next());
        }
        try {
            load(queueEntries).join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public boolean hasByte(String name) {
        return byteMap.containsKey(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public boolean hasImage(String name) {
        return imageMap.containsKey(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @return
     */
    public boolean isLoading(String name) {
        return loading.contains(name);
    }

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public static LoadingQueue getQueue() {
        return new LoadingQueue();
    }

}
