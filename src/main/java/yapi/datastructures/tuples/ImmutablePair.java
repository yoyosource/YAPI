package yapi.datastructures.tuples;

public class ImmutablePair<L, R> extends Pair<L, R> {

    private ImmutablePair() {

    }

    public ImmutablePair(R r, L l) {
        super(r, l);
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