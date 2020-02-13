// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.fractions;

import yapi.math.NumberUtils;
import yapi.string.StringFormatting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class Fraction {

    /**
     * Fraction Presets
     */
    public static final Fraction NEGATION = new Fraction(-1, 0);
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction TWO = new Fraction(2, 1);
    public static final Fraction TEN = new Fraction(10, 1);

    private static MathContext mathContext = new MathContext(100, RoundingMode.HALF_UP);

    private BigInteger numerator;
    private BigInteger denominator;
    private boolean mixed = false;
    private boolean percent = false;

    public Fraction(long number) {
        numerator = BigInteger.valueOf(number);
        denominator = BigInteger.valueOf(1);
        shorten();
    }

    public Fraction(long numerator, long denominator) {
        this.numerator = BigInteger.valueOf(numerator);
        this.denominator = BigInteger.valueOf(denominator);
        shorten();
    }

    public Fraction(double d) {
        this(BigDecimal.valueOf(d));
    }

    public Fraction(BigInteger number) {
        this.numerator = number;
        denominator = BigInteger.valueOf(1);
        shorten();
    }

    public Fraction(BigDecimal bigDecimal) {
        BigInteger bigInteger = BigInteger.ONE;
        boolean working = true;
        while (working) {
            try {
                // This method call is done to check if the BigDecimal can be a BigInteger by trying it and catching a exception if it did not work.
                bigDecimal.toBigIntegerExact();
                working = false;
            } catch (ArithmeticException e) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
                bigInteger = bigInteger.multiply(BigInteger.TEN);
            }
        }
        numerator = bigDecimal.toBigInteger();
        denominator = bigInteger;
        shorten();
    }

    private Fraction(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        shorten();
    }

    public static Fraction decode(String number) {
        number = number.replace(",", "");
        if (number.endsWith("%") && !number.contains("|")) {
            try {
                return new Fraction(Long.parseLong(number.substring(0, number.length() - 1)), 100).setPercent(true);
            } catch (NumberFormatException e) {
                // Ignore
            }
            try {
                return new Fraction(new BigDecimal(number.substring(0, number.length() - 1)).divide(BigDecimal.valueOf(100), mathContext)).setPercent(true);
            } catch (NumberFormatException e) {
                // Ignore
            }
            throw new NumberFormatException("No fraction input.");
        }
        if (!number.contains("|")) {
            try {
                return new Fraction(new BigDecimal(number));
            } catch (NumberFormatException e) {
                // Ignore
            }
            try {
                return new Fraction(new BigInteger(number));
            } catch (NumberFormatException e) {
                // Ignore
            }
            throw new NumberFormatException("No fraction input.");
        }
        int i = StringFormatting.occurrences(number, '|');
        if (!(i == 2 || i == 3) || number.startsWith("(") || number.endsWith(")")) {
            throw new NumberFormatException("No fraction input.");
        }
        String[] strings = number.split("\\|");
        if (strings.length == 3) {
            BigInteger denominator = new BigInteger(strings[2]);
            return new Fraction(denominator.multiply(new BigInteger(strings[0])).add(new BigInteger(strings[1])), denominator);
        }
        return new Fraction(new BigInteger(strings[0]), new BigInteger(strings[1]));
    }

    public static Fraction valueOf(byte i) {
        return new Fraction(i);
    }

    public static Fraction valueOf(short i) {
        return new Fraction(i);
    }

    public static Fraction valueOf(int i) {
        return new Fraction(i);
    }

    public static Fraction valueOf(long l) {
        return new Fraction(l);
    }

    public static Fraction valueOf(double d) {
        return new Fraction(d);
    }

    public static Fraction valueOf(float f) {
        return new Fraction(f);
    }

    private void shorten() {
        BigInteger divisor;
        do {
            divisor = numerator.gcd(denominator);
            numerator = numerator.divide(divisor);
            denominator = denominator.divide(divisor);
        } while (!divisor.toString().equals("1"));
    }

    public Fraction add(Fraction fraction) {
        if (fraction.denominator.compareTo(denominator) == 0) {
            return new Fraction(fraction.numerator.add(numerator), denominator).setPercent(percent || fraction.percent);
        } else {
            return new Fraction(fraction.numerator.multiply(denominator).add(numerator.multiply(fraction.denominator)), fraction.denominator.multiply(denominator)).setPercent(percent || fraction.percent);
        }
    }

    public Fraction subtract(Fraction fraction) {
        if (fraction.denominator.equals(denominator)) {
            return new Fraction(numerator.subtract(fraction.numerator), denominator).setPercent(percent || fraction.percent);
        } else {
            return new Fraction(numerator.multiply(fraction.denominator).subtract(fraction.numerator.multiply(denominator)), fraction.denominator.multiply(denominator)).setPercent(percent || fraction.percent);
        }
    }

    public Fraction multiply(Fraction fraction) {
        return new Fraction(fraction.numerator.multiply(numerator), fraction.denominator.multiply(denominator)).setPercent(percent || fraction.percent);
    }

    public Fraction divide(Fraction fraction) {
        return copy().multiply(fraction.reciprocal()).setPercent(percent || fraction.percent);
    }

    public Fraction remainder(Fraction fraction) {
        return new Fraction(numerator.remainder(fraction.numerator), denominator.remainder(fraction.denominator));
    }

    public Fraction square() {
        return this.multiply(this);
    }

    public Fraction cube() {
        return this.multiply(this).multiply(this);
    }

    public Fraction power(long n) {
        if (n < 0) {
            return Fraction.ONE.divide(power(n * -1));
        }
        if (n == 0) {
            return Fraction.ONE;
        }
        if (n == 1) {
            return copy();
        }
        if (n == 2) {
            return square();
        }
        if (n == 3) {
            return cube();
        }
        Fraction f = copy();
        while (n > 1) {
            f = f.multiply(this);
            n--;
        }
        return f;
    }

    public Fraction reciprocal() {
        return new Fraction(denominator, numerator);
    }

    public Fraction copy() {
        return new Fraction(numerator, denominator);
    }

    public Fraction setMixed(boolean mixed) {
        this.mixed = mixed;
        return this;
    }

    private Fraction setPercent(boolean percent) {
        this.percent = percent;
        return this;
    }

    private String checkEncoding() {
        if (denominator.toString().equals("1")) {
            return numerator + "";
        }
        if (denominator.toString().equals("0")) {
            return "NaN";
        }
        if (denominator.toString().equals("-1")) {
            return numerator.negate() + "";
        }
        return "";
    }

    public String encode() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        if (percent) {
            return encodePercent();
        }
        if (numerator.compareTo(denominator) > 0 && mixed) {
            return encodeMixed();
        }
        return encodeFlat();
    }

    public String encode(String format) {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        if (format.equals("%")) {
            return encodePercent();
        } else if (format.equals("||")) {
            return encodeMixed();
        } else if (format.equals("|") || format.equals("_")) {
            return encodeFlat();
        } else if (format.equals(".") || format.equals("0.") || format.equals(".0") || format.equals("0.0")) {
            return encodeNumber();
        } else {
            return encode();
        }
    }

    public String encodePercent() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return (num.multiply(BigDecimal.valueOf(100).divide(den, mathContext))) + "%";
    }

    public String encodeMixed() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return num.subtract(num.remainder(den, mathContext)).divide(den, mathContext) + "|" + addCommas(num.remainder(den, mathContext)) + "|" + addCommas(den);
    }

    public String encodeFlat() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        return addCommas(numerator) + "|" + addCommas(denominator);
    }

    public String encodeNumber() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return num.divide(den, mathContext).toPlainString();
    }

    private String addCommas(BigDecimal bigDecimal) {
        return NumberUtils.formatNumber(bigDecimal);
    }

    private String addCommas(BigInteger bigInteger) {
        return NumberUtils.formatNumber(bigInteger);
    }

    @Override
    public String toString() {
        return "Fraction{" +
                "numerator=" + numerator +
                ", denominator=" + denominator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;
        Fraction fraction = (Fraction) o;
        return Objects.equals(numerator, fraction.numerator) &&
                Objects.equals(denominator, fraction.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}