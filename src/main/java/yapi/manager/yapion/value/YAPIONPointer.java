// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.value;

import yapi.internal.exceptions.objectnotation.YAPIONException;
import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.YAPIONType;
import yapi.math.NumberRandom;
import yapi.string.StringFormatting;

import java.io.PrintStream;
import java.util.List;

public class YAPIONPointer extends YAPIONType {

    private String id = "";

    public YAPIONPointer(String pointer) {
        if (pointer.matches("[0-9a-z]{16}")) {
            id = pointer;
        } else {
            throw new YAPIONException();
        }
    }

    public String getId() {
        return id;
    }

    public static void main(String[] args) {
        send(YAPIONParser.parseObject("{Test(World)Hello(yoyo)Name(source)}"));
        // kjlgjlbg7e7i3GKDhhjHbfabcaDJ2hfcE8cCILAfghG9E1F20jA889ck2jAAij9b3HAiIkk71F9haIEGCh1J606gkc8bF31bIEbeHGJKkeBFC3a8Jh1Kdd0EhicAJ2Ea
        send(YAPIONParser.parseObject("{Test(World)Hello(yoyo)Name(sourcf)}"));
        // kjlgjlbg7e7i3GKDhhjHbfabcaDJ2hfcE8cCILAfghG9E1F20jA889ck2jAAij9b3HAiIkk71F9haIEGCh1J606gkc8bF31bIEbeHGJKkeBFC3a8Jh1Kdd0EhicAJ2Ea
        send(YAPIONParser.parseObject("{Test(World)Name(true)Hello(yoyo)}"));
        // jAl7hHdeba9c960bibljb179a8H76BaCBI9kG1kbhbElFjGCIhlccJec27lklf9LLJEcHEl11FelbkBgCL3Lak2kB2adHlK1LI60EiHEfcD3ALg2IJ1giJ20eadE06J0
        send(YAPIONParser.parseObject("{Test(World)Name(sourcf)Hello{Test(World)Name(sourcf)Hello(yoyo)}}"));
        // bgjBeI6FIl21LfIC9lcD377l3gGD23bIj880Hfjhf1AJI1G8GBj66DeCLJAiff7lLJAaFAiLJh8l9cAclD3B0c1Eh6ajBFJ1Ka8clkEafeldDf8EFb0G9lG0cedeG0Ba
        send(YAPIONParser.parseObject("{Test(World)Hello{Test(World)Name(sourcf)Hello(yoyo)}Name(sourcf)}"));
        // bgjBeI6FIl21LfIC9lcD377l3gGD23bIj880Hfjhf1AJI1G8GBj66DeCLJAiff7lLJAaFAiLJh8l9cAclD3B0c1Eh6ajBFJ1Ka8clkEafeldDf8EFb0G9lG0cedeG0Ba
        send(YAPIONParser.parseObject("{Test(false)Hello{Test(World)Name(sourcf)Hello(yoyo)}Name(sourcf)}"));
        // cbikbJ910j33I3JkcjfF67973kKj6jcIkC3EAJehgllFLdEaIFh88Jg82bA6f17F03CCEGdB0bah26CKk92H9cLEka9hC7JbL222CIECg8A1BH9CHh6Ef1ICdEdaI0DG
        send(YAPIONParser.parseObject("{Test(World)Hello{Test(World)Name(sourcf)Hello(yoyo)}Name(sourcf)}").getObject("Hello"));
        // EJDHFDA6dbdKfB7bAaGJilgeb12ad9CIGdkg1jEJBg0fKHK76AJBl3lgabHFG1gj9CJB38E8cJjAkHKJ1LbfcHekFDkG0kaI89Aa122jCdHL1FiH3EcljEacC1kC818F
        send(YAPIONParser.parseObject("{}"));
        // l6J8F3AHgGhBh6aIC1AEHG3D1AHfH9igDAIeC7aLdFj9Kb0A89EGclhG9fDkhJbH7LDiKglB69ihaCGGDHe1eA98ikg9D72Bci90FiJ8A0KdChgI1lbKddLIjghE1C1k
        send(YAPIONParser.parseObject("{4562440617622195218641171605700291324893228507248559930579192517899275167208677386505912811317371399778642309573594407310688704721375437998252661319722214188251994674360264950082874192246603776(Hello World)}"));
        // cHieh3a81H6D8gIafIcHdDkhjDbHfhLAbjkI9fI8GH71c3gLlhbhFdJgDh8B0HG7jC7Ff02KkhLaH6bD6HE9CbAB3gKe9akABHE2efdC2jeG6fJKiIAlIAgDLlHIgGjj
        send(YAPIONParser.parseObject("{1" + "0".repeat(63) + "1" + "0".repeat(63) + "0" + "0".repeat(63) + "(Hello World)}"));
        // e1i2eKaH2g806KIldbelghl1kE9felLA80kacFHHId0FcjfEDD6GJBKEAl28KFFFjB8kdE69kbK7GGcka3CJHaAgJEI99BiHliAKaCda0Aeb2LK0j3DiK7jiJKG8gEhI
        send(YAPIONParser.parseObject("{1" + "0".repeat(63) + "0" + "0".repeat(63) + "1" + "0".repeat(63) + "(Hello World)}"));
        // bdgBdkaC3g629eI3f1cDh3jJh28bfLHI8ilA89GLJd2FeLd0Bb7eIlJEC18g01E1jd7ie86flhIBBCaI67DhFKB2KKIL9JhFACCAa8eELIeb89JGi9E2J3e2KKH6ggfI
    }

    // 32^128 = 4562440617622195218641171605700291324893228507248559930579192517899275167208677386505912811317371399778642309573594407310688704721375437998252661319722214188251994674360264950082874192246603776

    private static void send(YAPIONObject yapionObject) {
        System.out.println(yapionObject.toString());
        System.out.println(createReferenceId(yapionObject));
    }

    private static char[] pointerChars = "01236789abcdefghijklABCDEFGHIJKL".toCharArray();
    private static NumberRandom numberRandom = new NumberRandom();

    private static int[] createBytes(int length) {
        int[] ints = new int[length];
        numberRandom.setSeed(length);
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                ints[i] += numberRandom.getInt(pointerChars.length * pointerChars.length);
            }
        }
        return ints;
    }

    /**
     * IMPORTANT: This method is not for hashing. This is just creating a Reference String for the inputted object.
     * It is not cryptographically secure and it is not intended to be secure!!
     *
     * @param yapionObject
     * @return
     */
    public static String createReferenceId(YAPIONObject yapionObject) {
        List<String> keys = yapionObject.getKeys();
        int[] ints = createBytes(64);
        int seed = ints.length * keys.size();

        YAPIONType yapionType = yapionObject.getParent();
        if (yapionType instanceof YAPIONObject) {
            String s = createReferenceId((YAPIONObject)yapionType);
            calc(ints, s);
        }

        for (String s : keys) {
            calc(ints, s);
            YAPIONType type = yapionObject.getVariable(s).getYapionType();
            calc(ints, type.getType());
            if (type instanceof YAPIONValue) {
                calc(ints, ((YAPIONValue) type).getValueType());
            }
        }
        for (int i = 0; i < ints.length; i++) {
            numberRandom.setSeed(ints[i]);
            numberRandom.skip(i);
            seed += ints[i];
        }
        calc(ints, numberRandom.setSeed(seed).getString(ints.length * keys.size()) + seed);

        return toString(ints);
    }

    private static String toString(int[] ints) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < ints.length; i++) {
            int x = ints[i] % (pointerChars.length * pointerChars.length);
            int x1 = x / pointerChars.length;
            int x2 = x % pointerChars.length;
            st.append(pointerChars[x1]).append(pointerChars[x2]);
        }
        return st.toString();
    }

    private static int[] calc(int[] ints, String s) {
        int each = pointerChars.length * pointerChars.length;

        int length = s.length() % ints.length;
        int seed = 0;
        for (int i = 0; i < s.length(); i++) {
            seed += s.charAt(i) * (i / ints.length + 1);
        }
        numberRandom.setSeed(seed);
        numberRandom.skip(s.length());
        numberRandom.skip(ints.length);

        if (s.length() % ints.length != 0) {
            s += numberRandom.getString(length + ints.length);
        } else {
            s += numberRandom.getString(ints.length);
        }

        for (int i = 0; i < s.length(); i++) {
            int index = i % ints.length;

            ints[index] += s.charAt(i);
            ints[index] %= each;
        }

        return ints;
    }

    @Override
    public String getType() {
        return "YAPIONPointer";
    }

}