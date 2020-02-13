// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LongTree {

    private long l;
    private List<LongTree> nodes = new ArrayList<>();

    /**
     *
     * @since Version 1.1
     *
     * @param l
     */
    public LongTree(long l) {
        this.l = l;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param longTree
     */
    public void add(LongTree longTree) {
        nodes.add(longTree);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param longTrees
     */
    public void add(LongTree[] longTrees) {
        for (LongTree longTree : longTrees) {
            if (longTree != null) {
                nodes.add(longTree);
            }
        }
    }

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public List<LongTree> getNodes() {
        return nodes;
    }

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public long getL() {
        return l;
    }

    @Override
    public String toString() {
        return "LongTree{" +
                "l=" + l +
                ", nodes=" + nodes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongTree)) return false;
        LongTree longTree = (LongTree) o;
        return l == longTree.l;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l);
    }

}