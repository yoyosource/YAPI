// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.circular;

import java.util.*;

public class CircularBuffer<T> implements Iterable<T> {

    private T[] buf;
    private int start = 0;

    private boolean allowNull = false;

    public CircularBuffer(int length) {
        buf = (T[]) new Object[length];
    }

    public CircularBuffer(int length, boolean allowNull) {
        if (allowNull) {
            buf = (T[]) new BufferEntry[length];
        } else {
            buf = (T[]) new Object[length];
        }
        this.allowNull = allowNull;
    }

    public void add(T t) {
        if (allowNull) {
            buf[start++] = (T)new BufferEntry<T>().setT(t);
        } else {
            if (t == null) {
                throw new NullPointerException("Null as value is not supported");
            }
            buf[start++] = t;
        }
        start = start % buf.length;
    }

    public T remove() {
        int index = start;
        start = (start + buf.length - 1) % buf.length;
        T t = buf[index];
        buf[index] = null;
        return t;
    }

    public void clear() {
        int length = buf.length;
        if (allowNull) {
            buf = (T[]) new BufferEntry[length];
        } else {
            buf = (T[]) new Object[length];
        }
        start = 0;
    }

    public List<T> list() {
        List<T> list = new ArrayList<>();
        int i = start;
        i = (i + buf.length - 1) % buf.length;
        int length = 0;
        while (length != buf.length) {
            list.add(buf[i]);
            i = (i + buf.length - 1) % buf.length;
            length++;
        }
        return list;
    }

    public boolean contains(T t) {
        return indexOf(t) >= 0;
    }

    public boolean contains(StringBuilder st) {
        return contains(st.toString());
    }

    public boolean contains(String s) {
        return indexOf(s) >= 0;
    }

    public int indexOf(T t) {
        int index = start;
        int length = 0;
        while (length != buf.length) {
            if (buf[index].equals(t)) {
                return length;
            }
            index = (index + buf.length - 1) % buf.length;
            length++;
        }
        return -1;
    }

    public int indexOf(StringBuilder st) {
        if (st.length() == 0) {
            return -1;
        }
        return indexOf(st.toString());
    }

    public int indexOf(String s) {
        if (s.length() == 0) {
            return -1;
        }
        return toPlainString().indexOf(s);
    }

    @Override
    public Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int index = start;
            boolean b = false;

            @Override
            public boolean hasNext() {
                if (b) {
                    return index != start && buf[index] != null;
                }
                return buf[index] != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("End of Iterator");
                }
                b = true;
                index = (index + buf.length - 1) % buf.length;
                return buf[(index + 1) % buf.length];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String toPlainString() {
        StringBuilder st = new StringBuilder();
        int index = start;
        int length = 0;
        while (length != buf.length) {
            st.append(buf[index]);
            index = (index + buf.length - 1) % buf.length;
            length++;
        }
        return st.toString();
    }

    @Override
    public String toString() {
        return "CircularBuffer{" +
                "buf=" + Arrays.toString(buf) +
                ", start=" + start +
                ", allowNull=" + allowNull +
                '}';
    }

    private class BufferEntry<T> {

        private T t = null;

        public T getT() {
            return t;
        }

        public BufferEntry setT(T t) {
            this.t = t;
            return this;
        }

        @Override
        public String toString() {
            return "BufferEntry{" +
                    "t=" + t +
                    '}';
        }
    }

}