package yapi.manager.worker;

import yapi.exceptions.YAPIException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskParalyse <T> {

    private Map<String, List<T>> tMap = new HashMap<>();

    public static void main(String[] args) {

    }

    public TaskParalyse() {

    }

    public List<List<T>> split(List<T> ts, int poolSize) {
        if (poolSize > ts.size()) {
            throw new YAPIException("poolSize needs to be smaller than your input list");
        }

        int ad = (int)Math.round(ts.size() / (double)poolSize);
        List<List<T>> r = new ArrayList<>();
        int j = 0;
        List<T> g = new ArrayList<>();
        for (T t : ts) {
            g.add(t);
            j++;
            if (j == ad || j > ad) {
                r.add(g);
                g = new ArrayList<>();
                j = 0;
            }
        }
        if (!g.isEmpty()) {
            r.add(g);
        }
        return r;
    }

    public void addResult(T t) {
        synchronized (tMap) {
            tMap.get(Thread.currentThread().getName() + Thread.currentThread().getId()).add(t);
        }
    }

    public List<T> merge() {
        List<T> merge = new ArrayList<>();
        synchronized (tMap) {
            for (Map.Entry<String, List<T>> entry : tMap.entrySet()) {
                merge.addAll(entry.getValue());
            }
        }
        return merge;
    }

}
