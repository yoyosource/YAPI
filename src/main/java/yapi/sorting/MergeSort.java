// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import java.util.Arrays;
import java.util.Comparator;

public class MergeSort<T> implements Sort<T> {

    private T[] ts = null;
    private Comparator<T> comparator = null;
    private boolean reverseSort = false;

    private SortingHook<T> sortingHook = null;

    public MergeSort(T[] ts) {

    }

    public MergeSort() {

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

        reverseSort = false;
        this.comparator = comparator;
        mergeSort(ts, 0, ts.length - 1);
    }

    @Override
    public void sortReversed(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        reverseSort = true;
        this.comparator = comparator;
        mergeSort(ts, 0, ts.length - 1);
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

    private void mergeSort(T[] ts, int l, int r) {
        if (ts == null) {
            return;
        }

        if (l < r) {
            int q = (l + r) / 2;

            mergeSort(ts, l, q);
            mergeSort(ts, q + 1, r);
            mergeSort(ts, l, q, r);
        }
    }

    private void mergeSort(T[] ts, int l, int q, int r) {
        int j;
        T[] nTS = Arrays.copyOfRange(ts, l, q + 1);
        for (j = q + 1; j <= r; j++) {
            nTS[r + q + 1 - j] = ts[j];
        }
        int i = l;
        j = r;

        if (reverseSort) {
            for (int k = l; k <= r; k++) {
                if (comparator.compare(nTS[i], nTS[j]) >= 0) {
                    ts[k] = nTS[i];
                    i++;
                } else {
                    ts[k] = nTS[j];
                    j--;
                }
            }
        } else {
            for (int k = l; k <= r; k++) {
                if (comparator.compare(nTS[i], nTS[j]) <= 0) {
                    ts[k] = nTS[i];
                    i++;
                } else {
                    ts[k] = nTS[j];
                    j--;
                }
            }
        }

        if (sortingHook != null) {
            sortingHook.hook(copy());
        }
    }

    private T[] copy() {
        return Arrays.copyOf(ts, ts.length);
    }

}