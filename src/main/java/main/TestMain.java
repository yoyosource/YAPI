/**
 * Copyright 2019,2020 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package main;

import yapi.math.calculator.fractions.Fraction;

import java.math.BigInteger;

public class TestMain {

    public static void main(String[] args) {
        BigInteger number = BigInteger.ONE;
        BigInteger bigInteger = BigInteger.valueOf(49);
        while (bigInteger.compareTo(BigInteger.valueOf(43)) > 0) {
            number = number.multiply(bigInteger);
            bigInteger = bigInteger.subtract(BigInteger.ONE);
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
