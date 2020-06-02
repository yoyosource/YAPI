// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.math.NumberRandom;
import yapi.sorting.hook.SortingHook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class YSort<T> {

    private T[] ts = null;
    private Comparator<T> comparator = null;

    private SortingHook<T> sortingHook = null;

    public YSort(T... ts) {
        this.ts = ts;
    }

    public YSort() {

    }

    public void setHook(SortingHook<T> sortingHook) {
        this.sortingHook = sortingHook;
    }

    private void swap(int i, int j) {
        T tmp = ts[i];
        ts[i] = ts[j];
        ts[j] = tmp;
    }

    public void sort(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        this.comparator = comparator;
        ySort(ts, ts.length - 1);
    }

    public void sortReversed(Comparator<T> comparator) {
        sort(comparator);
        reverse();
    }

    public void reverse() {
        if (ts.length <= 1) {
            return;
        }

        for (int i = 0; i < ts.length / 2; i++) {
            swap(i, ts.length - i - 1);
        }
    }

    public T[] getSortedArray() {
        return ts;
    }

    public void setArray(T... ts) {
        if (this.ts == null) {
            this.ts = ts;
        }
    }

    private class Batch {

        private int start;
        private int end;

        private boolean ascending = false;

        public Batch(int start, int end) {
            this.start = start;
            this.end = end;
            if (start < end) {
                ascending = true;
            }
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int get() {
            if (ascending && start == end) {
                return -1;
            } else {
                if (start < end) {
                    return -1;
                }
            }
            return start;
        }

        public void update() {
            if (start > end) {
                start--;
            } else {
                start++;
            }
        }

        public int length() {
            return start > end ? start - end + 1 : end - start;
        }

        @Override
        public String toString() {
            return "Batch{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }

    }

    private void ySort(T[] ts, int length) {
        int current = 0;
        int index = 0;
        int indexLength = 0;

        List<Batch> batches = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int compare = comparator.compare(ts[i], ts[i + 1]);
            if (compare > 0) {
                if (current != 1) {
                    if (indexLength != 0) {
                        batches.add(new Batch(index, index + indexLength));
                    }
                    index = i;
                    indexLength = 1;
                    current = 1;
                } else {
                    indexLength++;
                }
            } else if (compare < 0) {
                if (current != 2) {
                    if (indexLength != 0) {
                        batches.add(new Batch(index + indexLength, index));
                    }
                    index = i;
                    indexLength = 1;
                    current = 2;
                } else {
                    indexLength++;
                }
            } else {
                indexLength++;
            }
        }
        if (current == 2) {
            batches.add(new Batch(index + indexLength, index));
        } else {
            batches.add(new Batch(index, index + indexLength));
        }

        System.out.println(batches);
        System.out.println(Arrays.toString(ts));
        for (int i = 0; i < batches.size() - 1; i++) {
            Batch b = merge(ts, batches.get(i), batches.get(i + 1));
            batches.set(i + 1, b);
            System.out.println();
            System.out.println(batches);
            System.out.println(Arrays.toString(ts));
        }
    }

    private Batch merge(T[] ts, Batch b1, Batch b2) {
        int length = b1.length() + b2.length();
        T[] nTS = (T[])new Object[length];

        int index = b1.getStart();

        for (int i = 0; i < length; i++) {
            int i1 = b1.get();
            int i2 = b2.get();

            System.out.println(i1 + " " + i2);
            if (i1 == -1 && i2 == -1) {
                break;
            }

            if (i1 == -1) {
                nTS[i] = ts[b2.get()];
                b2.update();
                continue;
            }
            if (i2 == -1) {
                nTS[i] = ts[b1.get()];
                b1.update();
                continue;
            }

            int compare = comparator.compare(ts[i1], ts[i2]);
            if (compare > 0) {
                nTS[i] = ts[b2.get()];
                b2.update();
            } else {
                nTS[i] = ts[b1.get()];
                b1.update();
            }
        }
        System.out.println(Arrays.toString(nTS));

        for (int i = 0; i < length; i++) {
            ts[i + index] = nTS[i];
        }

        return new Batch(0, b2.getEnd());
    }

    public static void main(String[] args) {
        //YSort<Integer> ySort = new YSort<>(3, 5, 4, 3, 4, 6, 6, 9, 6, 8);
        //YSort<Integer> ySort = new YSort<>(7, 0, 7, 6, 1, 8, 5, 6, 0, 2);
        YSort<Integer> ySort = new YSort<>(0, 3, 4, 9, 4, 7, 8, 4, 3, 6);
        //YSort<Integer> ySort = new YSort<>(createIntArray(10, 10));
        ySort.sort(Integer::compareTo);
        System.out.println(Arrays.toString(ySort.getSortedArray()));
    }

    private static Integer[] createIntArray(int size, int constraint) {
        NumberRandom numberRandom = new NumberRandom();
        Integer[] ints = new Integer[size];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = numberRandom.getInt(constraint);
        }
        return ints;
    }

    private static Integer[] createIntArray(int size, boolean reversed) {
        Integer[] ints = new Integer[size];
        for (int i = 0; i < ints.length; i++) {
            if (reversed) {
                ints[i] = ints.length - 1 - i;
            } else {
                ints[i] = i;
            }
        }
        return ints;
    }

}