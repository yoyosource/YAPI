// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import java.util.Arrays;
import java.util.Comparator;

public class TimSort<T> implements Sort<T> {

    private static int RUN = 32;

    public void setRUN(int run) {
        TimSort.RUN = run;
    }

    public int getRUN() {
        return RUN;
    }

    private T[] ts = null;
    private Comparator<T> comparator = null;

    private SortingHook<T> sortingHook = null;

    public TimSort(T... ts) {
        this.ts = ts;
    }

    public TimSort() {

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
        timSort(ts, ts.length - 1);
    }

    @Override
    public void sortReversed(Comparator<T> comparator) {
        sort(comparator);
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

    private void timSort(T[] ts, int n) {
        for (int i = 0; i < n; i += RUN) {
            insertionSort(ts, i, Math.min((i + 31), (n - 1)));
        }

        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                merge(ts, left, mid, right);

                if (sortingHook != null) {
                    sortingHook.hook(copy());
                }
            }
        }
    }

    private void insertionSort(T[] ts, int left, int right) {
        for (int i= left + 1; i <= right; i++) {
            T temp = ts[i];
            int j = i -1;

            while (comparator.compare(ts[j], temp) > 0 && j >= left) {
                ts[j + 1] = ts[j];
                j--;
            }

            ts[j + 1] = temp;
        }
    }

    private void merge(T[] ts, int l, int m, int r) {
        int len1 = m - l + 1;
        int len2 = r - m;

        T[] left = (T[])new Object[len1];
        T[] right = (T[])new Object[len2];

        for (int x = 0; x < len1; x++) {
            left[x] = ts[l + x];
        }
        for (int x = 0; x < len2; x++) {
            right[x] = ts[m + 1 + x];
        }

        int i = 0;
        int j = 0;
        int k = l;

        while (i < len1 && j < len2) {
            if (comparator.compare(left[i], right[i]) <= 0) {
                ts[k] = left[i];
                i++;
            } else {
                ts[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < len1) {
            ts[k] = left[i];
            k++;
            i++;
        }

        while (j < len2) {
            ts[k] = right[j];
            k++;
            j++;
        }
    }

    private T[] copy() {
        return Arrays.copyOf(ts, ts.length);
    }

}