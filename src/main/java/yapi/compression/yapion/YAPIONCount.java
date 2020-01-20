package yapi.compression.yapion;

import java.util.Objects;

public class YAPIONCount {

    private String string;
    private long count = 1L;
    private long loss = 0;
    private long previous = 0;

    public YAPIONCount(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public long getCount() {
        return count;
    }

    public long getLoss() {
        return loss;
    }

    public void increment() {
        count++;
        loss = (string.length() - 4) * (count - 1) - 4;
        previous = string.length() * count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof String) return o.equals(string);
        if (!(o instanceof YAPIONCount)) return false;
        YAPIONCount that = (YAPIONCount) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return "'" + string + "':" + count + "(" + previous + ">" + (previous - loss) + "->" + loss + ")";
    }
}
