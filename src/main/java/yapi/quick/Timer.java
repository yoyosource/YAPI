package yapi.quick;

public class Timer {

    private long time = 0;
    private long lastTime = 0;

    public Timer() {}

    public void start() {
        if (time == 0) {
            lastTime = 0;
            time = System.nanoTime();
        }
    }

    public void stop() {
        if (time != 0) {
            lastTime = System.nanoTime() - time;
            time = 0;
        }
    }

    public void clear() {
        time = 0;
    }

    public void reset() {
        time = 0;
        lastTime = 0;
    }

    public long getTime() {
        return lastTime;
    }

    public String getTimeAsString() {
        return lastTime + "ns";
    }

    /**
     * N        Unformatted Nano time
     * X        Unformatted Micro time
     * T        Unformatted Milli time
     * S        Unformatted Seconds
     * A        Unformatted Minutes
     * H        Unformatted Hours
     * D        Unformatted Days
     *
     * n - nnn  Nano Seconds
     * x - xxx  Micro Seconds
     * m - mmm  Milli Seconds
     * s - ss   Seconds
     * M - MM   Minutes
     * h - hh   Hours
     * d - dd   Days
     *
     * F>?      Format type
     * | |
     * | +- N, X, T, S, A, H, D, n, x, m, s, M, h, d
     * |    |  |  |  |  |  |  |  |  |  |  |  |  |  |
     * |    +--*--*--*--*--*--*--+  |  |  |  |  |  |
     * |    |  |  |  |  |  |  |     |  |  |  |  |  |
     * |    |  +--*--*--*--*--*-----+  |  |  |  |  |
     * |    |  |  |  |  |  |  |        |  |  |  |  |
     * |    |  |  +--*--*--*--*--------+  |  |  |  |
     * |    |  |  |  |  |  |  |           |  |  |  |
     * |    |  |  |  +--*--*--*-----------+  |  |  |
     * |    |  |  |  |  |  |  |              |  |  |
     * |    |  |  |  |  +--*--*--------------+  |  |
     * |    |  |  |  |  |  |  |                 |  |
     * |    |  |  |  |  |  +--*-----------------+  |
     * |    |  |  |  |  |  |  |                    |
     * |    |  |  |  |  |  |  +--------------------+
     * |    |  |  |  |  |  |  |
     * |    ns µs ms s  m  h  d
     * |
     * +--- Synonym for Format
     *
     * @param format
     * @return
     */
    public String getTimeFormatted(String format) {
        format = format.replaceAll("F>[Nn]", "////1");
        format = format.replaceAll("F>[Xx]", "////2");
        format = format.replaceAll("F>[Tm]", "////3");
        format = format.replaceAll("F>[Ss]", "////4");
        format = format.replaceAll("F>[AM]", "////5");
        format = format.replaceAll("F>[Hh]", "////6");
        format = format.replaceAll("F>[Dd]", "////7");

        format = format.replace("N", lastTime + "");
        format = format.replace("X", lastTime / 1000 + "");
        format = format.replace("T", lastTime / 1000 / 1000 + "");
        format = format.replace("S", lastTime / 1000 / 1000 / 1000 + "");
        format = format.replace("A", lastTime / 1000 / 1000 / 1000 / 60 + "");
        format = format.replace("H", lastTime / 1000 / 1000 / 1000 / 60 / 60 + "");
        format = format.replace("D", lastTime / 1000 / 1000 / 1000 / 60 / 60 / 24 + "");

        format = formatNano(format);
        format = formatMicro(format);
        format = formatMilli(format);
        format = formatSeconds(format);
        format = formatMinutes(format);
        format = formatHours(format);
        format = formatDays(format);

        format = format.replace("////1", "ns").replace("////2", "µs").replace("////3", "ms").replace("////4", "s").replace("////5", "m").replace("////6", "h").replace("////7", "d");
        return format;
    }

    private String formatNano(String format) {
        long nano = lastTime - lastTime / 1000 * 1000;
        StringBuilder n3 = new StringBuilder().append(nano);
        while (n3.length() < 3) {
            n3.insert(0, "0");
        }
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("nnn", n3);
        format = format.replace("nn", n2);
        format = format.replace("n", n1);
        return format;
    }

    private String formatMicro(String format) {
        long nano = lastTime / 1000 - lastTime / 1000 / 1000 * 1000;
        StringBuilder n3 = new StringBuilder().append(nano);
        while (n3.length() < 3) {
            n3.insert(0, "0");
        }
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("xxx", n3);
        format = format.replace("xx", n2);
        format = format.replace("x", n1);
        return format;
    }

    private String formatMilli(String format) {
        long nano = lastTime / 1000 / 1000 - lastTime / 1000 / 1000 / 1000 * 1000;
        StringBuilder n3 = new StringBuilder().append(nano);
        while (n3.length() < 3) {
            n3.insert(0, "0");
        }
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("mmm", n3);
        format = format.replace("mm", n2);
        format = format.replace("m", n1);
        return format;
    }

    private String formatSeconds(String format) {
        long nano = lastTime / 1000 / 1000 / 1000 - lastTime / 1000 / 1000 / 1000 / 60 * 60;
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("ss", n2);
        format = format.replace("s", n1);
        return format;
    }

    private String formatMinutes(String format) {
        long nano = lastTime / 1000 / 1000 / 1000 / 60 - lastTime / 1000 / 1000 / 1000 / 60 / 60 * 60;
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("MM", n2);
        format = format.replace("M", n1);
        return format;
    }

    private String formatHours(String format) {
        long nano = lastTime / 1000 / 1000 / 1000 / 60 / 60 - lastTime / 1000 / 1000 / 1000 / 60 / 60 / 24 * 24;
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("hh", n2);
        format = format.replace("h", n1);
        return format;
    }

    private String formatDays(String format) {
        long nano = lastTime / 1000 / 1000 / 1000 / 60 / 60 / 24 - lastTime / 1000 / 1000 / 1000 / 60 / 60 / 24;
        StringBuilder n2 = new StringBuilder().append(nano);
        while (n2.length() < 2) {
            n2.insert(0, "0");
        }
        StringBuilder n1 = new StringBuilder().append(nano);
        while (n1.length() < 1) {
            n1.insert(0, "0");
        }

        format = format.replace("dd", n2);
        format = format.replace("d", n1);
        return format;
    }

    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(1000);
        timer.stop();
        System.out.println(timer.getTimeFormatted("dF>d hF>h MF>M sF>s mF>m xF>x nF>n   N"));
    }

}
