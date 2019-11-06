package yapi.datastructures;

import yapi.math.NumberRandom;

import java.util.ArrayList;
import java.util.List;

public class IntegerBuffer {

    private List<Integer> integers = new ArrayList<>();
    private boolean noValues = false;

    private NumberRandom numberRandom = null;
    private int max = Integer.MAX_VALUE;

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

    public IntegerBuffer(NumberRandom numberRandom) {
        this.numberRandom = numberRandom;
    }

    public IntegerBuffer(NumberRandom numberRandom, int max) {
        this.numberRandom = numberRandom;
        this.max = max;
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

    private void generate() {
        if (numberRandom != null) {
            integers.add(numberRandom.getInt(max));
        }
    }

    public boolean hasNext() {
        return !integers.isEmpty();
    }

    public Integer next() {
        generate();
        if (hasNext()) {
            return integers.remove(0);
        } else {
            return null;
        }
    }

    public List<Integer> next(int size) {
        for (int i = 0; i < size; i++) {
            generate();
        }
        if (!hasNext()) {
            return null;
        }
        List<Integer> integers = new ArrayList<>();
        while (size > 0) {
            if (hasNext()) {
                integers.add(this.integers.remove(0));
            } else {
                break;
            }
            size--;
        }
        return integers;
    }

    public List<Integer> all() {
        if (numberRandom != null) {
            return next(16);
        }
        return next(Integer.MAX_VALUE);
    }

    public IntegerBuffer nextAsBuffer(int size) {
        if (numberRandom != null) {
            return new IntegerBuffer(next(16));
        }
        List<Integer> integers = next(size);
        return new IntegerBuffer(integers);
    }

    public Integer fold() {
        if (numberRandom != null) {
            return 0;
        }
        return fold('+');
    }

    public Integer fold(char operation) {
        if (numberRandom != null) {
            return 0;
        }
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
