package yapi.math;

import yapi.exceptions.RangeException;

import java.util.ArrayList;
import java.util.List;

public class Range {

    private String rangeString = "";

    private RangeCheck included;
    private List<RangeCheck> excluded = new ArrayList<>();

    private boolean b = false;

    public Range(String range) {
        this.rangeString = range;
        if (checkRange(range)) {
            if (range.matches("[-0-9.><]+\\\\.+")) {
                included = new RangeCheck(range.substring(0, range.indexOf('\\')));
            } else {
                included = new RangeCheck(range);
            }
        } else {
            throw new RangeException("Range Expression was not an Range or was recursive");
        }
    }

    private boolean checkRange(String range) {
        if (range.matches("((-?\\d+[.>][.][.<]-?\\d+)|(-?\\d+(\\.\\d+)?[.>]\\.\\.)|(\\.\\.[.<]-?\\d+(\\.\\d+)?))(\\\\\\{[ ,0-9.<>]+\\})")) {
            if (b) {
                throw new RangeException("Range Expression was recursive");
            }
            if (range.indexOf('\\') + 1 != range.indexOf('{')) {
                throw new RangeException("Range Expression Error with \\ and {");
            }
            String[] strings = NumberUtils.splitRange(range.substring(range.indexOf('\\') + 2, range.lastIndexOf('}')), new String[]{", ", ","}, false, false);
            b = true;
            for (String s : strings) {
                if (!checkRange(s)) {
                    throw new RangeException("Range Expression " + s + " is not a range");
                }
                excluded.add(new RangeCheck(s));
            }
            b = false;
        }
        if (range.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        }
        return range.matches("((-?\\d+[.>][.][.<]-?\\d+)|(-?\\d+(\\.\\d+)?[.>]\\.\\.)|(\\.\\.[.<]-?\\d+(\\.\\d+)?))(\\\\\\{[ ,0-9.<>]+\\})?");
    }

    /**
     *
     * @since Version 1
     *
     * @param n
     * @param <N>
     * @return
     */
    public <N> boolean in(N n) {
        return check(n);
    }

    /**
     *
     * @since Version 1
     *
     * @param n
     * @param <N>
     * @return
     */
    public <N> boolean inside(N n) {
        return check(n);
    }

    /**
     *
     * @since Version 1
     *
     * @param n
     * @param <N>
     * @return
     */
    public <N> boolean out(N n) {
        return !check(n);
    }

    /**
     *
     * @since Version 1
     *
     * @param n
     * @param <N>
     * @return
     */
    public <N> boolean outside(N n) {
        return !check(n);
    }

    private <N> boolean check(N n) {
        boolean excluded = false;
        for (RangeCheck rangeCheck : this.excluded) {
            if (n instanceof Integer) {
                excluded = excluded || rangeCheck.checkInteger((Integer)n);
            }
            if (n instanceof Long) {
                excluded = excluded || rangeCheck.checkLong((Long)n);
            }
            if (n instanceof Double) {
                excluded = excluded || rangeCheck.checkDouble((Double)n);
            }
            if (n instanceof Float) {
                excluded = excluded || rangeCheck.checkFloat((Float)n);
            }
        }

        if (excluded) {
            return false;
        }

        if (n instanceof Integer) {
            return included.checkInteger((Integer)n);
        }
        if (n instanceof Long) {
            return included.checkLong((Long)n);
        }
        if (n instanceof Double) {
            return included.checkDouble((Double)n);
        }
        if (n instanceof Float) {
            return included.checkFloat((Float)n);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Range{" +
                "rangeString='" + rangeString + '\'' +
                '}';
    }
}

class RangeCheck {

    private Double min = null;
    private Double max = null;

    public RangeCheck(String range) {
        if (range.matches("-?\\d+(\\.\\d+)?")) {
            min = Double.parseDouble(range);
            max = Double.parseDouble(range);
        }
        else if (range.matches("-?\\d+\\.\\.\\.-?\\d+")) {
            min = Double.parseDouble(range.split("\\.\\.\\.")[0]);
            max = Double.parseDouble(range.split("\\.\\.\\.")[1]);
        } else if (range.matches("-?\\d+\\>\\.\\.-?\\d+")) {
            min = Double.parseDouble(range.split("\\>\\.\\.")[0]) + 1;
            max = Double.parseDouble(range.split("\\>\\.\\.")[1]);
        } else if (range.matches("-?\\d+\\.\\.\\<-?\\d+")) {
            min = Double.parseDouble(range.split("\\.\\.\\<")[0]);
            max = Double.parseDouble(range.split("\\.\\.\\<")[1]) - 1;
        } else if (range.matches("-?\\d+\\>\\.\\<-?\\d+")) {
            min = Double.parseDouble(range.split("\\>\\.\\<")[0]) + 1;
            max = Double.parseDouble(range.split("\\>\\.\\<")[1]) - 1;
        }
        else if (range.matches("\\.\\.\\.-?\\d+")) {
            max = Double.parseDouble(range.substring(3));
        } else if (range.matches("\\.\\.\\<-?\\d+")) {
            max = Double.parseDouble(range.substring(3)) - 1;
        } else if (range.matches("-?\\d+\\.\\.\\.")) {
            min = Double.parseDouble(range.substring(0, range.length() - 3));
        } else if (range.matches("-?\\d+\\>\\.\\.")) {
            min = Double.parseDouble(range.substring(0, range.length() - 3)) + 1;
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public boolean checkInteger(int i)  {
        if (min == null && max != null) {
            return max > i;
        } else if (min != null && max == null) {
            return min < i;
        } else if (min == null && max == null) {
            return true;
        }
        return min < i && max > i;
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public boolean checkLong(long i)  {
        if (min == null && max != null) {
            return max > i;
        } else if (min != null && max == null) {
            return min < i;
        } else if (min == null && max == null) {
            return true;
        }
        return min < i && max > i;
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public boolean checkDouble(double i)  {
        if (min == null && max != null) {
            return max > i;
        } else if (min != null && max == null) {
            return min < i;
        } else if (min == null && max == null) {
            return true;
        }
        return min < i && max > i;
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public boolean checkFloat(float i)  {
        if (min == null && max != null) {
            return max > i;
        } else if (min != null && max == null) {
            return min < i;
        } else if (min == null && max == null) {
            return true;
        }
        return min < i && max > i;
    }

    @Override
    public String toString() {
        return "RangeCheck{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}