// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.tuples;

import java.util.Objects;

public class Quad<A, B, C, D> {

    private A a;
    private B b;
    private C c;
    private D d;

    public Quad() {

    }

    public Quad(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    public D getD() {
        return d;
    }

    public void setA(A a) {
        this.a = a;
    }

    public void setB(B b) {
        this.b = b;
    }

    public void setC(C c) {
        this.c = c;
    }

    public void setD(D d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Quad{" + a + ", " + b + ", " + c + ", " + d + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quad)) return false;
        Quad<?, ?, ?, ?> quad = (Quad<?, ?, ?, ?>) o;
        return Objects.equals(a, quad.a) &&
                Objects.equals(b, quad.b) &&
                Objects.equals(c, quad.c) &&
                Objects.equals(d, quad.d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d);
    }

}