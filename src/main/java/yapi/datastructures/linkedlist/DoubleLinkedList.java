// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.linkedlist;

import java.util.Iterator;

public class DoubleLinkedList<T> implements Iterable<T> {

    private static long currentID = 0;

    public static void main(String[] args) {
        DoubleLinkedList<Integer> integerDoubleLinkedList = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            integerDoubleLinkedList.add(i);
        }
        System.out.println(integerDoubleLinkedList);
        integerDoubleLinkedList.moveToLast(0);
        System.out.println(integerDoubleLinkedList);
        integerDoubleLinkedList.moveToFront(integerDoubleLinkedList.size - 1);
        System.out.println(integerDoubleLinkedList);
        integerDoubleLinkedList.reverse();
        System.out.println(integerDoubleLinkedList);
        integerDoubleLinkedList.reverse();
        System.out.println(integerDoubleLinkedList);
    }

    private Node<T> head;
    private Node<T> tail;

    private int size = 0;

    private final long id = currentID++;

    private void boundaryCheck(int index) {
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void validNode(Node<T> node) {
        if (node.id == -1) {
            node.id = id;
        }
        if (node.id != id) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.prev = current.next = null;
            current.value = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    public void reverse() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.next = current.prev;
            current.prev = next;
            current = next;
        }
        Node<T> node = head;
        head = tail;
        tail = node;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isNotEmpty() {
        return size != 0;
    }

    public void add(T value) {
        addLast(value);
    }

    public void addLast(T value) {
        if (size == 0) {
            head = tail = new Node<>(value);
        } else {
            tail.next = new Node<>(value);
            tail.next.prev = tail;
            tail = tail.next;
        }
        tail.id = id;

        size++;
    }

    public void addLast(Node<T> node) {
        validNode(node);
        if (size == 0) {
            head = tail = node;
        } else {
            tail.next = node;
            tail.next.prev = tail;
            tail = tail.next;
        }

        size++;
    }

    public void addFirst(T value) {
        if (size == 0) {
            head = tail = new Node<>(value);
        } else {
            head.prev = new Node<>(value);
            head.next.prev = head;
            head = head.prev;
        }
        head.id = id;

        size++;
    }

    public void addFirst(Node<T> node) {
        validNode(node);
        if (size == 0) {
            head = tail = node;
        } else {
            head.prev = node;
            head.prev.next = head;
            head = head.prev;
        }

        size++;
    }

    public Node<T> removeLast() {
        if (size == 0) {
            return null;
        }

        Node<T> rt = tail;
        Node<T> current = tail.prev;
        tail.prev.next = null;
        tail.prev = null;

        tail = current;
        size--;

        if (size == 0) {
            tail = null;
        } else {
            head.prev = null;
        }
        rt.id = -1;
        return rt;
    }

    public Node<T> removeFirst() {
        if (size == 0) {
            return null;
        }

        Node<T> rt = head;
        Node<T> current = head.next;
        head.next.prev = null;
        head.next = null;

        head = current;
        size--;

        if (size == 0) {
            head = null;
        } else {
            tail.next = null;
        }
        rt.id = -1;
        return rt;
    }

    public void remove(Node<T> node) {
        validNode(node);
        if (node.prev == null) {
            removeFirst();
            return;
        }
        if (node.next == null) {
            removeLast();
            return;
        }

        node.next.prev = node.prev;
        node.prev.next = node.next;

        size--;
        node.prev = node.next = null;
        node.id = -1;
    }

    public Node<T> removeAt(int index) {
        boundaryCheck(index);
        Node<T> node = getNode(index);
        remove(node);
        node.id = -1;
        return node;
    }

    public void moveToFront(Node<T> node) {
        validNode(node);
        remove(node);
        addFirst(node);
    }

    public void moveToFront(int index) {
        boundaryCheck(index);
        addFirst(removeAt(index));
    }

    public void moveToFront() {
        addFirst(removeLast());
    }

    public void moveToLast(Node<T> node) {
        validNode(node);
        remove(node);
        addLast(node);
    }

    public void moveToLast(int index) {
        boundaryCheck(index);
        addLast(removeAt(index));
    }

    public void moveToLast() {
        addLast(removeFirst());
    }

    public void insertAfter(int index, Node<T> value) {
        validNode(value);
        boundaryCheck(index);
        insertAfter(getNode(index), value);
    }

    private void insertAfter(Node<T> node, Node<T> value) {
        validNode(value);
        if (node.next == null) {
            addLast(value);
            return;
        }

        Node<T> nextNode = node.next;
        node.next = nextNode.prev = value;
        value.next = nextNode;
        value.prev = node;
        size++;
    }

    public void insertAfter(int index, T value) {
        boundaryCheck(index);
        insertAfter(getNode(index), value);
    }

    private void insertAfter(Node<T> node, T value) {
        if (node.next == null) {
            addLast(value);
            return;
        }

        Node<T> nextNode = node.next;
        Node<T> current = new Node<>(value);
        node.next = nextNode.prev = current;
        current.next = nextNode;
        current.prev = node;
        size++;
    }

    public void insertBefore(int index, Node<T> value) {
        boundaryCheck(index);
        insertBefore(getNode(index), value);
    }

    private void insertBefore(Node<T> node, Node<T> value) {
        validNode(value);
        if (node.prev == null) {
            addFirst(value);
            return;
        }

        Node<T> prevNode = node.prev;
        node.prev = prevNode.next = value;
        value.prev = prevNode;
        value.next = node;
        size++;
    }

    public void insertBefore(int index, T value) {
        boundaryCheck(index);
        insertBefore(getNode(index), value);
    }

    private void insertBefore(Node<T> node, T value) {
        if (node.prev == null) {
            addFirst(value);
            return;
        }

        Node<T> prevNode = node.prev;
        Node<T> current = new Node<>(value);
        node.prev = prevNode.next = current;
        current.prev = prevNode;
        current.next = node;
        size++;
    }

    public Node<T> getNode(int index) {
        boundaryCheck(index);
        if (index > size / 2) {
            index = size - index - 1;
            Node<T> node = tail;
            while (index > 0) {
                node = node.prev;
                index--;
            }
            return node;
        } else {
            Node<T> node = head;
            while (index > 0) {
                node = node.next;
                index--;
            }
            return node;
        }
    }

    public T getValue(int index) {
        boundaryCheck(index);
        return getNode(index).value;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T data = current.value;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("DoubleLinkedList{");
        st.append("size=").append(size);
        st.append(", id=").append(id);
        st.append(", values={");
        boolean b = false;
        Node<T> current = head;
        String last = "";
        while (current != null) {
            if (b) {
                if (current.prev != null) {
                    st.append("<");
                }
                st.append("-");
                st.append(last);
            }
            st.append(current.value);
            last = current.next != null ? ">" : "";
            current = current.next;
            b = true;
        }
        st.append("}}");
        return st.toString();
    }

}