package yapi.datastructures.tuples;

import java.util.Objects;

public class Pair<L, R> {

    private L l = null;
    private R r = null;

    public Pair() {

    }

    public Pair(R r, L l) {
        this.r = r;
        this.l = l;
    }

    public R getR() {
        return r;
    }

    public L getL() {
        return l;
    }

    public void setR(R r) {
        this.r = r;
    }

    public void setL(L l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return "Pair{" + l + ", " + r + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(r, pair.r) &&
                Objects.equals(l, pair.l);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, l);
    }

}
