package yapi.math;

import yapi.exceptions.MathException;
import yapi.exceptions.math.RangeException;

import java.math.BigInteger;
import java.util.*;

public class NumberUtils {

    private NumberUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Time Complexity: O(1)
     *
     * @since Version 1
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
     * @since Version 1
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
     * @since Version 1
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
     * @since Version 1
     *
     * @param a
     * @param b
     * @return
     */
    public static long greatestCommonDivisor(long a, long b) {
        if (b == 0) {
            return a < 0 ? -a : a;
        } else {
            return greatestCommonDivisor(b, a % b);
        }
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @since Version 1
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
     * @since Version 1
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
     * @since Version 1
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
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            for (long i = n + 2; i < Long.MAX_VALUE; i += 6) {
                if (isPrime(i)) {
                    return i;
                }
                if (isPrime(i + 2)) {
                    return i + 2;
                }
            }
        } else {
            if (n < 2) {
                return 2;
            }
            if (n % 2 == 0) {
                n++;
            }
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            for (long i = n; i < Long.MAX_VALUE; i += 6) {
                if (isPrime(i)) {
                    return i;
                }
                if (isPrime(i + 2)) {
                    return i + 2;
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
     * @since Version 1
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
                if (n != 1) {
                    primes.add(n);
                }
                return primes;
            }
            while (n % currentPrime == 0) {
                primes.add(currentPrime);
                n = n / currentPrime;
            }
            currentPrime = nextPrime(currentPrime);
        }

        if (n != 1) {
            primes.add(n);
        }
        return primes;
    }

    /**
     * Time Complexity: O(n)
     *
     * @since Version 1
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
     * @since Version 1
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

    /**
     *
     * @since Version 1
     *
     * @param s
     * @return
     */
    public static long sum(String s) {
        long l = 0;
        for (long lo : RangeSimple.getRange(s)) {
            l += lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long add(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l += lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1.2
     *
     * @param s
     * @return
     */
    public static long add(String s) {
        return sum(s);
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long subtract(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l -= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long multiply(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l *= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long divide(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l /= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static Long min(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        for (long l : longs) {
            if (l < current) {
                current = l;
            }
        }
        return current;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param longs
     * @return
     */
    public static Long minIndex(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        long index = 0;
        for (int i = 0; i < longs.size(); i++) {
            long l = longs.get(i);
            if (l < current) {
                current = l;
                index = i;
            }
        }
        return index;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static Long max(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
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
     *
     * @since Version 1.1
     *
     * @param longs
     * @return
     */
    public static Long maxIndex(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        long index = 0;
        for (int i = 0; i < longs.size(); i++) {
            long l = longs.get(i);
            if (l > current) {
                current = l;
                index = i;
            }
        }
        return index;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double pythagoras(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     *
     * @since Version 1.2
     *
     * @param input
     * @return
     */
    public static long factorial(long input) {
        if (input < 0) {
            throw new MathException("Factorial of negatives is not defined");
        }
        if (input == 0) {
            return 1;
        }
        int output = 1;
        while (input > 0) {
            output *= input--;
        }
        return output;
    }

    /**
     * @see BigInteger#compareTo(BigInteger)
     * If input is bigger 100.000 it can take some time to compute.
     *
     * @since Version 1.2
     *
     * @param bigInteger
     * @return
     */
    public static BigInteger factorial(BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.ZERO) < 0) {
            throw new MathException("factorial of negatives is not defined");
        }
        BigInteger num = BigInteger.ONE;
        BigInteger b = bigInteger.add(BigInteger.ZERO);
        while (b.compareTo(BigInteger.ZERO) > 0) {
            num = num.multiply(b);
            b = b.subtract(BigInteger.ONE);
        }
        return num;
    }

}
