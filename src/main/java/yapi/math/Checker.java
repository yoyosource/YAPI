package yapi.math;

import java.util.ArrayList;
import java.util.List;

public class Checker {

    private List<Double> doubles = new ArrayList<>();
    private List<Double> doubleList = new ArrayList<>();
    private int i = 0;

    /**
     *
     * @since Version 1
     *
     * @param d
     * @return
     */
    public boolean add(double d) {
        boolean b = false;
        if (doubles.isEmpty()) {
            doubles.add(d);
        } else {
            if (doubles.size() == i) {
                return true;
            }
            if (doubles.get(i) == d) {
                i++;
                doubleList.add(d);
            } else {
                if (doubleList.size() != 0) {
                    i = 0;
                    for (double k : doubleList) {
                        doubles.add(k);
                    }
                    doubleList = new ArrayList<>();
                }
                doubles.add(d);
            }
        }
        return b;
    }

}
