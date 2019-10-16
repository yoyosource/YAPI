package yapi.datastructures;

import java.util.ArrayList;
import java.util.List;

public class IntegerBuffer {

    private List<Integer> integers = new ArrayList<>();
    private boolean noValues = false;

    public IntegerBuffer() {
        noValues = true;
    }

    public IntegerBuffer(int[] ints) {
        for (int i : ints) {
            integers.add(i);
        }
    }

    public IntegerBuffer(List<Integer> integers) {
        for (int i : integers) {
            this.integers.add(i);
        }
    }

    public void initialze(int[] ints) {
        if (!noValues) return;
        for (int i : ints) {
            integers.add(i);
        }
        noValues = false;
    }

    public void initialze(List<Integer> integers) {
        if (!noValues) return;
        for (int i : integers) {
            this.integers.add(i);
        }
        noValues = false;
    }

    public boolean hasNext() {
        return !integers.isEmpty();
    }

    public Integer next() {
        if (hasNext()) {
            return integers.remove(0);
        } else {
            return null;
        }
    }

    public List<Integer> next(int size) {
        if (!hasNext()) {
            return null;
        }
        List<Integer> integers = new ArrayList<>();
        while (size > 0) {
            if (hasNext()) {
                integers.add(integers.remove(0));
            } else {
                break;
            }
            size--;
        }
        return integers;
    }

    public List<Integer> all() {
        return next(Integer.MAX_VALUE);
    }

    public IntegerBuffer nextAsBuffer(int size) {
        List<Integer> integers = next(size);
        return new IntegerBuffer(integers);
    }

    public Integer fold() {
        return fold('+');
    }

    public Integer fold(char operation) {
        int i = 0;
        while (hasNext()) {
            if (operation == '-') {
                i -= next();
            } else if (operation == '*') {
                i *= next();
            } else if (operation == '/') {
                i /= next();
            } else {
                i += next();
            }
        }
        return i;
    }
}
