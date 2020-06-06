// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.tuples;

public class ImmutableQuad<A, B, C, D> extends Quad<A, B, C, D> {

    private ImmutableQuad() {

    }

    public ImmutableQuad(A a, B b, C c, D d) {
        super(a, b, c, d);
    }

    @Override
    public void setA(A a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setB(B b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setC(C c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setD(D d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "ImmutableQuad{" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + "}";
    }
}