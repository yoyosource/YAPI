// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort<T> implements Sort<T> {

    private T[] ts = null;
    private Comparator<T> comparator = null;
    private boolean reverseSort = false;

    private SortingHook<T> sortingHook = null;

    public QuickSort() {

    }

    public QuickSort(T... ts) {
        this.ts = ts;
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
        quickSort(ts, 0, ts.length - 1);
    }

    @Override
    public void sortReversed(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        reverseSort = true;
        this.comparator = comparator;
        quickSort(ts, 0, ts.length - 1);
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

    private void quickSort(T[] arr, int start, int end) {
        if (start < end) {
            int i = start;
            int j = end;

            T x = arr[(i + j) / 2];

            do {
                if (reverseSort) {
                    while (comparator.compare(arr[i], x) > 0) i++;
                    while (comparator.compare(x, arr[j]) > 0) j--;
                } else {
                    while (comparator.compare(arr[i], x) < 0) i++;
                    while (comparator.compare(x, arr[j]) < 0) j--;
                }

                if (i <= j) {
                    swap(i, j);
                    i++;
                    j--;
                }
            } while (i <= j);

            if (sortingHook != null) {
                sortingHook.hook(copy());
            }

            quickSort(arr, start, j);
            quickSort(arr, i, end);
        }
    }

    private T[] copy() {
        return Arrays.copyOf(ts, ts.length);
    }

}