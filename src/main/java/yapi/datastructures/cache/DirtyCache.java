// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.cache;

import java.util.HashMap;
import java.util.Map;

public class DirtyCache<K, V> {

    private int size = 100;

    private Node<V> head = null;
    private Node<V> tail = null;

    private Map<K, Node<V>> nodeCache = new HashMap<>();

    private Cleaner cleaner;

    private class Node<V> {
        private V data;
        private K key;
        private boolean dirty;
        private Node<V> prev, next;

        public Node(V data, K key, Node<V> prev, Node<V> next) {
            this.data = data;
            this.key = key;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public abstract class Cleaner<K, V> {

        public abstract void clean(boolean dirty, K key, V currentValue);

    }

    public DirtyCache(Cleaner<K, V> cleaner) {
        this.cleaner = cleaner;
    }

    public DirtyCache(int size, Cleaner<K, V> cleaner) {
        this.size = size;
        this.cleaner = cleaner;
    }

    public boolean hasValue(K key) {
        return nodeCache.containsKey(key);
    }

    public V getValue(K key) {
        if (nodeCache.containsKey(key)) {
            touch(nodeCache.get(key), null);
        }
        return nodeCache.get(key).data;
    }

    public void cache(K key, V value) {
        add(key, value);
    }

    public void add(K key, V value) {
        if (nodeCache.containsKey(key)) {
            touch(nodeCache.get(key), value);
            return;
        }
        while (nodeCache.size() >= size) {
            remove();
        }
        Node<V> node = addLast(key, value);
        nodeCache.put(key, node);
    }

    private Node<V> addLast(K key, V value) {
        if (head == null && tail == null) {
            head = tail = new Node<>(value, key, null, null);
            return head;
        } else {
            tail.next = new Node<>(value, key, tail, null);
            tail = tail.next;
            return tail;
        }
    }

    private void addLast(Node<V> node) {
        if (head == null && tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = tail.next;
        }
    }

    private void touch(Node<V> node, V newValue) {
        if (node.prev == null) {
            if (node.next != null) {
                node.next.prev = null;
            }
            addLast(node);
            return;
        }
        if (node.next == null) {
            return;
        }

        node.next.prev = node.prev;
        node.prev.next = node.next;

        node.prev = node.next = null;

        addLast(node);
        if (newValue != null) {
            node.data = newValue;
            node.dirty = true;
        }
    }

    private void remove() {
        if (head == null) {
            return;
        }

        Node<V> node = nodeCache.get(head.key);
        cleaner.clean(node.dirty, node.key, node.data);
        nodeCache.remove(head.key);
        head = head.next;
        head.prev = null;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("Cache{");
        st.append("size=").append(size);
        st.append(", ").append("actualSize=").append(nodeCache.size());
        st.append(", ").append("values={");
        boolean b = false;
        Node<V> current = head;
        while (current != null) {
            if (b) {
                st.append(", ");
            }
            st.append(current.key).append("=").append(current.data);
            current = current.next;
            b = true;
        }
        st.append("}");
        st.append(", ").append("cache=").append(nodeCache);
        st.append("}");
        return st.toString();
    }

}