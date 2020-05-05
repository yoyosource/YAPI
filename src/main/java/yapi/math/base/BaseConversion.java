// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.base;

import java.util.Map;

public class BaseConversion {

    private static char[] base;
    private static Map<Character, Integer> base2;

    private static BaseConversionType type;

    static {
        setBase(BaseConversionType.BASE_64_SYMMETRIC);
    }

    public static void setBase(BaseConversionType type) {
        base = type.getCharArray();
        base2 = type.getIndexArray();

        BaseConversion.type = type;
    }

    public static BaseConversionType getType() {
        return type;
    }

    public static long fromBase2toLong(String base) {
        return fromBaseNtoLong(base, 2);
    }

    public static long fromBase8toLong(String base) {
        return fromBaseNtoLong(base, 8);
    }

    public static long fromBase10toLong(String base) {
        return fromBaseNtoLong(base, 10);
    }

    public static long fromBase16toLong(String base) {
        return fromBaseNtoLong(base, 16);
    }

    public static long fromBase32toLong(String base) {
        return fromBaseNtoLong(base, 32);
    }

    public static long fromBase64toLong(String base) {
        return fromBaseNtoLong(base, 64);
    }

    public static long fromBaseNtoLong(String base, int baseType) {
        checkBaseType(baseType);
        long index = 1;
        long num = 0;
        for (int i = base.length() - 1; i >= 0; i--) {
            num += index * base2.get(base.charAt(i));
            index *= baseType;
        }
        return num;
    }

    public static int fromBase2toInt(String base) {
        return fromBaseNtoInt(base, 2);
    }

    public static int fromBase8toInt(String base) {
        return fromBaseNtoInt(base, 8);
    }

    public static int fromBase10toInt(String base) {
        return fromBaseNtoInt(base, 10);
    }

    public static int fromBase16toInt(String base) {
        return fromBaseNtoInt(base, 16);
    }

    public static int fromBase32toInt(String base) {
        return fromBaseNtoInt(base, 32);
    }

    public static int fromBase64toInt(String base) {
        return fromBaseNtoInt(base, 64);
    }

    public static int fromBaseNtoInt(String base, int baseType) {
        checkBaseType(baseType);
        int index = 1;
        int num = 0;
        for (int i = base.length() - 1; i >= 0; i--) {
            num += index * base2.get(base.charAt(i));
            index *= baseType;
        }
        return num;
    }

    public static String toBase2(long i) {
        return toBaseN(i, 2);
    }

    public static String toBase8(long i) {
        return toBaseN(i, 8);
    }

    public static String toBase10(long i) {
        return toBaseN(i, 10);
    }

    public static String toBase16(long i) {
        return toBaseN(i, 16);
    }

    public static String toBase32(long i) {
        return toBaseN(i, 32);
    }

    public static String toBase64(long i) {
        return toBaseN(i, 64);
    }

    public static String toBaseN(long l, int baseType) {
        checkBaseType(baseType);
        StringBuilder st = new StringBuilder();
        while (l > 0) {
            int x = (int)(l % baseType);
            st.append(base[x]);
            l = (l - x) / baseType;
        }
        return st.reverse().toString();
    }

    public static String toBase2(int i) {
        return toBaseN(i, 2);
    }

    public static String toBase8(int i) {
        return toBaseN(i, 8);
    }

    public static String toBase10(int i) {
        return toBaseN(i, 10);
    }

    public static String toBase16(int i) {
        return toBaseN(i, 16);
    }

    public static String toBase32(int i) {
        return toBaseN(i, 32);
    }

    public static String toBase64(int i) {
        return toBaseN(i, 64);
    }

    public static String toBaseN(int i, int baseType) {
        checkBaseType(baseType);
        StringBuilder st = new StringBuilder();
        while (i > 0) {
            int x = i % baseType;
            st.append(base[x]);
            i = (i - x) / baseType;
        }
        return st.reverse().toString();
    }

    public static String fromBaseNtoBaseN(String base, int baseTypeFrom, int baseTypeTo) {
        return fromBaseNtoBaseN_Long(base, baseTypeFrom, baseTypeTo);
    }

    public static String fromBaseNtoBaseN_Long(String base, int baseTypeFrom, int baseTypeTo) {
        return toBaseN(fromBaseNtoLong(base, baseTypeFrom), baseTypeTo);
    }

    public static String fromBaseNtoBaseN_Int(String base, int baseTypeFrom, int baseTypeTo) {
        return toBaseN(fromBaseNtoInt(base, baseTypeFrom), baseTypeTo);
    }

    private static void checkBaseType(int baseType) {
        if (baseType > base.length) {
            throw new IllegalArgumentException("baseType exceeds max Base type");
        }
        if (baseType < 2) {
            throw new IllegalArgumentException("baseType exceeds min Base type");
        }
    }

    public static String fromBaseTypeToBaseType(String number, BaseConversionType typeOld, BaseConversionType typeNew) {
        StringBuilder st = new StringBuilder();
        Map<Character, Integer> characterIntegerMap = typeOld.getIndexArray();
        char[] characterMap = typeNew.getCharArray();
        for (int i = 0; i < number.length(); i++) {
            Integer index = characterIntegerMap.get(number.charAt(i));
            if (index == null) {
                throw new IllegalStateException("Source exceeds base type to convert to");
            }
            st.append(characterMap[index]);
        }
        return st.toString();
    }

    /*
    public static void main(String[] args) {
        if (true) {
            for (int i = 2; i < 1000; i++) {
                System.out.println();
                System.out.println(i);
                System.out.println(NumberUtils.over(BigInteger.valueOf(i), BigInteger.valueOf(1)));
                System.out.println(NumberUtils.over(BigInteger.valueOf(i), BigInteger.valueOf(i - 1)));
            }
            return;
        }
        WorkerPool workerPool = new WorkerPool(RuntimeUtils.availableProcessors(), RuntimeUtils.availableProcessors() * 2);
        Logging logging = new Logging("BaseConversion Test");

        for (int i = 0; i < Math.pow(2, 14); i++) {
            int l = i;
            workerPool.work(new Task() {
                @Override
                public void run() {
                    StringBuilder st = new StringBuilder();
                    st._append(l)._append(":\n");
                    for (int base = 2; base <= BaseConversion.base.length; base++) {
                        String s = toBaseN(l, base);
                        if (base != 2) {
                            st._append("\n");
                        }
                        st._append(" ".repeat(2 - (base + "").length()) + base + " > " + s);
                    }
                    logging.add(st.toString());
                }
            });
            if (i % 1000 == 0) {
                ThreadUtils.sleep(1000);
            }
        }
    }
    */

}