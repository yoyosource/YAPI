package yapi.utils;

import yapi.exceptions.ArrayMutationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PrefixArray<E> {

    private E head;
    private List<E> tail;

    private boolean mutable = true;

    public PrefixArray() {
        tail = new ArrayList<>();
    }

    private PrefixArray(List<E> tail) {
        if (tail.isEmpty()) return;
        head = tail.get(0);
        tail.remove(0);
        this.tail = tail;
    }

    public E get() {
        return head;
    }

    public E get(int index) {
        if (index == 0) {
            return head;
        } else {
            return tail.get(index - 1);
        }
    }

    public void add(List<E> objects) {
        if (!mutable) throw new ArrayMutationException();
        for (E e : objects) {
            add(e);
        }
    }

    public void add(E... objects) {
        if (!mutable) throw new ArrayMutationException();
        for (E e : objects) {
            add(e);
        }
    }

    public void add(E object) {
        if (!mutable) throw new ArrayMutationException();
        if (head == null) {
            head = object;
        } else {
            tail.add(object);
        }
    }

    public void add(E object, int index) {
        if (!mutable) throw new ArrayMutationException();
        if (index == 0) {
            tail.add(0, head);
            head = object;
        } else {
            tail.add(index - 1, object);
        }
    }

    public boolean contains(E object) {
        return head.equals(object) || tail.contains(object);
    }

    public boolean tailContains(E object) {
        return tail.contains(object);
    }

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

    public void remove(int index) {
        if (!mutable) throw new ArrayMutationException();
        if (index == 0) {
            head = tail.get(0);
            tail.remove(0);
        } else {
            tail.remove(index - 1);
        }
    }

    public boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return tail.isEmpty();
    }

    public boolean isNotEmpty() {
        return head != null;
    }

    public int size() {
        if (head == null) {
            return 0;
        }
        return tail.size() + 1;
    }

    public int length() {
        return size();
    }

    public int tailSize() {
        return tail.size();
    }

    public int tailLength() {
        return tail.size();
    }

    public void sort(Comparator c) {
        if (!mutable) throw new ArrayMutationException();
        tail.sort(c);
    }

    public Integer fold() {
        return fold('+');
    }

    public Integer fold(char operation) {
        if (head == null) {
            return 0;
        }
        if (!(head instanceof Integer)) {
            return 0;
        }
        if (operation == '-') {
            return (int)head - new PrefixArray<>(tail).fold(operation);
        } else if (operation == '*') {
            return (int)head * new PrefixArray<>(tail).fold(operation);
        } else if (operation == '/') {
            return (int)head / new PrefixArray<>(tail).fold(operation);
        } else {
            return (int)head + new PrefixArray<>(tail).fold(operation);
        }
    }

    public boolean isMutable() {
        return mutable;
    }

    public boolean isImmutable() {
        return !mutable;
    }

    public PrefixArray<E> makeImmutable() {
        PrefixArray<E> prefixArray = new PrefixArray<>();
        prefixArray.head = this.head;
        prefixArray.tail = this.tail;
        prefixArray.mutable = false;
        return prefixArray;
    }

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
