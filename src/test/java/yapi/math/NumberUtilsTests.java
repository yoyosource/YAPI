package yapi.math;

import org.junit.Test;

import java.math.BigDecimal;

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

    public void greatestCommonDivisorTest(long a, long b, long expected) {
        long result = NumberUtils.greatestCommonDivisor(a, b);
        assertThat(result, is(equalTo(expected)));
    }

}
