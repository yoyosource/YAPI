// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.string;

import yapi.internal.exceptions.CompressionUsageException;
import yapi.file.FileUtils;
import yapi.math.NumberRandom;
import yapi.math.NumberUtils;
import yapi.string.StringCrypting;
import yapi.string.StringFormatting;
import yapi.string.StringSplitting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StringCompression {

    private boolean used = false;

    private StringCompression() {

    }

    public static StringCompression getInstance() {
        return new StringCompression();
    }

    public static void main(String[] args) {
        String t = "❤❤❤ " + (("Danke ❤❤❤ ".repeat(10)).repeat(10)).repeat(10).trim();
        String s = StringSplitting.merge(FileUtils.fileContentFromResourceAsString("main/yapi.info"), "\n");
        String g = new NumberRandom().getString(1024*140);
        String c = "Danke ".repeat(1024*80);
        String h = StringFormatting.toHex(StringCrypting.hash("Hallo InProgressing")).repeat(64) + StringFormatting.toHex(StringCrypting.hash("Hallo inProgressing")).repeat(64);
        String l = "DankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDankeDanke";
        String m = "Eigentum (Schutz leben, Freiheiten und Vermögen)\n" +
                "größter Grund ^ zu zusammeschließung\n" +
                "es fehlt feststehend und geordneten und bekannten Gesetz\n" +
                "-> allgemeine Zustimmung\n" +
                "-> Norm für Recht und Unrecht\n" +
                "-> allgemeine Maßstab zur Entscheidung ihrer Streitigkeiten (angenommen und anerkannt)\n" +
                "Gesetz Natur ist klar\n" +
                "-> Interesse beeinflusst\n" +
                "-> nicht nachdenken => zu wenig kennen -> keine Rechtsanerkennung, keine anwendungsverpfilchtung gesehen\n" +
                "es feht anerkannte und unparteiische Richter\n" +
                "-> Recht alle zwistigkeiten nach dem festehenden Gesetz zu entscheiden\n" +
                "=> Jeder Richter und Vollzieher dieses Gesetztes; Menschen sich selbst gegenüber parteiisch\n" +
                "-> eigenen Sache Leidenschaft und Rache zu weit fortreißend\n" +
                "es fehlt and Gewalt\n" +
                "-> dem gerechten Urteil Rückhalt\n" +
                "-> unterstützung\n" +
                "-> und gebührende Vollstreckung sichern\n";

        String i = m;

        System.out.println("Input");
        System.out.println(i);
        System.out.println(i.length());
        System.out.println("100%");
        System.out.println();

        StringCompression compress = StringCompression.getInstance();
        byte[] bytes = compress.compress(i);
        System.out.println("Compress");
        System.out.println(StringFormatting.toHex(bytes));
        System.out.println(bytes.length);
        System.out.println("~" + percent(i.length(), bytes.length) + "%");
        System.out.println();

        StringCompression decompress = StringCompression.getInstance();
        String o = decompress.decompress(bytes);
        System.out.println("Decompress");
        System.out.println(o);
        System.out.println(o.length());
        System.out.println("~" + percent(i.length(), o.length()) + "%");
    }

    public static double percent(int previous, int current) {
        return NumberUtils.round((double)current / previous * 100, 6);
    }

    public byte[] compress(String s) {
        if (used) {
            throw new CompressionUsageException("Don't reuse old instances. Get a new instance with getInstance().");
        }
        used = true;
        StringKeyGenerator keyGenerator = new StringKeyGenerator(2);
        List<StringCount> lookupTableCount = new ArrayList<>();

        return compress(StringFormatting.unformatText(s), keyGenerator, lookupTableCount);
    }

    private byte[] compress(String s, StringKeyGenerator keyGenerator, List<StringCount> lookupTableCount) {
        String first = s + "";
        String last = "";
        int times = 0;

        while (last.length() == 0 || last.length() > s.length()) {
            System.out.println(times++ + " -> ~" + percent(first.length(), s.length()) + "%");
            last = s + "";
            List<StringCount> counts = new ArrayList<>();
            split(s, counts);

            counts.forEach(c -> c.setKeyLength(keyGenerator.length()));

            sort(counts);
            filter(counts, keyGenerator);
            if (counts.isEmpty()) {
                break;
            }

            StringCount t = counts.get(counts.size() - 1);
            System.out.println(t);
            lookupTableCount.add(t);
            byte[] bytes = s.getBytes();
            byte[] toReplace = t.getString().getBytes();
            List<Integer> integers = occurrences(bytes, toReplace);
            byte[] key = keyGenerator.getKey();
            replace(bytes, integers, toReplace, key);
            t.setKey(key);

            List<Byte> compressed = compress(bytes, keyGenerator.length());
            StringBuilder st = new StringBuilder();
            for (int i = 0; i < compressed.size(); i++) {
                st.append((char) (byte) compressed.get(i));
            }
            s = st.toString();
        }

        List<Byte> lookupTable = new ArrayList<>();
        if (!lookupTableCount.isEmpty()) {
            lookupTable.add((byte)keyGenerator.length());
            for (int i = lookupTableCount.size() - 1; i >= 0; i--) {
                StringCount count = lookupTableCount.get(i);
                lookupTable.add((byte) 1);
                byte[] key = count.getKey();
                for (int index = 0; index < key.length; index++) {
                    lookupTable.add(count.getKey()[index]);
                }
                byte[] bytes = count.getString().getBytes();
                lookupTable.add((byte) bytes.length);
                for (int index = 0; index < bytes.length; index++) {
                    lookupTable.add(bytes[index]);
                }
            }
            lookupTable.add((byte) 0);
        }
        for (byte b : last.getBytes()) {
            lookupTable.add(b);
        }

        return toBytes(lookupTable);
    }

    private byte[] toBytes(List<Byte> bts) {
        byte[] bytes = new byte[bts.size()];
        for (int i = 0; i < bts.size(); i++) {
            bytes[i] = bts.get(i);
        }
        return bytes;
    }

    private List<Byte> compress(byte[] bytes, int keySize) {
        byte[] killBytes = new byte[bytes.length];
        int index = 0;
        while (index < bytes.length) {
            if (bytes[index] == 0) {
                killBytes[index] = 1;
            }
            if (bytes[index] == 1) {
                index += keySize;
            }
            index++;
        }

        List<Byte> compressed = new ArrayList<>();
        for (int i = 0; i < killBytes.length; i++) {
            if (killBytes[i] != 1) {
                compressed.add(bytes[i]);
            }
        }
        return compressed;
    }

    private void split(String s, List<StringCount> counts) {
        List<List<StringCount>> list = new ArrayList<>();
        int threads = 0;
        Count count = new Count();
        for (int i = 1; i < 64; i++) {
            threads++;
            List<StringCount> stringCounts = new ArrayList<>();
            list.add(stringCounts);
            int index = i;
            Runnable runnable = () -> {
                String t = s + "";
                while (t.length() > index) {
                    String str = t.substring(0, index);
                    t = t.substring(index);
                    count(new StringCount(str), stringCounts);
                }
                count.increment();
            };
            Thread t = new Thread(runnable);
            t.start();
        }
        while (threads > count.getCount()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (List<StringCount> c : list) {
            counts.addAll(c);
        }
    }

    private void sort(List<StringCount> counts) {
        counts.sort(Comparator.comparingLong(StringCount::getLoss));
    }

    private void filter(List<StringCount> stringCounts, StringKeyGenerator stringKeyGenerator) {
        for (int i = stringCounts.size() - 1; i >= 0; i--) {
            if (stringCounts.get(i).getLoss() <= 0) {
                stringCounts.remove(i);
            }
        }
        if (stringCounts.size() <= stringKeyGenerator.keys()) {
            return;
        }
        List<StringCount> counts = new ArrayList<>();
        while (counts.size() < stringKeyGenerator.keys()) {
            counts.add(stringCounts.get(stringCounts.size() - 1));
            stringCounts.remove(stringCounts.size() - 1);
            if (stringCounts.isEmpty()) {
                break;
            }
        }
        stringCounts.clear();
        stringCounts.addAll(counts);
        sort(stringCounts);
    }

    private void count(StringCount s, List<StringCount> counts) {
        int i = counts.indexOf(s);
        if (i == -1) {
            counts.add(s);
        } else {
            counts.get(i).increment();
        }
    }

    private List<Integer> occurrences(byte[] bytes, byte[] toSearch) {
        List<Integer> occurrences = new ArrayList<>();
        if (toSearch.length == 0) {
            return occurrences;
        }
        int i = 0;
        while (i < bytes.length - toSearch.length) {
            if (bytes[i] == toSearch[0]) {
                boolean match = true;
                for (int index = 0; index < toSearch.length; index++) {
                    if (bytes[i + index] != toSearch[index]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                    i += toSearch.length;
                }
            }
            i++;
        }
        return occurrences;
    }

    private void replace(byte[] bytes, List<Integer> occurrences, byte[] replace, byte[] replaceTo) {
        if (replaceTo.length + 1 > replace.length) {
            return;
        }
        for (int index : occurrences) {
            for (int i = 0; i < replace.length; i++) {
                bytes[index + i] = 0;
            }
            bytes[index] = (byte)1;
            for (int i = 0; i < replaceTo.length; i++) {
                bytes[index + i + 1] = replaceTo[i];
            }
        }
    }

    public String decompress(byte[] bytes) {
        if (used) {
            throw new CompressionUsageException("Don't reuse old instances. Get a new instance with getInstance().");
        }
        used = true;
        if (bytes[1] != (byte)1) {
            return StringFormatting.formatText(StringFormatting.toString(bytes));
        }

        int start = start(bytes);
        List<Byte> byteList = getText(bytes, start);
        List<byte[]> lookupTable = splitLookUpTable(getLookUpTable(bytes, start), bytes[0]);

        System.out.println(StringFormatting.toHex(toBytes(byteList), true));
        for (byte[] lookup : lookupTable) {
            byteList = replace(byteList, lookup, bytes[0]);
            System.out.println(StringFormatting.toHex(toBytes(byteList), true));
            break;
        }
        System.out.println(byteList.size());

        return "";
    }

    private int start(byte[] bytes) {
        int i = 1;
        while (i < bytes.length) {
            if (bytes[i] == 1) {
                i += bytes[0];
                i += bytes[i];
                continue;
            }
            if (bytes[i] == 0) {
                return i + 1;
            }
            i++;
        }
        return 0;
    }

    private List<Byte> getText(byte[] bts, int start) {
        List<Byte> bytes = new ArrayList<>();
        for (int i = start; i < bts.length; i++) {
            bytes.add(bts[i]);
        }
        return bytes;
    }

    private byte[] getLookUpTable(byte[] bts, int start) {
        return Arrays.copyOfRange(bts, 1, start - 1);
    }

    private List<byte[]> splitLookUpTable(byte[] bts, int keyLength) {
        List<byte[]> list = new ArrayList<>();
        while (bts.length != 0) {
            int start = 4 + bts[1 + keyLength];
            byte[] bytes = Arrays.copyOfRange(bts, 0, start);
            list.add(bytes);
            bts = Arrays.copyOfRange(bts, start, bts.length);
        }
        return list;
    }

    private List<Byte> replace(List<Byte> bts, byte[] toReplace, int keyLength) {
        List<Byte> bytes = new ArrayList<>();

        int i = 0;
        while (i < bts.size()) {
            if (bts.get(i) == (byte)1) {
                boolean occurring = true;
                for (int j = 0; j <= keyLength; j++) {
                    if (bts.get(i + j) != toReplace[j]) {
                        occurring = false;
                        break;
                    }
                }
                if (occurring) {
                    i += keyLength;
                    System.out.println(StringFormatting.toHex(toReplace, true) + " -> " + i);
                    for (int j = keyLength; j < toReplace.length; j++) {
                        bytes.add(toReplace[j]);
                    }
                } else {
                    bytes.add(bts.get(i));
                }
            } else {
                bytes.add(bts.get(i));
            }
            i++;
        }

        return bytes;
    }

    private class Count {
        int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

}