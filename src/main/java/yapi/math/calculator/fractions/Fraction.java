// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.fractions;

import ch.obermuhlner.math.big.BigDecimalMath;
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

    private static MathContext mathContext = new MathContext(200, RoundingMode.HALF_UP);

    public static void setPrecision(int precision) {
        if (precision == 0) {
            defaultPrecision();
            return;
        }
        mathContext = new MathContext(precision, RoundingMode.HALF_UP);
    }

    public static void setCalculationPrecision(int precision) {
        setPrecision(precision);
    }

    public static void defaultPrecision() {
        mathContext = new MathContext(200, RoundingMode.HALF_UP);
    }

    private BigInteger numerator;
    private BigInteger denominator;
    private boolean mixed = false;
    private boolean percent = false;

    private boolean shorten = true;

    public Fraction(long number) {
        numerator = BigInteger.valueOf(number);
        denominator = BigInteger.valueOf(1);
        shorten();
    }

    public Fraction(long number, boolean shorten) {
        numerator = BigInteger.valueOf(number);
        denominator = BigInteger.valueOf(1);
        this.shorten = shorten;
        shorten();
    }

    public Fraction(long numerator, long denominator) {
        this.numerator = BigInteger.valueOf(numerator);
        this.denominator = BigInteger.valueOf(denominator);
        shorten();
    }

    public Fraction(long numerator, long denominator, boolean shorten) {
        this.numerator = BigInteger.valueOf(numerator);
        this.denominator = BigInteger.valueOf(denominator);
        this.shorten = shorten;
        shorten();
    }

    public Fraction(double d) {
        this(BigDecimal.valueOf(d));
    }

    public Fraction(double d, boolean shorten) {
        this(BigDecimal.valueOf(d), shorten);
    }

    public Fraction(BigInteger number) {
        this.numerator = number;
        denominator = BigInteger.valueOf(1);
        shorten();
    }

    public Fraction(BigInteger number, boolean shorten) {
        this.numerator = number;
        denominator = BigInteger.valueOf(1);
        this.shorten = shorten;
        shorten();
    }

    public Fraction(BigDecimal bigDecimal) {
        BigInteger bigInteger = BigInteger.ONE;
        while (true) {
            try {
                numerator = bigDecimal.toBigIntegerExact();
                denominator = bigInteger;
                shorten();
                break;
            } catch (ArithmeticException e) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
                bigInteger = bigInteger.multiply(BigInteger.TEN);
            }
        }
    }

    public Fraction(BigDecimal bigDecimal, boolean shorten) {
        BigInteger bigInteger = BigInteger.ONE;
        while (true) {
            try {
                numerator = bigDecimal.toBigIntegerExact();
                denominator = bigInteger;
                this.shorten = shorten;
                shorten();
                break;
            } catch (ArithmeticException e) {
                bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
                bigInteger = bigInteger.multiply(BigInteger.TEN);
            }
        }
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
        if (!(i == 1 || i == 2) || number.startsWith("(") || number.endsWith(")")) {
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

    public static Fraction valueOf(String s) {
        return decode(s);
    }

    private void shorten() {
        if (!shorten) {
            return;
        }
        if (numerator.compareTo(BigInteger.ZERO) == 0 && denominator.compareTo(BigInteger.ZERO) == 0) {
            return;
        }
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
        if (fraction.denominator.compareTo(denominator) == 0) {
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

    private BigDecimal getBigDecimal() {
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return num.divide(den, mathContext);
    }

    public Fraction square() {
        return this.multiply(this);
    }

    public Fraction cube() {
        return this.multiply(this).multiply(this);
    }

    public Fraction power(Fraction f) {
        Fraction r = power(f.numerator);
        if (f.denominator.compareTo(BigInteger.ONE) == 0) {
            return r;
        }
        return new Fraction(BigDecimalMath.root(getBigDecimal(), new BigDecimal(f.denominator), mathContext));
    }

    public Fraction power(BigDecimal bigDecimal) {
        return power(new Fraction(bigDecimal));
    }

    public Fraction power(BigInteger bigInteger) {
        if (bigInteger.signum() == -1) {
            return Fraction.ONE.divide(power(bigInteger.multiply(BigInteger.valueOf(-1))));
        }
        if (bigInteger.signum() == 0) {
            return Fraction.ONE;
        }
        if (bigInteger.compareTo(BigInteger.ONE) == 0) {
            return copy();
        }
        if (bigInteger.compareTo(BigInteger.TWO) == 0) {
            return square();
        }
        if (bigInteger.compareTo(BigInteger.valueOf(3)) == 0) {
            return cube();
        }
        Fraction f = copy();
        BigInteger b = bigInteger.add(BigInteger.ZERO);
        while (b.compareTo(BigInteger.ONE) > 0) {
            f = f.multiply(this);
            b = b.subtract(BigInteger.ONE);
        }
        return f;
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

    public Fraction sin() {
        return new Fraction(BigDecimalMath.sin(getBigDecimal(), mathContext));
    }

    public Fraction asin() {
        return new Fraction(BigDecimalMath.asin(getBigDecimal(), mathContext));
    }

    public Fraction cos() {
        return new Fraction(BigDecimalMath.cos(getBigDecimal(), mathContext));
    }

    public Fraction cosh() {
        return new Fraction(BigDecimalMath.cosh(getBigDecimal(), mathContext));
    }

    public Fraction cot() {
        return new Fraction(BigDecimalMath.cot(getBigDecimal(), mathContext));
    }

    public Fraction coth() {
        return new Fraction(BigDecimalMath.coth(getBigDecimal(), mathContext));
    }

    public Fraction acos() {
        return new Fraction(BigDecimalMath.acos(getBigDecimal(), mathContext));
    }

    public Fraction acosh() {
        return new Fraction(BigDecimalMath.acosh(getBigDecimal(), mathContext));
    }

    public Fraction acot() {
        return new Fraction(BigDecimalMath.acot(getBigDecimal(), mathContext));
    }

    public Fraction acoth() {
        return new Fraction(BigDecimalMath.acoth(getBigDecimal(), mathContext));
    }

    public Fraction tan() {
        return new Fraction(BigDecimalMath.tan(getBigDecimal(), mathContext));
    }

    public Fraction tanh() {
        return new Fraction(BigDecimalMath.tanh(getBigDecimal(), mathContext));
    }

    public Fraction atan() {
        return new Fraction(BigDecimalMath.atan(getBigDecimal(), mathContext));
    }

    public Fraction atanh() {
        return new Fraction(BigDecimalMath.atanh(getBigDecimal(), mathContext));
    }

    public Fraction log() {
        return new Fraction(BigDecimalMath.log(getBigDecimal(), mathContext));
    }

    public Fraction log2() {
        return new Fraction(BigDecimalMath.log2(getBigDecimal(), mathContext));
    }

    public Fraction log10() {
        return new Fraction(BigDecimalMath.log10(getBigDecimal(), mathContext));
    }

    public Fraction exp() {
        return new Fraction(BigDecimalMath.exp(getBigDecimal(), mathContext));
    }

    public Fraction reciprocal() {
        return new Fraction(denominator, numerator);
    }

    public Fraction reciprocalDivision() {
        return new Fraction(BigDecimal.ONE.divide(getBigDecimal(), mathContext));
    }

    public double getDoubleValue() {
        return getBigDecimal().doubleValue();
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

    public Fraction setShorten(boolean shorten) {
        this.shorten = shorten;
        if (shorten) {
            shorten();
        }
        return this;
    }

    private String checkEncoding() {
        if (denominator.toString().equals("1")) {
            return addCommas(numerator);
        }
        if (denominator.toString().equals("0")) {
            return "NaN";
        }
        if (denominator.toString().equals("-1")) {
            return addCommas(numerator.negate());
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
        } else if (format.equals("e") || format.equals("E")) {
            return encodeEngineering();
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
        return addCommas(num.multiply(BigDecimal.valueOf(100).divide(den, mathContext))) + "%";
    }

    public String encodeMixed() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return addCommas(num.subtract(num.remainder(den, mathContext)).divide(den, mathContext)) + "|" + addCommas(num.remainder(den, mathContext)) + "|" + addCommas(den);
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
        return addCommas(num.divide(den, mathContext));
    }

    public String encodeEngineering() {
        String check = checkEncoding();
        if (!check.isEmpty()) {
            return check;
        }
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return addCommas(num.divide(den, mathContext));
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