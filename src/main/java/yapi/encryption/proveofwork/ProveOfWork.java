// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.proveofwork;

import yapi.math.NumberRandom;
import yapi.math.base.BaseConversion;
import yapi.runtime.ThreadUtils;
import yapi.string.HashType;
import yapi.string.StringCrypting;
import yapi.string.StringFormatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProveOfWork {

    public static void main(String[] args) {
        if (false) {
            System.out.println(ProveOfWork.validHash("\\Z$JbjjzJEw*"));
            System.out.println(ProveOfWork.validHash("nDRDN6nL[%F!"));
            return;
        }

        ProveOfWork proveOfWork = new ProveOfWork();

        // 582
        // \Z$Jbjjz
        // JEw*
        // 1h 48m 52s

        // 524
        // nDRDN6nL
        // [%F!
        // 1h 5m 2s

        // 100
        // fPrDFpvD
        // <-:
        // 0h 4m 21s

        // 619
        // f.^H2R"r
        // |uP
        // 4h 29m 27s

        for (int i = 1; i < 65536; i++) {
            String s = proveOfWork.work(i);
            String t = (proveOfWork.getTime() / 1000.0) + "";
            t = t + "0".repeat(4 - t.substring(t.indexOf('.')).length());
            t = " ".repeat(15 - t.length()) + t;
            String n = i + "";
            n = " ".repeat(15 - n.length()) + n;
            String w = proveOfWork.getTries() + "";
            w = " ".repeat(20 - w.length()) + w;
            System.out.println(n + ": " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + " " + t + "s [" + w + "] -> " + s);
            /*System.out.println("   " + s);
            String t = StringFormatting.toHex(StringCrypting.hash(s, HashType.SHA512));
            System.out.println("   " + t);*/
        }
    }

    private String salt = new NumberRandom().getString(8);
    private long time = 0;
    //private String salt = "";

    private String solution = "";
    private boolean done = false;
    private long tries = 0;

    public long getTries() {
        return tries;
    }

    public long getTime() {
        return time;
    }

    private class WorkSystem {

        char[] chars = new char[128];
        private long tries = 0;

        {
            chars[0] = 1;
        }

        public synchronized String next() {
            tries++;
            boolean b = true;
            int i = 0;
            while (b) {
                chars[i]++;
                b = false;
                if (chars[i] > 94) {
                    b = true;
                    chars[i] = 1;
                    i++;
                }
            }

            StringBuilder st = new StringBuilder();
            i = 0;
            while (i < chars.length) {
                if (chars[i] == 0) {
                    break;
                }
                char c = (char)(chars[i] + 31);
                if (c >= 127) {
                    c++;
                }
                st.append(c);
                i++;
            }
            return st.toString();
        }

    }

    public synchronized String work(int work) {
        if (work < 0) {
            work = 0;
        }
        if (work > 65536) {
            work = 65536;
        }

        int fWork = work;
        done = false;
        solution = "";
        WorkSystem workSystem = new WorkSystem();

        Runnable runnable = () -> {
            while (!done) {
                String current = workSystem.next();
                if (testHash(current, fWork)) {
                    done = true;
                    solution = current;
                }
            }
        };

        time = System.currentTimeMillis();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() / 2; i++) {
            Thread t = new Thread(runnable);
            t.setName("ProveOfWork Thread - " + i);
            t.start();
        }

        while (solution.isEmpty()) {
            ThreadUtils.sleep(100);
        }
        time = System.currentTimeMillis() - time;
        tries = workSystem.tries;
        return salt + solution;
    }

    private boolean testHash(String input, int work) {
        String s = StringFormatting.toHex(StringCrypting.hash(salt + input, HashType.SHA512));
        if (input.length() < 3) {
            return false;
        }
        if (s.length() - s.replace("0", "").length() < 5) {
            return false;
        }
        String w = BaseConversion.toBaseN(work, 8).replace("0", "F") + "0";
        if (!s.startsWith(w)) {
            return false;
        }
        boolean b = false;
        String t = "0123456789ABCDEF";
        for (int i = 0; i < t.length(); i++) {
            if (s.length() - s.replace(t.charAt(i) + "", "").length() > Math.sqrt(work) / 2) {
                b = true;
                break;
            }
        }
        if (!b) {
            return false;
        }
        return contains(s, input.charAt(work % input.length()));
    }

    private static boolean contains(String s, char c) {
        String t = StringFormatting.toHex((byte)c);
        return (s.length() - s.replace(t, "").length()) == 2;
    }

    public static boolean validHash(String s) {
        if (s.length() < 10) {
            return false;
        }
        String hash = StringFormatting.toHex(StringCrypting.hash(s));
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                break;
            }
            st.append(hash.charAt(i));
        }

        int work = BaseConversion.fromBaseNtoInt(st.toString().replace("F", "0"), 8) % s.substring(8).length();
        return contains(hash, s.substring(8).charAt(work));
    }

}