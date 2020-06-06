// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.tuples;

import java.util.Objects;

public class Triple<L, M, R> {

    private L l = null;
    private M m = null;
    private R r = null;

    public Triple() {

    }

    public Triple(L l, M m, R r) {
        this.l = l;
        this.m = m;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public M getM() {
        return m;
    }

    public R getR() {
        return r;
    }

    public void setL(L l) {
        this.l = l;
    }

    public void setM(M m) {
        this.m = m;
    }

    public void setR(R r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "Triple{" + l + ", " + m + ", " + r + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triple)) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(l, triple.l) &&
                Objects.equals(m, triple.m) &&
                Objects.equals(r, triple.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, m, r);
    }

}