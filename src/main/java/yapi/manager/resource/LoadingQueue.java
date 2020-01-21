package yapi.manager.resource;

import java.util.ArrayList;
import java.util.List;

public class LoadingQueue {

    private List<QueueEntry> queueEntries = new ArrayList<>();

    /**
     *
     * @since Version 1.1
     *
     * @param path
     * @param name
     * @return
     */
    public LoadingQueue add(String path, String name) {
        queueEntries.add(new QueueEntry(name, path));
        return this;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param path
     * @param name
     * @param async
     * @return
     */
    public LoadingQueue add(String path, String name, boolean async) {
        queueEntries.add(new QueueEntry(name, path, async));
        return this;
    }

    boolean hasNext() {
        return !queueEntries.isEmpty();
    }

    QueueEntry next() {
        return queueEntries.remove(0);
    }

}
