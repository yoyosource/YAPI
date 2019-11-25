package yapi.manager.worker;

public class Task {

    private long taskID = 0;

    final void setTaskID(long id) {
        taskID = id;
    }

    /**
     * @since Version 1.1
     *
     * @return
     */
    public final long getTaskID() {
        return taskID;
    }

    /**
     * @since Version 1.1
     *
     * @return
     */
    public final long getID() {
        return taskID;
    }

    /**
     * @since Version 1.1
     */
    public void run() {

    }

}
