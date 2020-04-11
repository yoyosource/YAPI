// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {

    private int count = 1;

    private HuffmanNode parent = null;
    private boolean right;

    private HuffmanNode h1;
    private HuffmanNode h2;

    private char c = '\u0000';

    public HuffmanNode(char c) {
        this.c = c;
    }

    public HuffmanNode(HuffmanNode h1, HuffmanNode h2) {
        h1.parent = this;
        h2.parent = this;
        h2.right = true;

        this.h1 = h1;
        this.h2 = h2;

        count += h1.count + h2.count - 1;
    }

    public char getChar() {
        return c;
    }

    public void increment() {
        if (c != '\u0000') {
            count++;
        }
    }

    @Override
    public String toString() {
        if (c != '\u0000') {
            if (c == '\n') {
                return "\\n";
            }
            if (c == '\t') {
                return "\\t";
            }
            if (c == '\b') {
                return "\\b";
            }
            if (c == '(' || c == ')' || c == '\\') {
                return "\\" + c;
            }
            return c + "";
        }
        return "(" + h1 + h2 + ")";
    }

    public String decode(char c) {
        if (this.c != c) {
            return "";
        }

        HuffmanNode currentNode = this;
        StringBuilder st = new StringBuilder();
        while (currentNode != null) {
            if (currentNode.right) {
                st.append("1");
            } else {
                st.append("0");
            }
            currentNode = currentNode.parent;
        }
        return st.toString();
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(count, o.count);
    }

}