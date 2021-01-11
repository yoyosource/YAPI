package yapi.datastructures;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TapeSet<T> implements Set<T> {

    private final Map<T, Node<T>> nodeMap = new HashMap<>();
    private Node<T> last = null;
    private Node<T> currentIterator = null;

    private class Node<T> {

        private Node<T> next = null;
        private Node<T> prev = null;
        private T value;

        public Node(T value) {
            this.value = value;
        }

        private void unlink() {
            if (!itself()) {
                next.prev = prev;
                prev.next = next;
            }
            next = null;
            prev = null;
            value = null;
        }

        private void link(Node<T> prev) {
            this.prev = prev;
            next = prev.next;
            next.prev = this;
            prev.next = this;
        }

        private boolean itself() {
            return next == prev && next == this;
        }

    }

    public int size() {
        return nodeMap.size();
    }

    public boolean isEmpty() {
        return nodeMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return nodeMap.containsKey(o);
    }

    @Override
    public boolean add(T t) {
        if (nodeMap.containsKey(t)) return false;
        Node<T> node = new Node<>(t);
        nodeMap.put(t, node);
        if (last == null) {
            last = node;
            currentIterator = node;
        } else {
            node.link(last);
        }
        return true;
    }

    @Override
    public boolean remove(Object t) {
        if (!nodeMap.containsKey(t)) return false;
        Node<T> node = nodeMap.get(t);
        if (node.itself()) {
            last = null;
            currentIterator = null;
        } else if (node == currentIterator) {
            nextIteratorNode();
        }
        node.unlink();
        nodeMap.remove(t);
        return true;
    }

    private void nextIteratorNode() {
        currentIterator = currentIterator.next;
    }

    public T next() {
        if (currentIterator == null) return null;
        T value = currentIterator.value;
        nextIteratorNode();
        return value;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(this::remove);
        return true;
    }

    @Override
    public void clear() {
        nodeMap.forEach((t, n) -> remove(n));
        nodeMap.clear();
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<T> stream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<T> parallelStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

}
