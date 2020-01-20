package yapi.datastructures;

import yapi.math.NumberUtils;
import yapi.math.Vector;

import java.util.*;

public class Graph {

    private List<Node> nodes = new ArrayList<>();
    private Map<Node, int[]> positions = new HashMap<>();
    private Random r = new Random();

    private boolean output = false;

    public Graph() {
        //
    }

    public Graph(Node... nodes) {
        for (Node n : nodes) {
            this.nodes.add(n);
        }
    }

    public Node addNode(String name) {
        Node node = new Node(name);
        nodes.add(node);
        return node;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }

    public String ascii() {
        positions = new HashMap<>();

        int h = 3 * nodes.size() + 12;
        int w = 7 * nodes.size() + 28;
        if (h > 60) {
            h = 60;
        }
        if (w > 140) {
            w = 140;
        }

        char[][] chars = new char[h][w];
        for (int x = 0; x < chars.length; x++) {
            Arrays.fill(chars[x], ' ');
        }

        for (Node n : nodes) {
            tempPlace(w, h, n);
        }

        for (int i = 0; i < 1000; i++) {
            moveTillMoreSpace(chars);
        }

        place(chars);

        if (output) {
            for (Map.Entry<Node, int[]> entry : positions.entrySet()) {
                System.out.println(entry.getKey().getName() + " -> " + entry.getValue()[0] + " " + entry.getValue()[1]);
            }
        }
        return getString(chars);
    }

    private void tempPlace(int w, int h, Node n) {
        int x;
        int y;
        int tries = 0;
        if (output) {
            System.out.println(n.getName());
        }
        do {
            x = r.nextInt(w - 2 - n.getName().length()) + 1;
            y = r.nextInt(h - 3) + 1;
            tries++;
            if (tries > 1000) {
                throw new InputMismatchException("no position found");
            }
            if (output) {
                System.out.println("x: " + x + "  y: " + y);
            }
        } while ((isNear(x, y, n)));

        positions.put(n, new int[]{x, y, x + n.getName().length(), y});
    }

    private void moveTillMoreSpace(char[][] chars) {
        Map<Node, int[]> intMap = new HashMap<>();
        for (Map.Entry<Node, int[]> entry : positions.entrySet()) {
            Vector vector = new Vector(2);

            for (Map.Entry<Node, int[]> e : positions.entrySet()) {
                double p = NumberUtils.pythagoras(e.getValue()[0], e.getValue()[1], entry.getValue()[0], entry.getValue()[1]);
                if (p == 0) {
                    continue;
                }
                vector.subtractVector(new Vector((e.getValue()[0] - entry.getValue()[0]) / p, (e.getValue()[1] - entry.getValue()[1]) / p));
            }

            intMap.put(entry.getKey(), new int[]{(int)vector.get(0), (int)vector.get(1)});
        }

        for (Map.Entry<Node, int[]> entry : positions.entrySet()) {
            int[] ints = entry.getValue();
            int nx = ints[0] + intMap.get(entry.getKey())[0];
            int ny = ints[1] + intMap.get(entry.getKey())[1];
            if (nx > 1 && nx < chars[0].length - 2 - entry.getKey().getName().length()) {
                ints[0] = nx;
                ints[2] = nx + entry.getKey().getName().length();
            }
            if (ny > 1 && ny < chars.length - 2) {
                ints[1] = ny;
                ints[3] = ny;
            }
            if (isNear(entry.getValue()[0] + nx, entry.getValue()[1] + ny, entry.getKey())) {
                continue;
            }
            positions.replace(entry.getKey(), ints);
        }
    }

    private boolean isNear(int x, int y, Node n) {
        for (Map.Entry<Node, int[]> entry : positions.entrySet()) {
            if (entry.getKey().equals(n)) {
                continue;
            }
            int dx = entry.getValue()[0];
            int dy = entry.getValue()[1];

            int px = (x - dx) * (x - dx);
            int py = (y - dy) * (y - dy);
            if (Math.sqrt(px + py) < entry.getKey().getName().length() + 5) {
                if (output) {
                    System.out.println("near: " + x + " " + y + " -> " + entry.getKey().getName());
                }
                return true;
            }
        }
        return false;
    }

    private void place(char[][] chars) {
        for (Map.Entry<Node, int[]> entry : positions.entrySet()) {
            int x = entry.getValue()[0];
            int y = entry.getValue()[1];
            for (char c : entry.getKey().getName().toCharArray()) {
                chars[y][x] = c;
                x++;
            }
        }
    }

    private String getString(char[][] chars) {
        StringBuilder st = new StringBuilder();
        for (int y = 0; y < chars.length; y++) {
            if (y != 0) {
                st.append("\n");
            }
            for (int x = 0; x < chars[y].length; x++) {
                st.append(chars[y][x]);
            }
        }
        return st.toString();
    }

}
