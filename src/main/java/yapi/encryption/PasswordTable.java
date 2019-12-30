package yapi.encryption;

import yapi.encryption.passwordtable.*;
import yapi.utils.Timer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordTable {

    private List<PWObject> pwObjects = new ArrayList<>();
    private BigInteger possibilities = new BigInteger("1");
    private BigInteger done = new BigInteger("0");
    private BigDecimal percent;
    private BigDecimal donePercent = new BigDecimal("0");
    private int doneNumber = 0;
    private boolean show = false;
    private Timer timer = new Timer();

    private String password = null;

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
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (escaped && (chars[i] == 'd' || chars[i] == '.' || chars[i] == 'l' || chars[i] == 'u' || chars[i] == 'w' || chars[i] == '{')) {
                char c = chars[i];
                if (c == 'd') {
                    possibilities = possibilities.multiply(new BigInteger("10"));
                    pwObjects.add(new PWNumber());
                } else if (c == '.') {
                    possibilities = possibilities.multiply(new BigInteger("107"));
                    pwObjects.add(new PWAnyCharacter());
                } else if (c == 'l') {
                    possibilities = possibilities.multiply(new BigInteger("26"));
                    pwObjects.add(new PWCharacterLowerCase());
                } else if (c == 'u') {
                    possibilities = possibilities.multiply(new BigInteger("26"));
                    pwObjects.add(new PWCharacterUpperCase());
                } else if (c == 'w') {
                    possibilities = possibilities.multiply(new BigInteger("52"));
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

    public synchronized String getPassword() {
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
        if (show) {
            boolean newNumber = false;
            while (new BigDecimal(done).compareTo(donePercent) > 0) {
                newNumber = true;
                doneNumber++;
                donePercent = donePercent.add(percent, new MathContext(2));
            }
            if (newNumber) {
                System.out.println("[" + "=".repeat(doneNumber - 1) + ">" + " ".repeat(100 - doneNumber) + "] " + leading(donePercent.divide(new BigDecimal(possibilities), new MathContext(4)).multiply(new BigDecimal("100")).toPlainString(), 10) + "%");
            }
        }
        return st.toString();
    }

    public String crack(String toCrack, int security, int threads) {
        return crack(toBytes(toCrack), security, threads);
    }

    public String crack(byte[] toCrack, int security, int threads) {
        timer.start();
        if (threads < 1) {
            return crack(toCrack, security);
        }
        password = null;
        for (int i = 0; i < threads; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (password == null) {
                        String pw = threadCrack(toCrack, security);
                        if (pw != null) {
                            password = pw;
                            timer.stop();
                        }
                    }
                }
            };
            Thread t = new Thread(runnable);
            t.setName("Cracker: " + i);
            t.start();
        }
        while (password == null) {
            if (done.compareTo(possibilities) > 0 || done.compareTo(possibilities) == 0) {
                password = "";
                timer.stop();
                failed();
                return "";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        done(password);
        return password;
    }

    private String threadCrack(byte[] toCrack, int security) {
        String password = getPassword();
        if (password == null) {
            return "";
        }
        byte[] crack = Arrays.copyOf(toCrack, toCrack.length);
        byte[] bytes = EncryptionSymmetric.decrypt(crack, EncryptionSymmetric.createKey(password, security));
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
        String password = getPassword();
        if (password == null) {
            return "";
        }
        int i = 0;
        while (password != null) {
            i++;
            byte[] crack = Arrays.copyOf(toCrack, toCrack.length);
            byte[] bytes = EncryptionSymmetric.decrypt(crack, EncryptionSymmetric.createKey(password, security));
            if (bytes.length != 0) {
                timer.stop();
                done(password);
                return password;
            }
            password = getPassword();
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
        System.out.println();
        System.out.println("[" + "=".repeat(100) + "] " + leading("100.0", 10) + "%");
        System.out.println();
        System.out.println("Password cracked in " + done.toString() + " attempts.");
        System.out.println("Password is '" + password + "'");
        System.out.println("Time passed since start: " + timer.getTimeFormatted("hF>h MF>M sF>s mF>m xF>x nF>n"));
        System.out.println();
        timer.reset();
    }

    private void failed() {
        if (!show) {
            timer.reset();
            return;
        }
        System.out.println();
        System.out.println("[" + "=".repeat(100) + "] " + leading("100.0", 10) + "%");
        System.out.println();
        System.out.println("Password cracked in " + done.toString() + " attempts.");
        System.out.println("Password is " + "'<UNKNOWN>'");
        System.out.println("Time passed since start: " + timer.getTimeFormatted("hF>h MF>M sF>s mF>m xF>x nF>n"));
        System.out.println();
        timer.reset();
    }

}
