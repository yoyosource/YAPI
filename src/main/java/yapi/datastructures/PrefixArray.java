package yapi.datastructures;

import yapi.exceptions.ArrayMutationException;
import yapi.math.RangeSimple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PrefixArray<E> {

    private E head;
    private List<E> tail = new ArrayList<>();

    private boolean mutable = true;

    public PrefixArray() {

    }

    private PrefixArray(List<E> tail) {
        if (tail.isEmpty()) return;
        head = tail.get(0);
        tail.remove(0);
        this.tail = tail;
    }

    public PrefixArray(String s) {
        List<Long> range = RangeSimple.getRange(s);
        for (int i = 0; i < range.size(); i++) {
            if (i == 0) head = (E) range.get(i);
            else tail.add((E)range.get(i));
        }
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public E get() {
        return head;
    }

    /**
     *
     * @since Version 1
     *
     * @param index
     * @return
     */
    public E get(int index) {
        if (index == 0) {
            return head;
        } else {
            return tail.get(index - 1);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param objects
     */
    public void add(List<E> objects) {
        if (!mutable) throw new ArrayMutationException();
        for (E e : objects) {
            add(e);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param objects
     */
    public void add(E... objects) {
        if (!mutable) throw new ArrayMutationException();
        for (E e : objects) {
            add(e);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param object
     */
    public void add(E object) {
        if (!mutable) throw new ArrayMutationException();
        if (head == null) {
            head = object;
        } else {
            tail.add(object);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param object
     * @param index
     */
    public void add(E object, int index) {
        if (!mutable) throw new ArrayMutationException();
        if (index == 0) {
            tail.add(0, head);
            head = object;
        } else {
            tail.add(index - 1, object);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param object
     * @return
     */
    public boolean contains(E object) {
        return head.equals(object) || tail.contains(object);
    }

    /**
     *
     * @since Version 1
     *
     * @param object
     * @return
     */
    public boolean tailContains(E object) {
        return tail.contains(object);
    }

    /**
     *
     * @since Version 1
     *
     * @param object
     */
    public void remove(E object) {
        if (!mutable) throw new ArrayMutationException();
        if (head.equals(object)) {
            if (!tail.isEmpty()) {
                head = tail.get(0);
                tail.remove(0);
            } else {
                head = null;
            }
        } else {
            tail.remove(object);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param index
     */
    public void remove(int index) {
        if (!mutable) throw new ArrayMutationException();
        if (index == 0) {
            head = tail.get(0);
            tail.remove(0);
        } else {
            tail.remove(index - 1);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public boolean isNotEmpty() {
        return head != null;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public int size() {
        if (head == null) {
            return 0;
        }
        return tail.size() + 1;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public int length() {
        return size();
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public int tailSize() {
        return tail.size();
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public int tailLength() {
        return tail.size();
    }

    /**
     *
     * @since Version 1
     *
     * @param c
     */
    public void sort(Comparator<? super E> c) {
        if (!mutable) throw new ArrayMutationException();
        tail.sort(c);
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public Object fold() {
        return fold('+');
    }

    /**
     *
     * @since Version 1
     *
     * @param operation
     * @return
     */
    public Object fold(char operation) {
        if (head == null) {
            return 0;
        }
        if (head instanceof Integer) {
            return foldInteger(operation);
        }
        if (head instanceof Double) {
            return foldDouble(operation);
        }
        if (head instanceof Float) {
            return foldFloat(operation);
        }
        if (head instanceof Long) {
            return foldLong(operation);
        }
        return 0;
    }

    /**
     *
     * @since Version 1
     *
     * @param operation
     * @return
     */
    public Integer foldInteger(char operation) {
        if (head == null) {
            return 0;
        }
        if (!(head instanceof Integer)) {
            return 0;
        }
        if (operation == '-') {
            return (int)head - new PrefixArray<>(tail).foldInteger(operation);
        } else if (operation == '*') {
            return (int)head * new PrefixArray<>(tail).foldInteger(operation);
        } else if (operation == '/') {
            return (int)head / new PrefixArray<>(tail).foldInteger(operation);
        } else {
            return (int)head + new PrefixArray<>(tail).foldInteger(operation);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param operation
     * @return
     */
    public Double foldDouble(char operation) {
        if (head == null) {
            return 0.0;
        }
        if (!(head instanceof Double)) {
            return 0.0;
        }
        if (operation == '-') {
            return (double)head - new PrefixArray<>(tail).foldDouble(operation);
        } else if (operation == '*') {
            return (double)head * new PrefixArray<>(tail).foldDouble(operation);
        } else if (operation == '/') {
            return (double)head / new PrefixArray<>(tail).foldDouble(operation);
        } else {
            return (double)head + new PrefixArray<>(tail).foldDouble(operation);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param operation
     * @return
     */
    public Float foldFloat(char operation) {
        if (head == null) {
            return 0F;
        }
        if (!(head instanceof Float)) {
            return 0F;
        }
        if (operation == '-') {
            return (float)head - new PrefixArray<>(tail).foldFloat(operation);
        } else if (operation == '*') {
            return (float)head * new PrefixArray<>(tail).foldFloat(operation);
        } else if (operation == '/') {
            return (float)head / new PrefixArray<>(tail).foldFloat(operation);
        } else {
            return (float)head + new PrefixArray<>(tail).foldFloat(operation);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param operation
     * @return
     */
    public Long foldLong(char operation) {
        if (head == null) {
            return 0L;
        }
        if (!(head instanceof Long)) {
            return 0L;
        }
        if (operation == '-') {
            return (long)head - new PrefixArray<>(tail).foldLong(operation);
        } else if (operation == '*') {
            return (long)head * new PrefixArray<>(tail).foldLong(operation);
        } else if (operation == '/') {
            return (long)head / new PrefixArray<>(tail).foldLong(operation);
        } else {
            return (long)head + new PrefixArray<>(tail).foldLong(operation);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public boolean isMutable() {
        return mutable;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public boolean isImmutable() {
        return !mutable;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public PrefixArray<E> makeImmutable() {
        PrefixArray<E> prefixArray = new PrefixArray<>();
        prefixArray.head = this.head;
        prefixArray.tail = this.tail;
        prefixArray.mutable = false;
        return prefixArray;
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public PrefixArray<E> copy() {
        PrefixArray<E> prefixArray = new PrefixArray<>();
        prefixArray.head = this.head;
        prefixArray.tail = this.tail;
        return prefixArray;
    }

    @Override
    public String toString() {
        return "PrefixArray{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrefixArray)) return false;
        PrefixArray<?> that = (PrefixArray<?>) o;
        return Objects.equals(head, that.head) &&
                Objects.equals(tail, that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail);
    }

}
