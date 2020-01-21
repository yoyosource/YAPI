package yapi.math;

import yapi.exceptions.NoStringException;
import yapi.exceptions.math.RangeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RangeSimple {

    private RangeSimple() {
        throw new IllegalStateException();
    }

    /**
     * simplifyRange is used to simplify your range down to one exclude.
     *
     * Example:
     *   '0...20\{0>..2, 7, 5...9\{6, 7}}#-10...10'
     *   '-10...20\{2...4, 6, 10}'
     *
     *   '0...20\{0>..2, 7, 5...9\{6, 7}}'
     *   '2...20\{5, 7...9}'
     *
     * @since Version 1
     *
     * @param range is the range to simplify.
     * @return is your simplified range.
     */
    public static String simplifyRange(String range) throws RangeException {
        List<Long> longs = getRange(range);
        long min = NumberUtils.min(longs);
        long max = NumberUtils.max(longs);

        List<Long> duplicated = duplicates(longs);
        StringBuilder duplicatedString = new StringBuilder();
        if (!duplicated.isEmpty()) {
            for (Long l : duplicated) {
                if (duplicatedString.length() != 0) {
                    duplicatedString.append("|");
                }
                duplicatedString.append(l);
            }
        }

        if (min == max) {
            return min + duplicatedString.toString();
        }
        List<Long> leftout = checkContinuity(longs, min, max);
        if (leftout.isEmpty()) {
            return min + "..." + max + duplicatedString.toString();
        }
        return min + "..." + max + "\\{" + simplifyLeftOut(leftout) + "}" + duplicatedString.toString();
    }

    private static String simplifyLeftOut(List<Long> longs) {
        long min = NumberUtils.min(longs);
        long max = NumberUtils.max(longs);

        List<Long> leftout = checkContinuity(longs, min, max);
        if (leftout.isEmpty()) {
            return min + "..." + max;
        }

        List<List<Long>> longList = new ArrayList<>();
        List<Long> currentList = new ArrayList<>();
        for (long i = min; i <= max; i++) {
            if (longs.contains(i)) {
                currentList.add(i);
            } else {
                longList.add(currentList);
                currentList = new ArrayList<>();
            }
        }
        if (!currentList.isEmpty()) {
            longList.add(currentList);
        }

        StringBuilder st = new StringBuilder();
        for (List<Long> longs1 : longList) {
            if (longs1.isEmpty()) {
                continue;
            }
            if (st.toString().length() > 0) {
                st.append(", ");
            }
            if (longs1.size() == 1) {
                st.append(longs1.get(0));
            } else if (longs1.size() == 2) {
                st.append(longs1.get(0) + ", " + longs.get(1));
            } else {
                st.append(longs1.get(0) + "..." + longs1.get(longs1.size() - 1));
            }
        }
        return st.toString();
    }

    private static List<Long> duplicates(List<Long> longs) {
        List<Long> duplicated = new ArrayList<>();
        for (int x = 0; x < longs.size(); x++) {
            long currentLong = longs.get(x);
            int count = 0;
            for (int y = 0; y < longs.size(); y++) {
                if (longs.get(y) == currentLong) {
                    count++;
                }
            }
            count--;
            while (count > 0) {
                duplicated.add(currentLong);
                count--;
            }
        }
        return duplicated;
    }

    private static List<Long> checkContinuity(List<Long> longs, long min, long max) {
        List<Long> leftout = new ArrayList<>();
        for (long l = min; l < max; l++) {
            if (!longs.contains(l)) {
                leftout.add(l);
            }
        }
        return leftout;
    }

    /**
     * getRange is used if you want to get a range of numbers.
     *
     * Example:
     *  Simple Ranges:
     *   From x to y including x and y
     *   '0...5'
     *     0, 1, 2, 3, 4, 5
     *   From x to y excluding x including y
     *   '0>..5'
     *     1, 2, 3, 4, 5
     *   From x to y including x excluding y
     *   '0..<5'
     *     0, 1, 2, 3, 4
     *   From x to y excluding x and y
     *   '0>.<5'
     *     1, 2, 3, 4
     *  Ranges with excluding Numbers:
     *   From x to y without 2
     *   '0...5\{2}'
     *     0, 1, 3, 4, 5
     *   From x to y without (From 0 to 2 excluding 0)
     *   '0...5\{0>..2}'
     *     0, 3, 4, 5
     *   From x to y without 2 and 3
     *   '0...5\{2, 3}'
     *     0, 1, 4, 5
     *   From x to y without (From 4 to 6 and 8 to 10)
     *   '0...10\{4...6, 8...10}'
     *     0, 1, 2, 3, 7
     *  More Complex Ranges:
     *   From x to y without (From 5 to 10 without 7 and 8)
     *   '0...10\{5...10\{7, 8}}'
     *     0, 1, 2, 3, 4, 7, 8
     *   From x to y without (From 5 to 10 without 7 and 8) and 1
     *   '0...10\{5...10\{7, 8}, 1}'
     *     0, 2, 3, 4, 7, 8
     *  Operations on Ranges:
     *   From x to y and x2 to y2 just put them together
     *   '0...5|-5...0'
     *     0, 1, 2, 3, 4, 5, -5, -4, -3, -2, -1, 0
     *   From x to y or x2 to y2 but leave out duplicates
     *   '0...5*-5...0'
     *     0, 1, 2, 3, 4, 5, -5, -4, -3, -2, -1
     *   From x to y and x2 to y2 but leave out the numbers which are not present in both ranges
     *   '0...5&2...7'
     *     2, 3, 4, 5
     *   From x to y and x2 to y2 but leave out the numbers which are present in both ranges
     *   '0...5#2...7'
     *     0, 1, 6, 7
     * Complex Example:
     *   '0...20\{0>..2, 7, 5...9\{6, 7}}#-10...10'
     *     11, 12, 13, 14, 15, 16, 17, 18, 19, 20, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 5, 7, 8, 9
     *   '0...20\{0>..2, 7, 5...9\{6, 7}}'
     *     0, 3, 4, 6, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
     *
     * @since Version 1
     *
     * @see #simplifyRange(String) to simplify your Range
     *
     * @param range is the range expression you want to evaluate.
     * @return the list of values your expression contains.
     */
    public static List<Long> getRange(String range) throws RangeException {
        if (range.matches("-?\\d+")) {
            List<Long> longs = new ArrayList<>();
            longs.add(Long.parseLong(range));
            return longs;
        }
        if (range.contains("|") || range.contains("&") || range.contains("#") || range.contains("*")) {
            String[] rangeModifications = splitRange(range, new String[]{"|", "&", "#", "*"}, true, false);
            List<List<Long>> longs = new ArrayList<>();
            List<String> operations = new ArrayList<>();
            for (String t : rangeModifications) {
                if (t.equals("|") || t.equals("&") || t.equals("#") || t.equals("*")) {
                    operations.add(t);
                    continue;
                }
                longs.add(getRange(t));
            }
            try {
                for (int i = 0; i < operations.size(); i++) {
                    String operation = operations.get(i);
                    if (operation.equals("|")) {
                        longs.set(i, or(longs.get(i), longs.get(i + 1)));
                    }
                    if (operation.equals("*")) {
                        longs.set(i, orExclude(longs.get(i), longs.get(i + 1)));
                    }
                    if (operation.equals("&")) {
                        longs.set(i, and(longs.get(i), longs.get(i + 1)));
                    }
                    if (operation.equals("#")) {
                        longs.set(i, xor(longs.get(i), longs.get(i + 1)));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                throw new RangeException("Range Expression in Operation part is missing: " + range);
            }
            return longs.get(longs.size() - 1);
        }
        if (!range.matches("(-?\\d+[.>]\\.[.<]-?\\d+)(\\\\\\{[.0-9\\-, <>{}\\\\]+\\})?")) {
            if (!range.matches("(-?\\d+[.>]\\.[.<]-?\\d+)")) {
                throw new RangeException("Range Expression Exception in Range part of: " + range);
            }
            throw new RangeException("Range Expression Exception in Exclude part of: " + range);
        }

        char[] chars = range.toCharArray();
        StringBuilder st = new StringBuilder();
        boolean hasExclude = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\') {
                hasExclude = true;
                range = range.substring(st.length() + 2, range.length() - 1);
                break;
            }
            st.append(chars[i]);
        }

        String currentRange = st.toString();
        boolean includeFirst = true;
        boolean includeLast = true;
        if (currentRange.contains("<")) {
            includeFirst = false;
        }
        if (currentRange.contains(">")) {
            includeLast = false;
        }
        String[] strings = currentRange.split("[.>]\\.[.<]");
        List<Long> longs = createRange(strings, includeFirst, includeLast);

        if (hasExclude) {
            String[] toExclude = splitRange(range, new String[]{", ", ","}, false, false);
            for (String t : toExclude) {
                List<Long> exclude = getRange(t);
                for (Long l : exclude) {
                    longs.remove(l);
                }
            }
        }

        return longs;
    }

    private static List<Long> or(List<Long> longs1, List<Long> longs2) {
        List<Long> longs = new ArrayList<>();
        for (long l : longs1) {
            longs.add(l);
        }
        for (long l : longs2) {
            longs.add(l);
        }
        return longs;
    }

    private static List<Long> orExclude(List<Long> longs1, List<Long> longs2) {
        List<Long> longs = new ArrayList<>();
        for (long l : longs1) {
            longs.add(l);
        }
        for (long l : longs2) {
            if (!longs.contains(l)) {
                longs.add(l);
            }
        }
        return longs;
    }

    private static List<Long> xor(List<Long> longs1, List<Long> longs2) {
        List<Long> longs = new ArrayList<>();
        for (long l : longs1) {
            if (!longs2.contains(l) && !longs.contains(l)) {
                longs.add(l);
            }
        }
        for (long l : longs2) {
            if (!longs1.contains(l) && !longs.contains(l)) {
                longs.add(l);
            }
        }
        return longs;
    }

    private static List<Long> and(List<Long> longs1, List<Long> longs2) {
        List<Long> longs = new ArrayList<>();
        for (long l : longs1) {
            if (longs2.contains(l)) {
                longs.add(l);
            }
        }
        return longs;
    }

    protected static List<Long> createRange(String[] strings, boolean includeFirst, boolean includeLast) {
        if (strings.length != 2) {
            throw new RangeException("The range has to many range parameter: " + Arrays.toString(strings));
        }
        long start = Long.parseLong(strings[0]);
        long stop = Long.parseLong(strings[1]);

        if (!includeFirst) {
            start++;
        }
        if (!includeLast) {
            stop--;
        }

        if (start > stop) {
            throw new RangeException("Start of range is bigger than end of Range: " +  start + " " + stop);
        }

        List<Long> integers = new ArrayList<>();
        for (long i = start; i <= stop; i++) {
            integers.add(i);
        }
        return integers;
    }

    protected static String[] splitRange(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
        if (string == null) throw new NullPointerException();
        if (splitStrings == null) throw new NullPointerException();
        if (string.isEmpty()) throw new NoStringException("No String");
        if (splitStrings.length == 0) throw new NoStringException("No Split Strings");

        char[] chars = string.toCharArray();

        List<String> words = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int i = 0;
        int lastSplit = 0;

        int inString = 0;

        while (i < chars.length) {
            int splitStringTest = 0;
            char c = chars[i];
            if (c == '{') {
                inString += 1;
            }
            if (c == '}') {
                inString -= 1;
            }
            if (inString > 0) {
                i++;
                stringBuilder.append(c);
                continue;
            }
            String s = "";
            for (String st : splitStrings) {
                StringBuilder sb = new StringBuilder();
                int index = i;
                int currentIndex = i;
                while (currentIndex < chars.length && currentIndex < index + st.length()) {
                    sb.append(chars[currentIndex]);
                    currentIndex++;
                }
                if (sb.toString().equals(st)) {
                    if (s.length() == 0) {
                        s = st;
                    }
                    splitStringTest++;
                }
            }

            if (splitStringTest == 0) {
                stringBuilder.append(c);
            } else {
                i += s.length() - 1;
                if (stringBuilder.length() == 0) {
                    if (reviveSplitted && !addToLast) {
                        words.add(s);
                    } else if (reviveSplitted && addToLast) {
                        words.add(stringBuilder + s);
                    }
                    stringBuilder = new StringBuilder();
                    lastSplit = i;
                    i++;
                    continue;
                }
                if (reviveSplitted) {
                    if (addToLast) {
                        words.add(stringBuilder.toString() + s);
                    } else {
                        words.add(stringBuilder.toString());
                        words.add(s);
                    }
                } else {
                    words.add(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                lastSplit = i;
            }
            i++;
        }
        if (lastSplit != string.length()) {
            if (stringBuilder.length() == 0) {
                return words.toArray(new String[0]);
            }
            words.add(stringBuilder.toString());
        }
        return words.toArray(new String[0]);
    }

}
