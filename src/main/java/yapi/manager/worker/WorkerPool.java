// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.worker;

import yapi.manager.log.Logging;
import yapi.runtime.ThreadUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorkerPool implements Runnable {

    private List<Worker> available = new ArrayList<>();
    private List<Worker> all = new ArrayList<>();
    private List<CachedWorker> cachedWorkers = new CopyOnWriteArrayList<>();
    private boolean run = true;
    private boolean started = false;
    private WorkerPool pool;

    private List<Task> tasks = new CopyOnWriteArrayList<>();

    private int minWorkers;
    private int maxWorkers;

    private static long wIDGlobal = 0;
    private long wID = wIDGlobal++;
    private long id = 0;
    private long taskID = 0;
    private long lastTaskAssigned = 0;
    private int wait = 0;

    private Logging log = new Logging("Worker Pool");
    private int lastLogSize = 0;

    private boolean daemon = false;

    private static ThreadGroup threadGroup = new ThreadGroup(ThreadUtils.yapiGroup, "Worker Pools");
    private ThreadGroup workerGroup = new ThreadGroup(threadGroup, "Worker Pool: " + wID);
    private ThreadGroup workerPoolGroup = new ThreadGroup(workerGroup, "Workers");

    private String name;

    /**
     *
     * @since Version 1.1
     *
     * @param workers
     */
    public WorkerPool(int workers) {
        if (workers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        minWorkers = workers;
        maxWorkers = workers;

        pool = this;

        createWorkers();
        taskManager();
    }

    /**
     * @since Version 1.2
     *
     * @param workers
     * @param startLater
     */
    public WorkerPool(int workers, boolean startLater) {
        if (workers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        minWorkers = workers;
        maxWorkers = workers;

        pool = this;

        createWorkers();
        if (!startLater) {
            taskManager();
        }
    }

    /**
     *
     * @since Version 1.1
     *
     * @param minWorkers
     * @param maxWorkers
     */
    public WorkerPool(int minWorkers, int maxWorkers) {
        if (minWorkers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        if (maxWorkers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        if (minWorkers > maxWorkers) {
            minWorkers = maxWorkers;
        }
        this.minWorkers = minWorkers;
        this.maxWorkers = maxWorkers;

        pool = this;

        createWorkers();
        taskManager();
    }

    /**
     * @since Version 1.2
     *
     * @param minWorkers
     * @param maxWorkers
     * @param startLater
     */
    public WorkerPool(int minWorkers, int maxWorkers, boolean startLater) {
        if (minWorkers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        if (maxWorkers < 1) {
            throw new IllegalArgumentException("No negative Workers!");
        }
        if (minWorkers > maxWorkers) {
            minWorkers = maxWorkers;
        }
        this.minWorkers = minWorkers;
        this.maxWorkers = maxWorkers;

        pool = this;

        createWorkers();
        if (!startLater) {
            taskManager();
        }
    }

    public void setName(String name) {
        try {
            Field field = workerGroup.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(workerGroup, "WorkerPool[" + wID + "]: " + name);
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
    }

    private void createWorkers() {
        log.add("Create " + minWorkers + " workers");
        while (all.size() < minWorkers) {
            Worker t = new Worker(workerPoolGroup, this);
            t.setDaemon(daemon);
            t.setName("Worker Pool: Worker " + (++id));
            t.start();
            all.add(t);
        }
    }

    private void createWorker() {
        if (all.size() < maxWorkers) {
            if (!cachedWorkers.isEmpty()) {
                Worker worker = cachedWorkers.remove(0).getWorker();
                all.add(worker);
                available.add(worker);
                log.add("Unfreeze Worker: '" + worker.getName() + "'");
                return;
            }
            Worker t = new Worker(workerPoolGroup, this);
            t.setDaemon(daemon);
            t.setName("Worker Pool: Worker " + (++id));
            t.start();
            all.add(t);
            log.add("Create Worker: '" + t.getName() + "'");
        }
    }

    private synchronized void deleteWorker(Worker worker) {
        synchronized (all) {
            log.add("Freeze Worker: '" + worker.getName() + "'");
            cachedWorkers.add(new CachedWorker(worker));
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getName().equals(worker.getName())) {
                    all.remove(i);
                    break;
                }
            }
        }
    }

    private boolean poolManager() {
        if (maxWorkers != minWorkers) {
            wait--;
            if (wait > 0) {
                return false;
            }
            if (!tasks.isEmpty() && System.currentTimeMillis() - lastTaskAssigned > 2000) {
                createWorker();
                wait = 2;
                return true;
            }
        }
        return false;
    }

    private void logWorkerPoolInfo() {
        if (lastLogSize == log.size()) {
            return;
        }
        log.add(  "    WorkerPool Info:" +
                "\n    Worker: " + all.size() + "/" + available.size() + "" +
                "\n    Cached: " + cachedWorkers.size() + "   Cached Time: " + (cachedWorkers.isEmpty() ? "---" : (cachedWorkers.get(0).cachedTime() / 1000.0) + "s") + "" +
                "\n    MinWorker: " + minWorkers + "   MaxWorker: " + maxWorkers + "" +
                "\n    Tasks: " + +tasks.size() + "" +
                "\n    LastAssignedTaskTime: " + lastTaskAssigned + "   " + ((lastTaskAssigned - System.currentTimeMillis()) / 1000.0) + "s");
        lastLogSize = log.size();
    }

    @Override
    public synchronized void run() {
        long workerPoolInfo = System.currentTimeMillis();
        while (run) {
            while (tasks.isEmpty() || available.isEmpty()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (!run) {
                    break;
                }
                delegatePool();
                if (System.currentTimeMillis() - workerPoolInfo > 10000) {
                    workerPoolInfo = System.currentTimeMillis();
                    logWorkerPoolInfo();
                }
            }
            delegateTask();
        }
    }

    private void delegatePool() {
        boolean b = poolManager();
        if (!b && tasks.isEmpty() && all.size() > minWorkers && System.currentTimeMillis() - lastTaskAssigned > 20000) {
            deleteWorker(all.get(0));
        }
        if (!cachedWorkers.isEmpty()) {
            if (cachedWorkers.get(0).cachedTime() > 20000) {
                log.add("Delete Worker: '" + cachedWorkers.get(0).getWorker().getName() + "'");
                cachedWorkers.remove(0).getWorker().delete();
            }
        }
    }

    private synchronized void delegateTask() {
        if (tasks.isEmpty() || available.isEmpty()) {
            return;
        }
        Worker worker = available.remove(0);
        Task task = tasks.remove(0);
        if (worker == null) {
            tasks.add(task);
            return;
        }
        log.add("Tasks: " + tasks.size());
        lastTaskAssigned = System.currentTimeMillis();
        worker.setTask(task);
    }

    private void taskManager() {
        if (started) {
            return;
        }
        started = true;
        Thread t = new Thread(workerGroup, this);
        t.setName("Worker Pool: Task Manager");
        t.start();
        log.add("Started Task Manager Thread");
    }

    public void start() {
        taskManager();
    }

    void done(Worker worker) {
        if ((System.currentTimeMillis() - lastTaskAssigned < 1500 || tasks.isEmpty()) && all.size() > minWorkers) {
            deleteWorker(worker);
        } else {
            available.add(worker);
        }
    }

    void delete(Worker worker) {
        all.remove(worker);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param task
     */
    public synchronized void work(Task task) {
        if (!run) {
            return;
        }
        task.setTaskID(++taskID);
        tasks.add(task);
        notifyAll();
    }

    public synchronized void work(TaskRunnable runnable) {
        if (!run) {
            return;
        }
        Task task = new Task(runnable);
        task.setTaskID(++taskID);
        tasks.add(task);
        notifyAll();
    }

    /**
     * @since Version 1.1
     */
    public synchronized void close() {
        log.add("Starting to close");
        run = false;
        int tries = 0;
        while (!all.isEmpty()) {
            while (available.isEmpty()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (tries++ > 20) {
                    break;
                }
            }
            if (tries > 20) {
                break;
            }
            tries = 0;
            Worker worker = available.remove(0);
            if (worker == null) {
                continue;
            }
            worker.finish();
            log.add("Closing > " + worker.getName());
            all.remove(worker);
        }
        while (!all.isEmpty()) {
            Worker worker = all.remove(0);
            worker.finish();
            log.add("Closing > " + worker.getName());
        }
        try {
            workerGroup.destroy();
        } catch (IllegalThreadStateException e) {

        }
        log.add("Finished to close");
    }

    /**
     * @since Version 1.2
     *
     * @return
     */
    public boolean isDoneWorker() {
        return all.size() == available.size();
    }

    /**
     * @since Version 1.2
     */
    public void awaitWorker() {
        synchronized (this) {
            while (all.size() != available.size()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * @since Version 1.2
     *
     * @return
     */
    public boolean isDone() {
        return tasks.isEmpty();
    }

    /**
     * @since Version 1.1
     */
    public void await() {
        synchronized(this) {
            while (!tasks.isEmpty()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * @since Version 1.1
     */
    public void awaitClose() {
        synchronized (this) {
            log.add("Starting to wait on finishing all tasks");
            while (!tasks.isEmpty()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            log.add("Finished with all tasks");
            close();
        }
    }

    void log(String message) {
        log.add(message);
    }

    private class CachedWorker {

        private long time = System.currentTimeMillis();
        private Worker worker;

        public CachedWorker(Worker worker) {
            this.worker = worker;
        }

        public Worker getWorker() {
            return worker;
        }

        public long cachedTime() {
            return System.currentTimeMillis() - time;
        }

    }

    @Override
    public String toString() {
        return "WorkerPool{" +
                "available=" + available.size() +
                ", minWorkers=" + minWorkers +
                ", maxWorkers=" + maxWorkers +
                ", currentWorkers=" + all.size() +
                '}';
    }

}