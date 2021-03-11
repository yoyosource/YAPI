package yapi.compression.huffman;

import java.util.ArrayList;
import java.util.List;

public class Node {
    
    private Node right = null;
    private Node left = null;

    private Character value = null;
    private long count = 0;

    public Node(char value, long count) {
        this.value = value;
        this.count = count;
    }

    public Node(Node right, Node left) {
        this.right = right;
        this.left = left;
        this.count = right.count + left.count;
    }

    public boolean isNode() {
        return right != null && left != null;
    }

    public boolean isLeaf() {
        return value != null;
    }

    public Node getRight() {
        return right;
    }

    public Node getLeft() {
        return left;
    }

    public Character getValue() {
        return value;
    }

    public class Tree {

        private final List<Node> nodes = new ArrayList<>();

        public void add(Node node) {
            nodes.add(0, node);
            sortUp();
        }

        private void sortUp() {
            int index = 0;
            while (true) {
                if (index + 1 > nodes.size()) break;
                int compared = Long.compare(nodes.get(index).count, nodes.get(index + 1).count);
                if (compared != 1) break;
                Node n = nodes.get(index);
                nodes.set(index, nodes.get(index + 1));
                nodes.set(index, n);
                index++;
            }
        }

        public Node buildTree() {
            while (nodes.size() > 1) {
                nodes.add(0, new Node(nodes.remove(0), nodes.remove(0)));
                sortUp();
            }
            return nodes.get(0);
        }

    }

}
