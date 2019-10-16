package yapi.math;

import yapi.exceptions.RangeException;

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> integers = getRange(s);
        long l = 0;
        for (long lo : integers) {
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

    public static List<Integer> getRange(String s) {
        if (s.matches("\\[\\d+ \\d+\\]")) {
            String[] strings = s.substring(1, s.length() - 1).split(" ");
            return createRange(strings, true, true);
        } else if (s.matches("\\d+ \\d+")) {
            String[] strings = s.split(" ");
            return createRange(strings, true, true);
        } else if (s.matches("\\]\\d+ \\d+\\]")) {
            String[] strings = s.substring(1, s.length() - 1).split(" ");
            return createRange(strings, false, true);
        } else if (s.matches("\\[\\d+ \\d+\\[")) {
            String[] strings = s.substring(1, s.length() - 1).split(" ");
            return createRange(strings, true, false);
        } else if (s.matches("\\]\\d+ \\d+\\[")) {
            String[] strings = s.substring(1, s.length() - 1).split(" ");
            return createRange(strings, false, false);
        } else if (s.matches("\\d+\\.\\.\\d+")) {
            String[] strings = s.split("\\.\\.");
            return createRange(strings, true, true);
        } else if (s.matches("\\d+\\(\\.\\d+")) {
            String[] strings = s.split("\\(\\.");
            return createRange(strings, false, true);
        } else if (s.matches("\\d+\\.\\)\\d+")) {
            String[] strings = s.split("\\.\\)");
            return createRange(strings, true, false);
        } else if (s.matches("\\d+\\(\\)\\d+")) {
            String[] strings = s.split("\\(\\)");
            return createRange(strings, false, false);
        }
        else {
            throw new RangeException();
        }
    }

    private static List<Integer> createRange(String[] strings, boolean includeFirst, boolean includeLast) {
        if (strings.length != 2) {
            throw new RangeException();
        }
        int start = Integer.parseInt(strings[0]);
        int stop = Integer.parseInt(strings[1]);

        if (!includeFirst) {
            start++;
        }
        if (!includeLast) {
            stop--;
        }

        if (start > stop) {
            throw new RangeException();
        }

        List<Integer> integers = new ArrayList<>();
        for (int i = start; i <= stop; i++) {
            integers.add(i);
        }
        return integers;
    }

}
