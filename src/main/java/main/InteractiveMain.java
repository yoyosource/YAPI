package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveMain {

    private static List<String> messages = new ArrayList<>();

    private static void send() {
        if (messages.isEmpty()) {
            return;
        }
        String message = messages.stream().collect(Collectors.joining("\n"));
        messages.clear();
        System.out.println(message);
    }

    private static void send(String s) {
        while (s.endsWith(" ")) {
            s = s.substring(0, s.length() - 1);
        }
        messages.add(s);
    }

    private static void send(StringBuilder s) {
        send(s.toString());
    }

    public static void main(String[] args) {
        try {
            byte[] bytes = InteractiveMain.class.getResourceAsStream("/yapi/conjecture/Collatz.class").readAllBytes();
            StringBuilder st = new StringBuilder();
            for (byte b : bytes) {
                st.append((char)b);
            }
            System.out.println(st);
        } catch (IOException e) {

        }

        //versions();
    }

    public static void mapKeys() {
        send("");
        send("Map Keys:");
        send("> s -> static");
        send("> t -> throws");
        send("> r -> returns");
    }

    private static List<Version> versions = new ArrayList<>();

    static {
        versions.add(new Version("1.0 Release", new Point[]{
                new Point("DataStructures", new Point[]{
                        new Point("IntegerBuffer", new Point[]{
                                new Point("Constructor", new String[]{"<empty>", "int[]", "List<Integer>", "NumberRandom", "NumberRandom, int"}),
                                new Point("Methods", new String[]{"initialze(int[])", "initialze(List<Integer>)", "hasNext()", "next()", "next(int)", "all()", "nextAsBuffer(int)", "fold()", "fold(char)"})
                        }),
                        new Point("PrefixArray<E>", new Point[]{
                                new Point("Constructor", new String[]{"<empty>", "List<E>", "String"}),
                                new Point("Methods", new String[]{"get()", "get(int)", "add(List<E>)", "add(E...)", "add(E)", "add(E, int)", "contains(E)", "tailContains(E)", "remove(E)", "remove(int)", "isEmpty()", "isNotEmpty()", "size()", "length()", "tailSize()", "tailLength()", "sort(Comparator)", "fold()", "fold(char)", "foldInteger(char)", "foldDouble(char)", "foldFloat(char)", "foldLong(char)", "isMutable()", "isImmutable()", "makeImmutable()", "copy()"})
                        })
                }),
                new Point("Exceptions", new Point("YAPIException", new String[]{"ArrayMutationException", "NoStringException", "RangeException"})),
                new Point("Math", new Point[]{
                        new Point("NumberRandom", new Point[]{
                                new Point("Constructor", new String[]{"<empty>", "long"}),
                                new Point("Methods", new String[]{"getSeed()", "getDouble()", "getDouble(double)", "getInt()", "getInt(int)", "getLong()", "getLong(long)", "getFloat()", "getFloat(float)", "getChar()", "getChar(char)", "getString(int)"})
                        }),
                        new Point("Range", new Point[]{
                                new Point("Constructor", new String[]{"String"}),
                                new Point("Methods", new String[]{"in(N)", "inside(N)", "out(N)", "outside(N)"})
                        }),
                        new Point("Vector", new Point[]{
                                new Point("Constructor", new String[]{"int", "double..."}),
                                new Point("Methods", new String[]{"setVector(int, double)", "setVec(double[])", "addVector(Vector)", "subtractVector(Vector)", "multiplyVector(int)", "multiplyVectorScalar(Vector)", "crossProduct(Vector)", "length()", "get(int)", "copy()"})
                        }),
                        new Point("NumberUtils", new Point[]{
                                new Point("Methods (s)", new String[]{"round(double, int)", "isPrime(long)", "getPrimes(long)", "greatestCommonDivisor(long, long)", "leastCommonMultiple(long, long)", "areCoprime(long, long)", "nextPrime(long)", "primeFactorization(long)", "setDivisorsSorted(long)", "getDivisors(long)", "sum(String)", "add(List<Long>)", "subtract(List<Long>)", "multiply(List<Long>)", "divide(List<Long>)", "min(List<Long>)", "max(List<Long>)", "simplifyRange(String)", "getRange(String)"})
                        })
                }),
                new Point("Utils", new String[]{"ArrayUtils", "ColorUtils", "FileUtils", "StringUtils"})}));
    }

    private static void versions() {
        send("YAPI:");
        send("Versions:");
        for (Version version : versions) {
            send(version.getVersionOwn());
        }
        send("Patch:");
        send("> ---");
        send("");
        latest();
        mapKeys();
        send();
    }

    private static void latest() {
        send(versions.get(versions.size() - 1).getString());
    }

    private static void version(String search) {
        for (Version version : versions) {
            if (version.getVersionOwn().equalsIgnoreCase(search)) {
                send(version.getString());
                break;
            }
        }
        for (Version version : versions) {
            if (version.getVersionOwn().toLowerCase().contains(search.toLowerCase())) {
                send(version.getString());
                break;
            }
        }
        send("No Version with that name found.");
    }

    private static void search(String search) {

    }

}

class Point {

    private String topic = "";
    private List<Point> points = new ArrayList<>();

    public Point(String topic) {
        this.topic = topic;
    }

    public Point(String topic, Point point) {
        this.topic = topic;
        points.add(point);
    }

    public Point(String topic, List<Point> points) {
        this.topic = topic;
        this.points = points;
    }

    public Point(String topic, Point[] points) {
        this.topic = topic;
        this.points = Arrays.asList(points);
    }

    public Point(String topic, String[] points) {
        this.topic = topic;
        for (String s : points) {
            this.points.add(new Point(s));
        }
    }

    public String getString() {
        return getString(0, false);
    }

    public String getString(int i, boolean b) {
        StringBuilder st = new StringBuilder();
        if (i == 0) {
            st.append(topic).append(':');
            for (Point p : points) {
                st.append('\n').append(p.getString(i + 1, topic.startsWith("Constructor") || topic.startsWith("Method")));
            }
        } else {
            StringBuilder pad = new StringBuilder();
            for (int j = 1; j < i; j++) {
                pad.append(' ');
            }
            if (b) {
                st.append(pad.toString()).append('-').append(' ').append(topic);
            } else {
                if (topic.startsWith("Constructor") || topic.startsWith("Method")) {
                    st.append(pad.toString()).append('@').append(' ').append(topic);
                } else {
                    st.append(pad.toString()).append('>').append(' ').append(topic);
                }
            }
            for (Point p : points) {
                st.append('\n').append(p.getString(i + 1, topic.startsWith("Constructor") || topic.startsWith("Method")));
            }
        }
        return st.toString();
    }

    public String getTopic() {
        return topic;
    }

    public List<Point> getPoints() {
        return points;
    }
}

class Version {

    private String version = "";
    private List<Point> points = new ArrayList<>();

    public Version(String version) {
        this.version = version;
    }

    public Version(String version, List<Point> points) {
        this.points = points;
    }

    public Version(String version, Point[] points) {
        this.version = version;
        this.points = Arrays.asList(points);
    }

    public Version(String version, String[] points) {
        this.version = version;
        for (String s : points) {
            this.points.add(new Point(s));
        }
    }

    public String getString() {
        StringBuilder st = new StringBuilder();
        st.append("Version ").append(version);
        for (Point p : points) {
            st.append('\n').append(p.getString());
        }
        return st.toString();
    }

    public String getVersion() {
        return "Version " + version;
    }

    public String getVersionOwn() {
        return "> " + version;
    }

    public List<Point> getPoints() {
        return points;
    }
}
