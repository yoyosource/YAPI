package yapi.math;

import yapi.exceptions.NoStringException;
import yapi.exceptions.RangeException;

import java.util.*;

public class NumberUtils {

    private NumberUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Time Complexity: O(1)
     *
     * @param toRound the number you want to round.
     * @param digits the digits you want to round to.
     * @return the rounded number.
     */
    public static double round(double toRound, int digits) {
        int x = (int)Math.pow(10, digits);

        double r = (toRound * x);
        double t = r - (int) r;

        if (t >= 0.5) {
            return (((double)(int)r) + 1) / x;
        } else {
            return (((double)(int)r) + 0) / x;
        }
    }

    /**
     * Time Complexity: O(√n)
     *
     * @param n the number to check if it is prime
     * @return is prime yes or no.
     */
    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        long limit = (long) Math.sqrt(n);

        for (long i = 5; i <= limit; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Time Complexity: O(n+n*√n)
     *
     * @param n the upper limit to the Primes generated.
     * @return all primes between 0 and {@param n}
     */
    public static List<Long> getPrimes(long n) {
        List<Long> primes = new ArrayList<>();
        if (n < 1) {
            return primes;
        }
        if (n > 1) {
            primes.add(2L);
        }
        for (long i = 1; i <= n; i += 2) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @param a
     * @param b
     * @return
     */
    public static long greatestCommonDivisor(long a, long b) {
        return b == 0 ? (a < 0 ? -a : a) : greatestCommonDivisor(b, a % b);
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @param a
     * @param b
     * @return
     */
    public static long leastCommonMultiple(long a, long b) {
        long lcm = (a / greatestCommonDivisor(a, b)) * b;
        return lcm > 0 ? lcm : -lcm;
    }

    private static long greatestCommonFactor(long a, long b) {
        return b == 0 ? a : greatestCommonFactor(b, a % b);
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean areCoprime(long a, long b) {
        return greatestCommonFactor(a, b) == 1;
    }

    /**
     * Time Complexity: O()
     *
     * @param n
     * @return
     */
    public static long nextPrime(long n) {
        if (isPrime(n)) {
            long l = lowPrimes(n);
            if (l != -1) {
                return l;
            }
            for (long i = n + 2; i < Long.MAX_VALUE; i += 2) {
                if (isPrime(i)) {
                    return i;
                }
            }
        } else {
            if (n < 2) {
                return 2;
            }
            for (long i = n + 1; i < Long.MAX_VALUE; i++) {
                if (isPrime(i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int lowPrimes(long n) {
        if (n == 2) {
            return 3;
        } else if (n == 3) {
            return 5;
        } else if (n == 5) {
            return 7;
        } else if (n == 7) {
            return 11;
        }
        return -1;
    }

    /**
     * Time Complexity: O()
     *
     * @param n
     * @return
     */
    public static List<Long> primeFactorization(long n) {
        if (n < 2) {
            return new ArrayList<>();
        }
        List<Long> primes = new ArrayList<>();
        if (isPrime(n)) {
            primes.add(n);
            return primes;
        }

        long currentPrime = 2;
        while (!isPrime(n)) {
            if (n < currentPrime) {
                primes.add(n);
                return primes;
            }
            while (n % currentPrime == 0) {
                primes.add(currentPrime);
                n = n / currentPrime;
            }
            currentPrime = nextPrime(currentPrime);
        }

        primes.add(n);
        return primes;
    }

    /**
     * Time Complexity: O(n)
     *
     * @param n
     * @return
     */
    public static List<Long> getDivisorsSorted(long n) {
        List<Long> divisors = new ArrayList<>();
        for (long i = 1; i <= Math.abs(n); i++) {
            if (n % i == 0) {
                divisors.add(i);
            }
        }
        return divisors;
    }

    /**
     * Time Complexity: O(√n)
     *
     * @param n
     * @return
     */
    public static List<Long> getDivisors(long n) {
        List<Long> divisors = new ArrayList<>();
        for (long i = 1; i <= Math.sqrt(Math.abs(n)); i++) {
            if (n % i == 0) {
                divisors.add(i);
                if (n / i != i) {
                    divisors.add(n / i);
                }
            }
        }
        return divisors;
    }

    public static long sum(String s) {
        long l = 0;
        for (long lo : getRange(s)) {
            l += lo;
        }
        return l;
    }

    public static long add(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l += lo;
        }
        return l;
    }

    public static long subtract(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l -= lo;
        }
        return l;
    }

    public static long multiply(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l *= lo;
        }
        return l;
    }

    public static long divide(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l /= lo;
        }
        return l;
    }

    public static Long min(List<Long> longs) {
        if (longs.isEmpty()) {
            return null;
        }
        long current = longs.get(0);
        for (long l : longs) {
            if (l < current) {
                current = l;
            }
        }
        return current;
    }

    public static Long max(List<Long> longs) {
        if (longs.isEmpty()) {
            return null;
        }
        long current = longs.get(0);
        for (long l : longs) {
            if (l > current) {
                current = l;
            }
        }
        return current;
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
     * @param range is the range to simplify.
     * @return is your simplified range.
     *
     * @throws RangeException if the range has any mistakes.
     * @throws NullPointerException if the range is empty.
     */
    public static String simplifyRange(String range) throws RangeException, NullPointerException {
        List<Long> longs = getRange(range);
        long min = min(longs);
        long max = max(longs);

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
        long min = min(longs);
        long max = max(longs);

        List<Long> leftout = checkContinuity(longs, min, max);
        if (leftout.isEmpty()) {
            return min + "..." + max;
        }

        System.out.println(leftout + " " + longs);

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
     *
     * @param range is the range expression you want to evaluate.
     * @return the list of values your expression contains.
     *
     * @throws RangeException if the range has any mistakes.
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
            for (String t : rangeModifications) {
                if (t.equals("|") || t.equals("&") || t.equals("#") || t.equals("*")) {
                    continue;
                }
                longs.add(getRange(t));
            }
            try {
                for (int i = 0; i < rangeModifications.length; i++) {
                    if (rangeModifications[i].equals("|")) {
                        longs.set(i, or(longs.get(i - 1), longs.get(i)));
                    }
                    if (rangeModifications[i].equals("*")) {
                        longs.set(i, orExclude(longs.get(i - 1), longs.get(i)));
                    }
                    if (rangeModifications[i].equals("&")) {
                        longs.set(i, and(longs.get(i - 1), longs.get(i)));
                    }
                    if (rangeModifications[i].equals("#")) {
                        longs.set(i, xor(longs.get(i - 1), longs.get(i)));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                throw new RangeException();
            }
            return longs.get(longs.size() - 1);
        }
        if (!range.matches("(-?\\d+[.>]\\.[.<]-?\\d+)(\\\\\\{[.0-9\\-, <>{}\\\\]+\\})?")) {
            throw new RangeException();
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

    private static List<Long> createRange(String[] strings, boolean includeFirst, boolean includeLast) {
        if (strings.length != 2) {
            throw new RangeException();
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
            throw new RangeException();
        }

        List<Long> integers = new ArrayList<>();
        for (long i = start; i <= stop; i++) {
            integers.add(i);
        }
        return integers;
    }

    private static String[] splitRange(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
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
