// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class YSort<T> implements Sort<T> {

    private T[] ts = null;
    private Comparator<T> comparator;

    private static boolean print = false;

    public YSort(T... ts) {
        this.ts = ts;
    }

    public static <T> T[] sort(Comparator<T> comparator, T... ts) {
        YSort<T> ySort = new YSort<>(ts);
        ySort.sort(comparator);
        return ySort.getArray();
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (ts == null) {
            return;
        }

        this.comparator = comparator;
        sort();
    }

    @Override
    public T[] getArray() {
        return ts;
    }

    private class Batch {

        private int head;
        private int tail;
        private int index = 0;

        private boolean reverse = false;

        public Batch(int head, int tail) {
            this.head = head;
            this.tail = tail;
            if (head > tail) {
                reverse = true;
            }
        }

        public int getIndex() {
            if (!reverse) {
                if (print) {
                    System.out.println("NON REVERSE");
                    System.out.println("Index:" + index + " Head:" + head + " Tail:" + tail);
                    System.out.println("Head+Index:" + (head + index) + " Head+Index>Tail-1:" + (head + index > tail - 1));
                }
                if (head + index > tail) return -1;
                return head + index;
            } else {
                if (print) {
                    System.out.println("REVERSE");
                    System.out.println("Index:" + index + " Head:" + head + " Tail:" + tail);
                    System.out.println("Tail-Index:" + (tail - index) + " Tail-Index<Head:" + (tail - index < head));
                }
                if (head - index < tail) return -1;
                return head - index;
            }
        }

        public void update() {
            index++;
        }

        public int length() {
            if (reverse) {
                return head - tail + 1;
            } else {
                return tail - head + 1;
            }
        }

        public int getTail() {
            return this.tail;
        }

        public int getHead() {
            return this.head;
        }

        public int getStart() {
            if (reverse) {
                return tail;
            }
            return head;
        }

        public int getEnd() {
            if (reverse) {
                return head;
            }
            return tail;
        }

        public Batch reset(int head, int tail) {
            this.head = head;
            this.tail = tail;
            reverse = false;
            if (head > tail) {
                reverse = true;
            }
            this.index = 0;
            return this;
        }

        @Override
        public String toString() {
            return "{" + head + "->" + tail + "[" + index + "]}";
        }

        public String toString(T[] ts) {
            StringBuilder st = new StringBuilder();
            st.append("[");
            int length = length();
            int cIndex = reverse ? tail : head;
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    st.append(", ");
                }
                st.append(ts[cIndex + i]);
            }
            st.append("]");
            return st.toString();
        }

    }

    private void sort() {
        LinkedList<Batch> batches = new LinkedList<>();

        int lastSort = -2;
        int index = 0;
        int batchLength = 0;
        int length = ts.length;

        if (length < 2) {
            return;
        }

        for (int i = 1; i < length; i++) {
            int compare = comparator.compare(ts[i - 1], ts[i]);
            if (lastSort == -2) {
                lastSort = compare;
            }
            if (compare != lastSort) {
                addBatch(index, batchLength, lastSort, batches);
                index = i;
                batchLength = 0;
                lastSort = -2;
            } else {
                batchLength++;
            }
        }

        addBatch(index, batchLength, lastSort, batches);

        while (batches.size() > 1) {
            output(batches);
            Batch batch1 = batches.removeFirst();
            Batch batch2 = batches.removeFirst();
            if (batch1.getStart() > batch2.getStart()) {
                batches.add(merge(batch2, batch1));
            } else {
                batches.add(merge(batch1, batch2));
            }
        }
        output(batches);
        Batch batch = batches.removeFirst();
        if (batch.reverse) {
            for (int i = 0; i < ts.length / 2; i++) {
                T t = ts[i];
                ts[i] = ts[ts.length - i - 1];
                ts[ts.length - i - 1] = t;
            }
        }
    }

    private void output(LinkedList<Batch> batches) {
        if (print) {
            System.out.println(batches.stream().map(b -> b.toString(ts)).collect(Collectors.joining(" ")));
        }
    }

    private void addBatch(int index, int length, int compare, LinkedList<Batch> batches) {
        if (compare > 0) {
            batches.add(new Batch(index + length, index));
        } else {
            batches.add(new Batch(index, index + length));
        }
    }

    private Batch merge(Batch batch1, Batch batch2) {
        T[] array = (T[]) new Object[batch1.length() + batch2.length()];
        int index = 0;

        while (index < array.length) {
            int i1 = batch1.getIndex();
            int i2 = batch2.getIndex();

            int compare;
            if (i1 == -1) {
                compare = 1;
            } else if (i2 == -1) {
                compare = -1;
            } else {
                compare = comparator.compare(ts[i1], ts[i2]);
            }

            if (compare < 0) {
                array[index] = ts[batch1.getIndex()];
                batch1.update();
            } else {
                array[index] = ts[batch2.getIndex()];
                batch2.update();
            }
            index++;
        }

        mergeBack(batch1.getStart(), array);
        return returnBatch(batch1, batch2);
    }

    private Batch returnBatch(Batch batch1, Batch batch2) {
        return batch1.reset(batch1.getStart(), batch2.getEnd());
    }

    private void mergeBack(int start, T[] array) {
        for (int i = 0; i < array.length; i++) {
            ts[start + i] = array[i];
        }
    }

    @Override
    public void sortReversed(Comparator<T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArray(T... ts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHook(SortingHook<T> sortingHook) {
        throw new UnsupportedOperationException();
    }

}