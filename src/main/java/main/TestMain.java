package main;

import yapi.math.calculator.fractions.Fraction;

import java.math.BigInteger;

public class TestMain {

    public static void main(String[] args) {
        BigInteger number = new BigInteger("1");
        BigInteger bigInteger = new BigInteger("49");
        while (bigInteger.compareTo(new BigInteger("43")) > 0) {
            number = number.multiply(bigInteger);
            bigInteger = bigInteger.subtract(new BigInteger("1"));
        }
        System.out.println(Fraction.decode(number.toString() + "|" + factorial(6)).toString());
        // 13.983.816


        /*
        int start = 20000;
        for (int i = start; i < 1000000; i++) {
            Timer timer = new Timer();
            timer.start();
            NumberUtils.factorial(new BigInteger(i + ""));
            timer.stop();
            if (i % 1000 == 0) {
                System.out.println(timer.getTimeAsString() + " -> " + i);
            }
            if (timer.getTime() / 1000 / 1000 > 1000 && i != start) {
                break;
            }
        }*/
    }

    private static long factorial(long l) {
        if (l == 0) {
            return 1;
        }
        if (l > 0) {
            return l * factorial(l - 1);
        }
        throw new NumberFormatException("No negatives");
    }

}
