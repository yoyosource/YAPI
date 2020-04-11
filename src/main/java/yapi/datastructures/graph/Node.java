// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    private static long idGiver;
    private static boolean compact = false;
    private long id;
    private String name;
    private List<Node> connections = new ArrayList<>();

    public Node(String name) {
        id = idGiver++;
        this.name = name;
    }

    public static void setCompact(boolean compact) {
        Node.compact = compact;
    }

    public String getName() {
        return name;
    }

    public List<Node> getConnections() {
        return connections;
    }

    public void addConnection(Node node) {
        connections.add(node);
    }

    public void addConnection(String name) {
        connections.add(new Node(name));
    }

    // │ ─ ┼ └ ┘ ┌ ┐ ┤ ├ ┬ ┴ •

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        if (compact) {
            st.append(name + " <" + id + ">" + " {");
        } else {
            st.append("Node{name='" + name + "', connections=[");
        }
        boolean b = false;
        for (Node node : connections) {
            if (b) {
                st.append(", ");
            }
            if (node.equals(this)) {
                if (compact) {
                    st.append("*" + node.name + " <" + node.id + ">");
                } else {
                    st.append("Node{name='" + node.name + "', recursive=[]}");
                }
            } else {
                List<Node> nodes = new ArrayList<>();
                nodes.add(this);
                st.append(node.getString(nodes));
            }
            b = true;
        }
        if (compact) {
            st.append("}");
        } else {
            st.append("]}");
        }
        return st.toString();
    }

    private String getString(List<Node> nodeList) {
        StringBuilder st = new StringBuilder();
        if (compact) {
            st.append(name + " <" + id + ">" + " {");
        } else {
            st.append("Node{name='" + name + "', connections=[");
        }
        boolean b = false;
        for (Node node : connections) {
            if (b) {
                st.append(", ");
            }
            if (node.equals(this)) {
                if (compact) {
                    st.append("*" + node.name + " <" + node.id + ">");
                } else {
                    st.append("Node{name='" + node.name + "', recursive=[]}");
                }
            } else {
                List<Node> nodes = new ArrayList<>();
                for (Node n : nodeList) {
                    nodes.add(n);
                }
                nodes.add(this);
                if (nodes.contains(node)) {
                    if (compact) {
                        st.append("*" + node.name + " <" + node.id + ">");
                    } else {
                        st.append("Node{name='" + node.name + "', recursive=[]}");
                    }
                } else {
                    st.append(node.getString(nodes));
                }
            }
            b = true;
        }
        if (compact) {
            st.append("}");
        } else {
            st.append("]}");
        }
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return id == node.id &&
                Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}