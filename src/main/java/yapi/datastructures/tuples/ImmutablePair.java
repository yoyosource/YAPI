// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.tuples;

public class ImmutablePair<L, R> extends Pair<L, R> {

    private ImmutablePair() {

    }

    public ImmutablePair(L l, R r) {
        super(l, r);
    }

    @Override
    public void setR(R r) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setL(L l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "ImmutablePair{" + getL() + ", " + getR() + "}";
    }
}