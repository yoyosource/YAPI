// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class BogoSort<T> implements Sort<T> {

    private T[] ts = null;
    private Comparator<T> comparator;

    private SortingHook<T> sortingHook = null;

    public BogoSort(T... ts) {

    }

    public BogoSort() {

    }

    @Override
    public void setHook(SortingHook<T> sortingHook) {
        this.sortingHook = sortingHook;
    }

    private void swap(int i, int j) {
        T tmp = ts[i];
        ts[i] = ts[j];
        ts[j] = tmp;
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        this.comparator = comparator;
        bogoSort(ts);
    }

    @Override
    public void sortReversed(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        this.comparator = comparator;
        bogoSort(ts);
        reverse();
    }

    @Override
    public void reverse() {
        if (ts.length <= 1) {
            return;
        }

        for (int i = 0; i < ts.length / 2; i++) {
            swap(i, ts.length - i - 1);
        }
    }

    @Override
    public T[] getArray() {
        return ts;
    }

    @Override
    public void setArray(T... ts) {
        if (this.ts == null) {
            this.ts = ts;
        }
    }

    private void bogoSort(T[] ts) {
        Random random = new Random();

        while (!sorted(ts)) {
            swap(random.nextInt(ts.length), random.nextInt(ts.length));

            if (sortingHook != null) {
                sortingHook.hook(copy());
            }
        }
    }

    private boolean sorted(T[] ts) {
        for (int i = 0; i < ts.length - 2; i++) {
            if (comparator.compare(ts[i], ts[i + 1]) >= 0) {
                return false;
            }
        }
        return true;
    }

    private T[] copy() {
        return Arrays.copyOf(ts, ts.length);
    }

}