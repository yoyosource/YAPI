// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption;

import yapi.encryption.passwordtable.*;
import yapi.quick.Timer;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordTable {

    public static void main(String[] args) {
        PasswordTable passwordTable = new PasswordTable("\\d \\d\\d");
        passwordTable.showProgress();
        passwordTable.crack("3C 5A C8 33 F9 44 4C EC C1 70 C1 04 E6 DC 3B 96 6A 38 C5 09 A2 4B 04 9F B4 E0 CD 0A EC 8E 08 29 E2 95 27 BA 2F 68 82 25 AB E9 38 31 AC 2E", 0);
    }

    private List<PWObject> pwObjects = new ArrayList<>();
    private BigInteger possibilities = BigInteger.ONE;
    private BigInteger done = BigInteger.ZERO;
    private BigDecimal percent;
    private BigDecimal donePercent = BigDecimal.ZERO;
    private int doneNumber = 0;
    private boolean show = false;
    private Timer timer = new Timer();

    private long attempts = 0;
    private long time = System.currentTimeMillis();
    private String eta = "";

    private String pwd = null;

    /**
     * \\. is any character
     * \\w is a-zA-Z
     * \\l is a-z
     * \\u is A-Z
     * \\d is 0-9
     * \\{ is defining a character group closed by \\} which cycles though the characters defined in between
     *
     * @param s
     */
    public PasswordTable(String s) {
        char[] chars = s.toCharArray();
        boolean escaped = false;
        int i = 0;
        while (i < chars.length) {
            if (chars[i] == '\\') {
                escaped = true;
                i++;
                continue;
            }

            if (escaped && (chars[i] == 'd' || chars[i] == '.' || chars[i] == 'l' || chars[i] == 'u' || chars[i] == 'w' || chars[i] == '{')) {
                char c = chars[i];
                if (c == 'd') {
                    possibilities = possibilities.multiply(BigInteger.TEN);
                    pwObjects.add(new PWNumber());
                } else if (c == '.') {
                    possibilities = possibilities.multiply(BigInteger.valueOf(107));
                    pwObjects.add(new PWAnyCharacter());
                } else if (c == 'l') {
                    possibilities = possibilities.multiply(BigInteger.valueOf(26));
                    pwObjects.add(new PWCharacterLowerCase());
                } else if (c == 'u') {
                    possibilities = possibilities.multiply(BigInteger.valueOf(26));
                    pwObjects.add(new PWCharacterUpperCase());
                } else if (c == 'w') {
                    possibilities = possibilities.multiply(BigInteger.valueOf(52));
                    pwObjects.add(new PWCharacter());
                } else if (c == '{') {
                    String st = getBracket(chars, i);
                    possibilities = possibilities.multiply(new BigInteger(st.length() + ""));
                    pwObjects.add(new PWCharacterGroup(st));
                    i += st.length() + 2;
                }
            } else {
                pwObjects.add(new PWStatic(chars[i]));
            }
            escaped = false;
            i++;
        }

        percent = new BigDecimal(possibilities).divide(BigDecimal.TEN.multiply(BigDecimal.TEN), new MathContext(2));
    }

    private static byte[] toBytes(String s) {
        if (s.matches("[A-F0-9]{2}( [A-F0-9]{2})*")) {
            String[] strings = s.split(" ");
            byte[] bytes = new byte[strings.length];
            for (int i = 0; i < strings.length; i++) {
                bytes[i] = (byte)(Integer.parseInt(strings[i], 16));
            }
            return bytes;
        } else {
            return s.getBytes();
        }
    }

    private String getBracket(char[] chars, int index) {
        int bracket = 1;
        index++;
        boolean escaped = false;
        StringBuilder st = new StringBuilder();
        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (escaped && chars[i] == '{') {
                bracket++;
            } else if (escaped && chars[i] == '}') {
                bracket--;
            } else {
                if (escaped) {
                    st.append('\\');
                }
                st.append(chars[i]);
            }
            escaped = false;
            if (bracket == 0) {
                break;
            }
        }
        return st.toString();
    }

    public synchronized String getPwd() {
        StringBuilder st = new StringBuilder();
        boolean b = true;
        int i = 0;
        for (PWObject pwObject : pwObjects) {
            if (i == pwObjects.size() && b) {
                return null;
            }
            i++;
            st.append(pwObject.getChar());
            if (b) {
                b = pwObject.update();
            }
        }
        done = done.add(BigInteger.ONE);

        attempts++;
        if (System.currentTimeMillis() - time > 1000) {
            time = System.currentTimeMillis();
            eta();
            attempts = 0;
        }

        if (show) {
            boolean newNumber = false;
            while (new BigDecimal(done).compareTo(donePercent) > 0) {
                newNumber = true;
                doneNumber++;
                donePercent = donePercent.add(percent, new MathContext(2));
            }
            if (donePercent.toPlainString().equals(possibilities.toString())) {
                return null;
            }
            if (newNumber) {
                message();
            }
        }
        return st.toString();
    }

    private void message() {
        System.out.println("[" + "=".repeat(doneNumber - 1) + ">" + " ".repeat(100 - doneNumber) + "] " + leading(donePercent.divide(new BigDecimal(possibilities), new MathContext(4)).multiply(new BigDecimal("100")).toPlainString(), 10) + "%" + "   " + leading(eta, 10) + "ms");
    }

    private void eta() {
        eta = BigInteger.valueOf(timer.currentTime()).multiply(possibilities.divide(done)).divide(BigInteger.valueOf(1000000)).toString();
    }

    public String crack(String toCrack, int security, int threads) {
        return crack(toBytes(toCrack), security, threads);
    }

    public String crack(byte[] toCrack, int security, int threads) {
        timer.start();
        if (threads < 1) {
            return crack(toCrack, security);
        }
        pwd = null;
        for (int i = 0; i < threads; i++) {
            Runnable runnable = () -> {
                while (pwd == null) {
                    String pw = threadCrack(toCrack, security);
                    if (pw != null) {
                        pwd = pw;
                        timer.stop();
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.setName("Cracker: " + i);
            t.start();
        }
        while (pwd == null) {
            if (done.compareTo(possibilities) > 0 || done.compareTo(possibilities) == 0) {
                pwd = "";
                timer.stop();
                failed();
                return "";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        done(pwd);
        return pwd;
    }

    private String threadCrack(byte[] toCrack, int security) {
        String password = getPwd();
        if (password == null) {
            return "";
        }
        byte[] crack = Arrays.copyOf(toCrack, toCrack.length);
        byte[] bytes = EncryptionSymmetric.decrypt(crack, EncryptionSymmetric.createKey(password, security, true));
        if (bytes.length != 0) {
            return password;
        }
        return null;
    }

    public String crack(String toCrack, int security) {
        return crack(toBytes(toCrack), security);
    }

    public String crack(byte[] toCrack, int security) {
        timer.start();
        String password = getPwd();
        if (password == null) {
            return "";
        }
        while (password != null) {
            byte[] crack = Arrays.copyOf(toCrack, toCrack.length);
            byte[] bytes = EncryptionSymmetric.decrypt(crack, EncryptionSymmetric.createKey(password, security, true));
            if (bytes.length != 0) {
                timer.stop();
                done(password);
                return password;
            }
            password = getPwd();
        }
        timer.stop();
        failed();
        return "";
    }

    public String getPossibilities() {
        return possibilities.toString();
    }

    public void showProgress() {
        show = true;
    }

    public void hideProgress() {
        show = false;
    }

    private String leading(String s, int length) {
        if (s.endsWith("00")) {
            s = s.substring(0, s.length() - 2);
        }
        if (s.endsWith(".")) {
            s = s + "0";
        }
        if (s.endsWith(".0")) {
            s = s + "0";
        }
        StringBuilder leading = new StringBuilder();
        while ((leading.length() + s.length()) < length) {
            leading.append(' ');
        }
        return leading + s;
    }

    private void done(String password) {
        if (!show) {
            timer.reset();
            return;
        }
        PrintStream printStream = System.out;
        printStream.println();
        printStream.println("[" + "=".repeat(100) + "] " + leading("100.0", 10) + "%");
        printStream.println();
        printStream.println("Password cracked in " + done.toString() + " attempts.");
        printStream.println("Password is '" + password + "'");
        printStream.println("Time passed since start: " + timer.getTimeFormatted("hF>h MF>M sF>s mF>m xF>x nF>n"));
        printStream.println();
        printStream.flush();
        timer.reset();
    }

    private void failed() {
        if (!show) {
            timer.reset();
            return;
        }
        PrintStream printStream = System.out;
        printStream.println();
        printStream.println("[" + "=".repeat(100) + "] " + leading("100.0", 10) + "%");
        printStream.println();
        printStream.println("Password cracked in " + done.toString() + " attempts.");
        printStream.println("Password is " + "'<UNKNOWN>'");
        printStream.println("Time passed since start: " + timer.getTimeFormatted("hF>h MF>M sF>s mF>m xF>x nF>n"));
        printStream.println();
        printStream.flush();
        timer.reset();
    }

}