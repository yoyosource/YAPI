// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WorkerPool implements Runnable {

    private List<Worker> available = new ArrayList<>();
    private List<Worker> all = new ArrayList<>();
    private boolean run = true;
    private WorkerPool pool;

    private List<Task> tasks = new CopyOnWriteArrayList<>();

    private int minWorkers;
    private int maxWorkers;

    private long id = 0;
    private long taskID = 0;
    private int wait = 0;

    private List<String> log = new ArrayList<>();

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

    private void createWorkers() {
        log.add("Created " + minWorkers + " workers");
        while (all.size() < minWorkers) {
            Worker t = new Worker(this);
            t.setName("Worker Pool: Worker " + (++id));
            t.start();
            all.add(t);
        }
    }

    private void createWorker() {
        if (all.size() < maxWorkers) {
            log.add("Created new Worker: " + all.size());
            Worker t = new Worker(this);
            t.setName("Worker Pool: Worker " + (++id));
            t.start();
            all.add(t);
        }
    }

    private void deleteWorker() {
        if (all.size() <= minWorkers) {
            return;
        }
        while (available.isEmpty()) {
            try {
                synchronized (this) {
                    wait(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (available.isEmpty()) {
            deleteWorker();
            return;
        }
        available.remove(0).delete();
        //System.out.println(available.size() + " " + all.size());
    }

    private void poolManager() {
        if (maxWorkers != minWorkers) {
            wait--;
            if (tasks.size() > all.size() + all.size() && wait <= 0) {
                createWorker();
                wait = 2;
            } else if (tasks.size() < all.size() * (all.size() / 2) && wait <= 0) {
                //deleteWorker();
                wait = 2;
            }
        }
    }

    @Override
    public synchronized void run() {
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
                poolManager();
            }
            synchronized (pool) {
                if (tasks.isEmpty() || available.isEmpty()) {
                    continue;
                }
                Worker worker = available.remove(0);
                Task task = tasks.remove(0);
                if (worker == null) {
                    tasks.add(task);
                    continue;
                }
                worker.setTask(task);
            }
        }
    }

    private void taskManager() {
        Thread t = new Thread(this);
        t.setName("Worker Pool: Task Manager");
        t.start();
        log.add("Started Task Manager Thread");
    }

    void done(Worker worker) {
        available.add(worker);
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
        log.add("Finished to close");
    }

    /**
     * @since Version 1.1
     */
    public synchronized void await() {
        while (!tasks.isEmpty()) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * @since Version 1.1
     */
    public synchronized void awaitClose() {
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

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public String getLog() {
        return log.stream().collect(Collectors.joining("\n"));
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