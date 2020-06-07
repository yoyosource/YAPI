// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.distributions;

import yapi.math.Fraction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MathHomework {

    private static void test3() {
        Fraction fraction = Fraction.decode("8856579168360000|2221591525865712000");
        System.out.println(fraction.encodePercent());
        System.exit(0);
    }

    private static void test2() {

        List<Integer> done = new ArrayList<>();
        int count = 0;
        int countIndex = 13;
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                /*if (i * j == countIndex) {

                    count++;
                }*/
                for (int k = 1; k < 7; k++) {
                    if (i + j + k == countIndex) {
                        count++;
                        System.out.print("(" + i + "|" + j + "|" + k + "), ");
                    }
                    if (!done.contains(i + j + k)) {
                        done.add(i + j + k);
                        //System.out.print((i + j + k) + ", ");
                    }
                    //System.out.print("(" + i + "|" + j + "|" + k + "), ");
                }
            }
        }
        System.out.println();
        System.out.println(countIndex + " " + count);
        System.exit(0);
    }

    // create 91000 1|7
    // betweenAndEqual 12995 13105

    public static void main(String[] args) {
        //BigDecimal value = new BigDecimal(NumberUtils.over(BigInteger.valueOf(9), BigInteger.valueOf(4))).divide(new BigDecimal(NumberUtils.over(BigInteger.valueOf(36), BigInteger.valueOf(4))), new MathContext(100, RoundingMode.HALF_UP));
        //Fraction fr = new Fraction(value);
        //System.out.println(fr.encode("%"));
        //test3();
        //test2();
        test();
        if (false) {
            System.out.println("Seite 289");
            System.out.println("Number 1a");
            number1A();
            System.out.println("\nNumber 1b");
            number1B();
            System.out.println("\nNumber 2");
            number2();
            System.out.println("\nNumber 3");
            number3();
        }
        if (false) {
            System.out.println("Seite 284");
            BinomialDistribution binomialDistribution = new BinomialDistribution(10, 8 / 10.0);
            BigDecimal[] doubles = binomialDistribution.getBinomial();
            System.out.println(Arrays.toString(doubles));

            BigDecimal fractionDouble = binomialDistribution.getProbability(10);
            //double fractionDouble = binomialDistribution.getProbability(10);
            Fraction fraction = new Fraction(fractionDouble);
            System.out.println(fraction.encodeFlat() + "   " + fraction.encodePercent() + "   " + fraction.encodeMixed() + "   " + fraction.encodeNumber());
        }
    }

    public static void test() {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        BinomialDistribution binomialDistribution = new BinomialDistribution(0, 0);
        while (s != null && !(s.equals("exit") || s.equals("close"))) {
            String[] strings = s.split(" ");
            BigDecimal[] solutions = new BigDecimal[0];
            if (strings[0].equalsIgnoreCase("create") && strings.length == 3) {
                try {
                    long size = Long.parseLong(strings[1]);
                    Fraction fraction = Fraction.valueOf(strings[2]);
                    double probability = fraction.getDoubleValue();

                    binomialDistribution = new BinomialDistribution(size, probability);
                    System.out.println("> " + "Created Binomial Distribution with size=" + size + " and probability=" + probability);
                } catch (NumberFormatException e) {
                    System.out.println("> " + "Please give 2 numbers after '" + strings[0] + "'");
                }
            }

            if (strings[0].equalsIgnoreCase("binomial") && strings.length == 1) {
                solutions = binomialDistribution.getBinomial();
            }
            if (strings[0].equalsIgnoreCase("function") && strings.length == 1) {
                solutions = binomialDistribution.getFunction();
            }
            if (strings[0].equalsIgnoreCase("binomial") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getBinomial(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> " + "Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("above") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityAbove(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("aboveAndEqual") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityAboveAndEqual(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("below") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityBelow(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("belowAndEqual") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityBelowAndEqual(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("equal") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityEqual(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("notEqual") && strings.length == 2) {
                try {
                    long experiment = Long.parseLong(strings[1]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityNotEqual(experiment)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 1 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("between") && strings.length == 3) {
                try {
                    int x1 = Integer.parseInt(strings[1]);
                    int x2 = Integer.parseInt(strings[2]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityBetween(x1, x2)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 2 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("betweenAndEqual") && strings.length == 3) {
                try {
                    int x1 = Integer.parseInt(strings[1]);
                    int x2 = Integer.parseInt(strings[2]);

                    solutions = new BigDecimal[]{binomialDistribution.getProbabilityBetweenAndEqual(x1, x2)};
                } catch (NumberFormatException e) {
                    System.out.println("> Please give 2 numbers after '" + strings[0] + "'");
                }
            }
            if (strings[0].equalsIgnoreCase("mu") || strings[0].equalsIgnoreCase("expectedValue") && strings.length == 1) {
                solutions = new BigDecimal[]{BigDecimal.valueOf(binomialDistribution.getMu())};
            }
            if (strings[0].equalsIgnoreCase("sigma") || strings[0].equalsIgnoreCase("expectedDeviation") && strings.length == 1) {
                solutions = new BigDecimal[]{BigDecimal.valueOf(binomialDistribution.getSigma())};
            }

            if (solutions.length != 0) {
                if (solutions.length == 1) {
                    Fraction fraction = new Fraction(solutions[0]);
                    System.out.println("> " + fraction.encodeFlat() + "\n  " + fraction.encodePercent() + "\n  " + fraction.encodeMixed() + "\n  " + fraction.encodeNumber());
                } else {
                    for (int i = 0; i < solutions.length; i++) {
                        Fraction fraction = new Fraction(solutions[i]);
                        System.out.println("> Index: " + i + "\n  " + fraction.encodeFlat() + "\n  " + fraction.encodePercent() + "\n  " + fraction.encodeMixed() + "\n  " + fraction.encodeNumber());
                    }
                }
            }

            s = scanner.nextLine();
        }
    }

    public static void number1A() {
        BinomialDistribution binomialDistribution = new BinomialDistribution(50, 0.05);
        System.out.println(binomialDistribution.getProbability(4));
        System.out.println(binomialDistribution.getProbabilityBelowAndEqual(4));
        System.out.println(binomialDistribution.getProbabilityAboveAndEqual(3));
        System.out.println(binomialDistribution.getProbabilityBetweenAndEqual(1, 5));
        System.out.println(binomialDistribution.getProbabilityBelow(1).add(binomialDistribution.getProbabilityAbove(5)));
    }

    public static void number1B() {
        BinomialDistribution binomialDistribution = new BinomialDistribution(100, 0.03);
        System.out.println(binomialDistribution.getProbability(4));
        System.out.println(binomialDistribution.getProbabilityBelowAndEqual(4));
        System.out.println(binomialDistribution.getProbabilityAboveAndEqual(3));
        System.out.println(binomialDistribution.getProbabilityBetweenAndEqual(1, 5));
        System.out.println(binomialDistribution.getProbabilityBelow(1).add(binomialDistribution.getProbabilityAbove(5)));
    }

    public static void number2() {
        BinomialDistribution binomialDistribution = new BinomialDistribution(25, 0.2);
        System.out.println(binomialDistribution.getProbability(5));
        System.out.println(binomialDistribution.getProbabilityBetween(4, 6));
        System.out.println(binomialDistribution.getProbabilityBelow(5));
        System.out.println(binomialDistribution.getProbabilityAbove(5));
        System.out.println(binomialDistribution.getMu());
        System.out.println(binomialDistribution.getSigma() * 2);
        System.out.println();
        binomialDistribution = new BinomialDistribution(7, 0.2);
        System.out.println(binomialDistribution.getMu());
        System.out.println(binomialDistribution.getSigma());
    }

    public static void number3() {
        BinomialDistribution binomialDistribution = new BinomialDistribution(20, 0.5);
        System.out.println("1,4");
        System.out.println(binomialDistribution.getBinomial(10));
        System.out.println(binomialDistribution.getFunction(10));
        System.out.println("2,5");
        System.out.println(binomialDistribution.getBinomial(11));
        System.out.println(binomialDistribution.getFunction(11));
        System.out.println("3,6");
        System.out.println(binomialDistribution.getBinomial(15));
        System.out.println(binomialDistribution.getFunction(15));
    }

}