// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.general;

import yapi.math.NumberUtils;
import yapi.string.StringFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StringCompression {

    public static void main(String[] args) {
        if (true) {
            StringCompression stringCompression = new StringCompression();
            byte[] bytes;
            //bytes = stringCompression.compress("Danke   ".repeat(1024 * 512));
            //bytes = stringCompression.compress("Danke   ".repeat(1024 * 1));
            //bytes = stringCompression.compress((StringUtils.toHex(StringUtils.hash("Hallo InProgressing")).repeat(64) + StringUtils.toHex(StringUtils.hash("Hallo inProgressing")).repeat(64)));
            //bytes = stringCompression.compress(("❤❤❤ " + (("Danke ❤❤❤ ".repeat(10)).repeat(10)).repeat(10).trim()));
            //bytes = stringCompression.compress(StringUtils.merge(FileUtils.fileContentFromResourceAsString("main/yapi.info"), "\n"));
            bytes = stringCompression.compress("Eigentum (Schutz leben, Freiheiten und Vermögen)\ngrößter Grund ^ zu zusammeschließung\nes fehlt feststehend und geordneten und bekannten Gesetz\n-> allgemeine Zustimmung\n-> Norm für Recht und Unrecht\n-> allgemeine Maßstab zur Entscheidung ihrer Streitigkeiten (angenommen und anerkannt)\nGesetz Natur ist klar\n-> Interesse beeinflusst\n-> nicht nachdenken => zu wenig kennen -> keine Rechtsanerkennung, keine anwendungsverpfilchtung gesehen\nes feht anerkannte und unparteiische Richter\n-> Recht alle zwistigkeiten nach dem festehenden Gesetz zu entscheiden\n=> Jeder Richter und Vollzieher dieses Gesetztes; Menschen sich selbst gegenüber parteiisch\n-> eigenen Sache Leidenschaft und Rache zu weit fortreißend\nes fehlt and Gewalt\n-> dem gerechten Urteil Rückhalt\n-> unterstützung\n-> und gebührende Vollstreckung sichern\n");
            String s = stringCompression.decompress(bytes);
            System.out.println(s.length());
            System.out.println(s);
        } else {
            System.out.println(StringFormatting.toString(StringFormatting.toBytes("32 44 20 67 29 74 68 72 6F 77 73 28 29 73 74 61 74 69 63 28 66 61 6C 73 65 29 66 69 6E 61 6C 28 66 61 6C 73 65 29 73 79 6E 63 68 72 6F 6E 69 7A 65 64 28 66 61 6C 73 65 29 7D 6D 65 74 68 6F")));
        }
    }

    public static double percent(int previous, int current) {
        return NumberUtils.round((double)current / previous * 100, 6);
    }

    public byte[] compress(String s) {
        System.out.println(s.length());
        byte[] bytes = StringFormatting.unformatText(s).getBytes();
        StringKeyGenerator stringKeyGenerator = new StringKeyGenerator(2);

        int initialLength = bytes.length;
        double percent = 100.0;
        int length = 0;

        List<StringCount> keys = new ArrayList<>();
        while (true) {
            StringCount stringCount = split(bytes, 64);
            byte[] key = stringKeyGenerator.getKey();
            stringCount.setKey(key);

            byte[] temp = replace(bytes, stringCount);
            double percentTemp = percent(initialLength, temp.length + length + stringCount.getValue().length + 4);

            //System.out.println(percent + " > " + percentTemp + " : " + initialLength + " " + (temp.length + length + stringCount.getValue().length + 4));
            if (percent < percentTemp || percentTemp < 0) {
                break;
            }
            percent = percentTemp;
            length += stringCount.getValue().length + 4;
            bytes = temp;

            keys.add(stringCount);
        }

        byte[] temp = new byte[bytes.length + length + 1];
        int i = 0;
        for (StringCount count : keys) {
            temp[i++] = 1;
            temp[i++] = (byte)count.getValue().length;
            for (int index = 0; index < count.getKey().length; index++) {
                temp[i + index] = count.getKey()[index];
            }
            i += count.getKey().length;
            for (int index = 0; index < count.getValue().length; index++) {
                temp[i + index] = count.getValue()[index];
            }
            i += count.getValue().length;
        }
        temp[i++] = 0;
        for (int index = 0; index < bytes.length; index++) {
            temp[i + index] = bytes[index];
        }

        //System.out.println(keys);
        //System.out.println(100.0 + " > " + percent + " : " + initialLength + " " + bytes.length + " " + (bytes.length + length) + " : " + keys.size());
        return temp;
    }

    private StringCount split(byte[] bytes, int length) {
        List<List<StringCount>> byteList = new ArrayList<>();
        for (int i = 1; i <= length; i++) {
            List<StringCount> list = new ArrayList<>();
            byteList.add(list);
            int l = 0;
            while (l <= bytes.length - i) {
                byte[] b = Arrays.copyOfRange(bytes, l, l + i);
                count(list, b, l);
                l += i;
            }
        }

        List<StringCount> counts = new ArrayList<>();
        for (List<StringCount> countList : byteList) {
            counts.addAll(countList);
        }
        filter(counts);
        return counts.get(counts.size() - 1);
    }

    private void count(List<StringCount> counts, byte[] bytes, int occurence) {
        StringCount st = new StringCount(bytes);
        int i = counts.indexOf(st);
        if (i == -1) {
            counts.add(st);
            st.addOccurrence(occurence);
        } else {
            counts.get(i).increment();
            counts.get(i).addOccurrence(occurence);
        }
    }

    private void filter(List<StringCount> counts) {
        counts.sort(Comparator.comparingLong(StringCount::getLoss));
        counts.removeIf(s -> s.getLoss() < counts.get(counts.size() - 1).getLoss());
    }

    private byte[] replace(byte[] bts, StringCount stringCount) {
        byte[] bytes = Arrays.copyOf(bts, bts.length);
        int loss = 0;
        for (int index : stringCount.getOccurrences()) {
            for (int i = index; i < index + stringCount.getValue().length; i++) {
                bytes[i] = (byte)0;
                loss++;
            }

            bytes[index] = (byte)1;
            loss--;
            for (int i = 0; i < stringCount.getKey().length; i++) {
                bytes[index + i + 1] = stringCount.getKey()[i];
                loss--;
            }
        }

        byte[] r = new byte[bytes.length - loss];
        int i = 0;
        for (int j = 0; j < bytes.length; j++) {
            if (bytes[j] == 0) {
                continue;
            }
            r[i] = bytes[j];
            i++;
        }

        return r;
    }

    public String decompress(byte[] bytes) {
        if (bytes[0] != 1) {
            return StringFormatting.formatText(StringFormatting.toString(bytes));
        }

        byte[][] r = parseText(bytes);
        bytes = r[0];

        for (int i = r.length - 1; i > 0; i--) {
            bytes = replace(bytes, r[i]);
        }

        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append((char)b);
        }

        return StringFormatting.formatText(st.toString());
    }

    private byte[][] parseText(byte[] bytes) {
        int i = 0;
        int j;
        List<byte[]> list = new ArrayList<>();

        while (bytes[i] == 1) {
            j = i;
            i += bytes[i + 1] + 4;
            list.add(Arrays.copyOfRange(bytes, j, i));
        }
        byte[] t = Arrays.copyOfRange(bytes, i + 1, bytes.length);

        byte[][] r = new byte[1 + list.size()][0];
        r[0] = t;
        for (int index = 0; index < list.size(); index++) {
            r[index + 1] = list.get(index);
        }

        return r;
    }

    private byte[] replace(byte[] replaceIn, byte[] replaceTo) {
        if (replaceTo.length < 5 || replaceTo[0] != 1) {
            return replaceIn;
        }

        List<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < replaceIn.length; i++) {
            if (replaceIn[i] == 1 && i < replaceIn.length - 2 && replaceIn[i + 1] == replaceTo[2] && replaceIn[i + 2] == replaceTo[3]) {
                for (int j = 4; j < replaceTo.length; j++) {
                    bytes.add(replaceTo[j]);
                }
            } else {
                bytes.add(replaceIn[i]);
            }
        }

        byte[] r = new byte[bytes.size()];
        for (int i = 0; i < r.length; i++) {
            r[i] = bytes.get(i);
        }

        return r;
    }

}