package yapi.math;

import org.junit.Test;
import yapi.array.ArrayUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class NumberUtilsTests {

    @Test
    public void roundTest() {
        roundTest(0.9, 0, 1.0);
        roundTest(0.4, 0, 0.0);
        roundTest(0.5, 0, 1.0);
        roundTest(0.45, 1, 0.5);
        roundTest(0.44, 1, 0.4);

        roundTest(BigDecimal.valueOf(0.9), 0, BigDecimal.valueOf(1));
        roundTest(BigDecimal.valueOf(0.4), 0, BigDecimal.valueOf(0));
        roundTest(BigDecimal.valueOf(0.5), 0, BigDecimal.valueOf(1));
        roundTest(BigDecimal.valueOf(0.45), 1, BigDecimal.valueOf(0.5));
        roundTest(BigDecimal.valueOf(0.44), 1, BigDecimal.valueOf(0.4));

    }

    private void roundTest(double d, int digits, double expected) {
        double result = NumberUtils.round(d, digits);
        assertThat(result, is(equalTo(expected)));
    }

    private void roundTest(BigDecimal d, int digits, BigDecimal expected) {
        BigDecimal result = NumberUtils.round(d, digits);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void primeTest() {
        primeTest(-1, false);
        primeTest(1, false);
        primeTest(2, true);
        primeTest(3, true);
        primeTest(4, false);
        primeTest(5, true);
        primeTest(6, false);
        primeTest(7, true);
    }

    private void primeTest(long l, boolean expected) {
        boolean result = NumberUtils.isPrime(l);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void fibonacciTest() {
        fibonacciTest(1, true);
        fibonacciTest(2, true);
        fibonacciTest(3, true);
        fibonacciTest(4, false);
        fibonacciTest(5, true);
        fibonacciTest(6, false);
        fibonacciTest(7, false);
        fibonacciTest(8, true);
    }

    private void fibonacciTest(long l, boolean expected) {
        boolean result = NumberUtils.isFibonacci(l);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void squareTest() {
        squareTest1(1, true);
        squareTest1(2, false);
        squareTest1(3, false);
        squareTest1(4, true);
        squareTest1(5, false);
        squareTest1(6, false);
        squareTest1(7, false);
        squareTest1(8, false);
        squareTest1(9, true);

        squareTest2(1, true);
        squareTest2(2, false);
        squareTest2(3, false);
        squareTest2(4, true);
        squareTest2(5, false);
        squareTest2(6, false);
        squareTest2(7, false);
        squareTest2(8, false);
        squareTest2(9, true);
    }

    private void squareTest1(long l, boolean expected) {
        boolean result = NumberUtils.isSquare(l);
        assertThat(result, is(equalTo(expected)));
    }

    private void squareTest2(long l, boolean expected) {
        boolean result = NumberUtils._isSquare(l);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void primesTest() {
        Long[] result = NumberUtils.getPrimes(7).toArray(new Long[0]);
        assertThat(result, is(equalTo(new Long[]{2L, 3L, 5L, 7L})));
    }

    @Test
    public void greatestCommonDivisorTest() {
        greatestCommonDivisorTest(6, 1, 1);
        greatestCommonDivisorTest(6, 2, 2);
        greatestCommonDivisorTest(12, 6, 6);
    }

    private void greatestCommonDivisorTest(long a, long b, long expected) {
        long result = NumberUtils.greatestCommonDivisor(a, b);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void leastCommonMultipleTest() {
        leastCommonMultipleTest(12, 6, 12);
        leastCommonMultipleTest(18, 6, 18);
        leastCommonMultipleTest(18, 7, 126);
    }

    private void leastCommonMultipleTest(long a, long b, long expected) {
        long result = NumberUtils.leastCommonMultiple(a, b);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void greatestCommonFactorTest() {
        greatestCommonFactorTest(102, 2, 2);
        greatestCommonFactorTest(77, 4, 1);
        greatestCommonFactorTest(108, 64, 4);
        greatestCommonFactorTest(128, 64, 64);
    }

    private void greatestCommonFactorTest(long a, long b, long expected) {
        long result = NumberUtils.greatestCommonDivisor(a, b);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void areCoPrimeTest() {
        areCoPrimeTest(10, 5, false);
        areCoPrimeTest(11, 7, true);
        areCoPrimeTest(101, 1, true);
    }

    private void areCoPrimeTest(long a, long b, boolean expected) {
        boolean result = NumberUtils.areCoPrime(a, b);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void nextPrimeTest() {
        nextPrimeTest(100, 101);
        nextPrimeTest(1, 2);
        nextPrimeTest(19, 23);
    }

    private void nextPrimeTest(long a, long expected) {
        long result = NumberUtils.nextPrime(a);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void primeFactorizationTest() {
        primeFactorizationTest(10, 2, 5);
        primeFactorizationTest(101, 101);
        primeFactorizationTest(3465, 3, 3, 5, 7, 11);
    }

    private void primeFactorizationTest(long a, long... expected) {
        List<Long> longs = NumberUtils.primeFactorization(a);
        assertThat(longs, is(equalTo(ArrayUtils.asList(expected))));
    }

    @Test
    public void getDivisorsSortedTest() {
        getDivisorsSortedTest(100, 1, 2, 4, 5, 10, 20, 25, 50, 100);
        getDivisorsSortedTest(4040, 1, 2, 4, 5, 8, 10, 20, 40, 101, 202, 404, 505, 808, 1010, 2020, 4040);
        getDivisorsSortedTest(10, 1, 2, 5, 10);
        getDivisorsSortedTest(7, 1, 7);

        getDivisorsTest(100, 1, 100, 2, 50, 4, 25, 5, 20, 10);
        getDivisorsTest(4040, 1, 4040, 2, 2020, 4, 1010, 5, 808, 8, 505, 10, 404, 20, 202, 40, 101);
        getDivisorsTest(10, 1, 10, 2, 5);
        getDivisorsTest(7, 1, 7);
    }

    private void getDivisorsSortedTest(long a, long... expected) {
        List<Long> longs = NumberUtils.getDivisorsSorted(a);
        assertThat(longs, is(equalTo(ArrayUtils.asList(expected))));
    }

    private void getDivisorsTest(long a, long... expected) {
        List<Long> longs = NumberUtils.getDivisors(a);
        assertThat(longs, is(equalTo(ArrayUtils.asList(expected))));
    }

    @Test
    public void sumTest() {
        sumTest("1...10", 55);
        sumTest("1...100", 5050);
        sumTest("1...324", 52650);

        addTest(10, 2, 5, 3);
        addTest(100, 20, 50, 30);

        addTest("1...10", 55);
        addTest("1...100", 5050);
        addTest("1...324", 52650);
    }

    private void sumTest(String a, long expected) {
        long result = NumberUtils.sum(a);
        assertThat(result, is(equalTo(expected)));
    }

    private void addTest(long expected, long... longs) {
        long result = NumberUtils.add(ArrayUtils.asList(longs));
        assertThat(result, is(equalTo(expected)));
    }

    private void addTest(String a, long expected) {
        long result = NumberUtils.add(a);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void subtractTest() {
        subtractTest(-10, 5, 3, 2);
        subtractTest(-100, 50, 30, 20);
    }

    private void subtractTest(long expected, long... longs) {
        long result = NumberUtils.subtract(ArrayUtils.asList(longs));
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void multiplyTest() {
        multiplyTest(10, 2, 5);
        multiplyTest(100, 2, 5, 5, 2);
    }

    private void multiplyTest(long expected, long... longs) {
        long result = NumberUtils.multiply(ArrayUtils.asList(longs));
        assertThat(result, is(equalTo(expected)));
    }

}
