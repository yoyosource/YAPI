package yapi.datastructures.tuples;

public class ImmutableTriple<L, M, R> extends Triple<L, M, R> {

    private ImmutableTriple() {

    }

    public ImmutableTriple(L l, M m, R r) {
        super(l, m, r);
    }

    @Override
    public void setL(L l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setM(M m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setR(R r) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "ImmutableTriple{" + getL() + ", " + getM() + ", " + getR() + "}";
    }
}
