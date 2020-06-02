// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.math.NumberUtils;

public class KeyStrength {

    private KeyStrength() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @param password Your password to check
     * @return your password strength as a double between 0 and 1 calculated by the sigmoid() function with the strength
     */
    public static double passwordStrength(String password) {
        return passwordStrength(password, false);
    }

    /**
     * @param password Your password to check
     * @param rawStrength if you want the sgimoid() or the double value beforehand
     * @return your password strength as a double between 0 and 1 calculated by the sigmoid() function with the strength
     */
    public static double passwordStrength(String password, boolean rawStrength) {
        double strength = 0;
        long length = password.length();
        strength += length * 4;

        long consecutive = 0;
        long consecutiveNumber = 0;

        long upperCase = 0;
        long upperCaseC = 0;
        long lowerCase = 0;
        long lowerCaseC = 0;
        long numbers = 0;
        long numbersC = 0;
        long symbols = 0;
        long symbolsC = 0;
        long middleNumbersOrSymbols = 0;
        long sequencialNumbers = 0;
        long sequencialAlphabet = 0;

        StringBuilder repeatedChars = new StringBuilder();
        long repeatedCharsNumber = 0;

        char lC = '\u0000';

        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (repeatedChars.toString().contains(c + "")) {
                repeatedCharsNumber++;
            }
            if (i > 0) {
                lC = password.charAt(i - 1);
            }
            repeatedChars.append(Character.toLowerCase(c));

            if (c >= 'A' && c <= 'Z') {
                if (c - lC <= 1 && c - lC >= -1) {
                    sequencialAlphabet++;
                    if (c == lC) {
                        sequencialAlphabet++;
                    }
                }
                upperCase++;
                if (lC >= 'A' && lC <= 'Z') {
                    upperCaseC++;
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
            if (c >= 'a' && c <= 'z') {
                if (c - lC <= 1 && c - lC >= -1) {
                    sequencialAlphabet++;
                    if (c == lC) {
                        sequencialAlphabet++;
                    }
                }
                lowerCase++;
                if (lC >= 'a' && lC <= 'z') {
                    lowerCaseC++;
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
            if (c >= '0' && c <= '9') {
                if (c - lC <= 1 && c - lC >= -1) {
                    sequencialNumbers++;
                    if (c == lC) {
                        sequencialNumbers++;
                    }
                }
                numbers++;
                if (i != 0 && i != length - 1) {
                    middleNumbersOrSymbols++;
                }
                if (lC >= '0' && lC <= '9') {
                    numbersC++;
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
            if ((c >= '!' && c <= '/') || (c >= ':' && c <= '@') || (c >= '[' && c <= '`') || (c >= '{' && c <= '~')) {
                symbols++;
                if (i != 0 && i != length - 1) {
                    middleNumbersOrSymbols++;
                }
                if ((lC >= '!' && lC <= '/') || (lC >= ':' && lC <= '@') || (lC >= '[' && lC <= '`') || (lC >= '{' && lC <= '~')) {
                    symbolsC++;
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
            if (consecutive >= 3) {
                consecutiveNumber++;
            }
        }

        if (upperCase > 0 && upperCase < length) {
            strength += (length - upperCase) * 2;
        }
        if (lowerCase > 0 && lowerCase < length) {
            strength += (length - lowerCase) * 2;
        }
        if (numbers > 0 && numbers < length) {
            strength += numbers * 4;
        }
        strength += symbols * 6;
        strength += middleNumbersOrSymbols * 2;

        if (symbols == 0 && numbers == 0) {
            strength -= length;
        }
        if (symbols == 0 && upperCase == 0 && lowerCase == 0) {
            strength -= length;
        }

        strength -= upperCaseC * 2;
        strength -= lowerCaseC * 2;
        strength -= numbersC * 2;
        strength -= symbolsC * 2;

        strength -= consecutiveNumber * 3;
        strength -= repeatedCharsNumber;

        strength -= sequencialNumbers * 2;
        strength -= sequencialAlphabet * 2;
        if (rawStrength) {
            return strength;
        }
        return NumberUtils.sigmoid(strength - 50);
    }

}