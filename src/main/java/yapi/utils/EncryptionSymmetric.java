package yapi.utils;

import yapi.exceptions.EncryptionException;
import yapi.math.NumberRandom;
import yapi.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptionSymmetric {

    private EncryptionSymmetric() {
        throw new IllegalStateException("Utility class");
    }

    public static final int XOR       = 0;
    public static final int NOT       = 1;
    public static final int INVERSE   = 1;

    public static final int PLUS      = 2;
    public static final int ADD       = 2;
    public static final int MINUS     = 3;
    public static final int SUBTRACT  = 3;

    public static final int XOR_PLUS  = 6;
    public static final int XOR_MINUS = 7;

    private static List<Integer> operations = new ArrayList<>();
    static {
        operations.add(XOR_PLUS);
    }

    public static void resetEncryption() {
        operations.clear();
        operations.add(XOR_PLUS);
    }

    public static void setEncryption() {
        resetEncryption();
    }

    public static void setEncryption(int... ints) {
        operations.clear();
        if (ints.length == 0) {
            operations.add(XOR_PLUS);
            return;
        }
        for (int i : ints) {
            if (i >= 0 && i <= 7) {
                operations.add(i);
            }
        }
    }

    public static void setEncryption(String operation) {
        operations.clear();
        if (operation.isBlank()) {
            operations.add(XOR_PLUS);
            return;
        }
        String[] strings = operation.split(" ");
        for (String s : strings) {
            if (s.equalsIgnoreCase("XOR") || s.equals(XOR + "")) {
                operations.add(XOR);
            } else if (s.equalsIgnoreCase("NOT") || s.equals(NOT + "")) {
                operations.add(NOT);
            } else if (s.equalsIgnoreCase("INVERSE")) {
                operations.add(INVERSE);
            } else if (s.equalsIgnoreCase("PLUS") || s.equals(PLUS + "")) {
                operations.add(PLUS);
            } else if (s.equalsIgnoreCase("ADD")) {
                operations.add(ADD);
            } else if (s.equalsIgnoreCase("MINUS") || s.equals(MINUS + "")) {
                operations.add(MINUS);
            } else if (s.equalsIgnoreCase("SUBTRACT")) {
                operations.add(SUBTRACT);
            } else if (s.equalsIgnoreCase("XOR_PLUS") || s.equals(XOR_PLUS + "")) {
                operations.add(XOR_PLUS);
            } else if (s.equalsIgnoreCase("XOR_MINUS") || s.equals(XOR_MINUS + "")) {
                operations.add(XOR_MINUS);
            }
        }
    }

    public static void main(String[] args) {
        //setEncryption(ADD, INVERSE, MINUS, XOR_PLUS, INVERSE, ADD, XOR_PLUS, XOR, MINUS, NOT, ADD, NOT);

        if (false) {
            int security = 4;
            char[] s1 = createKey("pw12345", security).toCharArray();
            char[] s2 = createKey("pw21345", security).toCharArray();
            int longest = 0;
            while (s1.length > Math.pow(10, longest)) {
                longest++;
            }

            int duplicates = 0;
            for (int i = 0; i < s1.length; i++) {
                if (s1[i] == s2[i]) {
                    duplicates++;
                    //System.out.println(" ".repeat(longest - (i + "").length()) + i + " -> " + s1[i]);
                }
            }
            System.out.println("Total duplicates  : " + duplicates);
            System.out.println("Total length      : " + s1.length);

            return;
        }

        if (false) {
            for (int i = 0; i < 100; i++) {
                decrypt(encrypt(toBytes(createKey()), createKey()), createKey());
            }
        }

        if (false) {
            byte[] bytes = toBytes("55 5E 26 A4 A8 BC BC 4A B4 94 16 E4 48 38 AE 58 B6 59 C3 C8 F4 22 F5 4E 60 4C C9 CA A4 63 4F 04 39 6A 63 6A 40 C9 0E EC B0 4B F1 CE AE 38 2A 15 A9 D9 B4 0D 83 F4 98 2B 6B 78 2E A9 FC 8C 60 B9 A1 66 A8 84 55 4B 08 75 44 B1 A2 A5 CE F4 EB 9B 48 CA D5 A1 9A 85 2E EE 48 A4 49 83 08 7B 35 6E 2E 78 F5 2B 90 1C C8 28 55 D8 A1 13 44 28 31 7D EE 69 EE 2C BB 2F 41 DC 51 B0 11 EA 64 94 59 1A A5 D0 24 A3 4D 21 C0 7C CD 53 70 BC B7 18 1A 8D 44 AA 61 64 FD 0C 98 58 FA 55 4E 4E");
            String s = toString(decrypt(bytes, "h&dX`N`fL>\"Dtvfzj&2vHFtL>@X(BZ`fD&DJT BFB|6Nr b(86h&P4V4^`Vp8|n$L\\xb.hnj(4 `,jZljLz\\N(zfrVzBrJ4&,B*LPXTr20\\n0TF2jjv6>6lp@0>NbZj<:x4\"JDH|^,<L4lt$hXZz(6:4fZ^NLPdLL` 8 j<t^n@>vXBp*TpLX`&Xz R\"N<0|0>Nrpl2f@2:nNnVVp zNRXd8(fVbv,HX6Tt:8LxJVDBJpV`&(VP2Z4d||Zr>n,^."));
            System.out.println(s);
        }

        if (true) {
            String key = createKey("pw12345", 1);
            System.out.println(key);
            String toCrypt = "Ich bin der coder32 und kann ziemlich schnell auf Tastaturen schreiben. Das kann ich, da ich einen Computer habe, seit ich 7 Jahre alt bin.";
            //toCrypt = new NumberRandom().getString(1024);
            Timer timer = new Timer();
            timer.start();
            byte[] bytes = encrypt(toBytes(toCrypt), key);
            timer.stop();
            System.out.println(toHex(bytes));
            System.out.println(timer.getTimeFormatted("MMF>M ssF>s mmmF>m xxxF>x nnnF>n"));
            //key = ".` rFrN(>8><&Z^0@h\\F8FVxdL.J,&@,f4<nxBPB&z, XV0N\"b*bJhj |XhZ HR|@Bh:@::\\22@@<lrNX6bvFJ|N,jJz*XVT\\4|f(2^D6zd>R\\lTXZzbPZ.V<0ZHpTV*h|(bN:>R(`xJvVDZr`0:j0nnX^Bp$zxr:\"$nBhN(P60JrxF20zx<P6^$*p &hnd8fNR&x:z8n t:\\N>ZZ,.$V,XBL^6J6zz,xXrrP&hnx@4&@8P\\n*^,6FFd|dN&pd>|J$<Br8 >\\`<h\\6Z|TzFH^@^(fRz&L6Z,V.\\R* trJ^.TfHzH,xH`LVdl(H`6Jt8 2RT*Zhp@l&Dp*6<4nhn8hVR<`T&|n<TbPjv\"p|0rrdtR:.p>fz*.Fh|Dnf.>4Z4hrvV2dPnX4 6 BZ^ $FT2>rt4*Dxh.db^8b8flb.rRx6$Z<\\2^>*d\\Vtt\\R2NJ^>6XFD<$(`<f\\XH \">d >xp,H&VdDJvT\\2*^Xl2jhj<J86T0@b(4(>DTh8DHDzbX2`Vh^zF0TFTPVHFBzhh*bX^$P&\\BL@r\\.j<Z02vFDvhzFnDZ&P^Nx@$(x\\F@(,H$LRf&ZJ4Nj&.f2*^,tlfD\\FVB&&&$nN4@VrLp|\"$0b4p| b<2,z\\(P<r*2\"(J4h6x\\*l,Hp`J.,$,hvFn8hF.z`J.@\\d\"BJH\":x2x2&2@v v:P\\&\"bV<(@N4xtT40:>Xx2&\\&P0@r2*2(^DxbX\"X@Jx\\,6v6 Fxl\"@TNf8z<0& BR&J\\^|.Dd(2X2<HzR6|jdb<8<<l(F ZJZ`$&*0$T@.*h**L*\"N6JDlrZLxXJfb<jlT\\F(l|6@jl^:vlzNnPh&$\\vv>Dd\":J4@.$6rx<Ptnpp6\"jB&> \\.PPLF@x,XHbNv f|NBZ|,^:rPDh$b6:lHt``jj\"\"z,b@tjdf0.djZ4F^ftHBJXHZnj2R4@z624\"bFz@nTVrbld6ZD6`62l\"|0Rlb<Bf6&&&BZR0>jt$P 6tpJr,8*N@r.Z>br6ht>6n\"v,dLpH\\*XblL>*.N<`:NhLBV";
            timer.reset();
            timer.start();
            String output = toString(decrypt(bytes, key));
            timer.stop();
            System.out.println(output);
            System.out.println(timer.getTimeFormatted("MMF>M ssF>s mmmF>m xxxF>x nnnF>n"));
        }
    }

    public static String createKey(String username, String password, int security) {
        long checksum = 0;
        for (char c : username.toCharArray()) {
            checksum += c;
        }
        return rotate(createKey(password, security - 1) + createKey(username, security - 1), checksum);
    }

    public static String createKey(String password, int security) {
        if (security < 0) {
            security = 0;
        }
        if (security > 16) {
            security = 16;
        }
        security = (int)Math.pow(2, security + 8);

        long checksum = 0;
        for (char c : password.toCharArray()) {
            checksum += c;
        }

        int hashsum = 0;
        byte[] hash = StringUtils.hash(password, "SHA256");
        for (byte b : hash) {
            hashsum += b;
        }

        String key = toHex(hash).replace(" ", "") + new NumberRandom(checksum).getString(security / 2 - 64) + new NumberRandom(hashsum).getString(security / 2 - 64);
        key = toHex(hash).replace(" ", "") + mixUP(key, checksum + hashsum);
        return key;
    }

    private static String mixUP(String key, long random) {
        NumberRandom numberRandom = new NumberRandom(random);
        char[] chars = key.toCharArray();
        for (int i = 0; i < 10000; i++) {
            int x1 = numberRandom.getInt(chars.length);
            int x2 = numberRandom.getInt(chars.length);
            char c = chars[x1];
            chars[x1] = chars[x2];
            chars[x2] = c;
        }
        StringBuilder st = new StringBuilder();
        for (char c : chars) {
            st.append(c);
        }
        return st.toString();
    }

    public static String createKey(int security) {
        if (security < 0) {
            security = 0;
        }
        if (security > 16) {
            security = 16;
        }
        security += 8;
        return new NumberRandom().getString((int)Math.pow(2, security));
    }

    public static String createKey() {
        return createKey(2);
    }

    private static String pad() {
        return new NumberRandom().getString(8);
    }

    private static String pad(int length) {
        return new NumberRandom().getString(length);
    }

    private static String[] keys(String key) {
        StringBuilder st = new StringBuilder();
        String[] strings = new String[key.length() / 32];
        int i = 0;
        for (char c : key.toCharArray()) {
            st.append(c);
            if (st.length() == 32) {
                strings[i] = st.toString();
                st = new StringBuilder();
                i++;
            }
        }
        return strings;
    }

    private static boolean checkKey(int length) {
        if (length < 256) {
            return false;
        }
        int limit = 1;
        while (limit < length) {
            limit *= 2;
        }
        return length == limit;
    }

    public static byte[] encrypt(byte[] text, String key) {
        if (!checkKey(key.length())) {
            throw new EncryptionException("Key needs to be at least 256 bytes long and a power of 2. Your key was " + key.length() + " bytes long.");
        }

        String[] keys = keys(key);
        byte[] bytes = new byte[text.length + pad().length() + 33];

        char[] pad = pad().toCharArray();
        byte[] checksum = StringUtils.hash(toString(text), "SHA-256");
        char[] chars = new char[bytes.length];
        for (int i = 0; i < pad.length; i++) {
            chars[i + 1] = pad[i];
        }
        for (int i = 0; i < checksum.length; i++) {
            chars[i + 1 + pad.length] = (char)checksum[i];
        }
        for (int i = 0; i < text.length; i++) {
            chars[i + 1 + pad.length + checksum.length] = (char)text[i];
        }

        for (int i = 1; i < bytes.length; i++) {

            int l = i - 1;
            int remove = (i < 8 ? i - 1 : 7);
            byte b = (byte)chars[i];

            for (int j = l; j >= l - remove; j--) {
                b = encrypt(b, rotate(keys[((int)chars[j] + 128) % keys.length], bytes[j] + 128));
            }
            for (int j = 0; j < keys.length; j++) {
                b = (encrypt(b, keys[j]));
            }

            bytes[i] = b;
        }

        byte b = 0;
        for (int i = 0; i < bytes.length; i++) {
            b += bytes[i] * i;
        }
        bytes[0] = b;

        NumberRandom numberRandom = new NumberRandom(b);
        for (int i = 0; i < 1000; i++) {
            int x1 = numberRandom.getInt(bytes.length - 1) + 1;
            int x2 = numberRandom.getInt(bytes.length - 1) + 1;
            b = bytes[x1];
            bytes[x1] = bytes[x2];
            bytes[x2] = b;
        }

        return bytes;
    }

    public static byte[] decrypt(byte[] bytes, String key) {
        if (!checkKey(key.length())) {
            throw new EncryptionException("Key needs to be at least 256 bytes long and a power of 2. Your key was " + key.length() + " bytes long.");
        }

        String[] keys = keys(key);
        byte[] intermediate = new byte[bytes.length];

        byte b = bytes[0];
        NumberRandom numberRandom = new NumberRandom(b);
        List<Integer> switches = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            switches.add(numberRandom.getInt(bytes.length - 1) + 1);
            switches.add(numberRandom.getInt(bytes.length - 1) + 1);
        }
        for (int i = switches.size() - 1; i >= 0; i -= 2) {
            int x1 = switches.get(i);
            int x2 = switches.get(i - 1);
            b = bytes[x1];
            bytes[x1] = bytes[x2];
            bytes[x2] = b;
        }

        bytes[0] = 0;

        for (int i = 1; i < bytes.length; i++) {

            int l = i - 1;
            int remove = (i < 8 ? i - 1 : 7);
            b = bytes[i];

            for (int j = keys.length - 1; j >= 0; j--) {
                b = (decrypt(b, keys[j]));
            }
            for (int j = l - remove; j <= l; j++) {
                b = decrypt(b, rotate(keys[((int)intermediate[j] + 128) % keys.length], bytes[j] + 128));
            }

            intermediate[i] = b;
        }

        int shift = pad().length() + 1;
        for (int i = shift; i < intermediate.length; i++) {
            intermediate[i - shift] = intermediate[i];
        }
        intermediate = Arrays.copyOf(intermediate, intermediate.length - shift);

        byte[] checksum1 = Arrays.copyOf(intermediate, 32);
        shift = 32;
        for (int i = shift; i < intermediate.length; i++) {
            intermediate[i - shift] = intermediate[i];
        }
        intermediate = Arrays.copyOf(intermediate, intermediate.length - shift);

        byte[] checksum2 = StringUtils.hash(toString(intermediate), "SHA-256");
        if (!Arrays.equals(checksum1, checksum2)) {
            return new byte[0];
        }

        return Arrays.copyOf(intermediate, intermediate.length);
    }

    private static byte encrypt(byte b, String key) {
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == XOR) {
                b = xor(b, key);
            } else if (operations.get(i) == NOT) {
                b = not(b);
            } else if (operations.get(i) == PLUS) {
                b = plus(b, key);
            } else if (operations.get(i) == MINUS) {
                b = subtract(b, key);
            } else if (operations.get(i) == XOR_PLUS) {
                b = xorPlus(b, key);
            } else if (operations.get(i) == XOR_MINUS) {
                b = xorMinus(b, key);
            }
        }
        return b;
    }

    private static byte decrypt(byte b, String key) {
        for (int i = operations.size() - 1; i >= 0; i--) {
            if (operations.get(i) == XOR) {
                b = xor(b, key);
            } else if (operations.get(i) == NOT) {
                b = not(b);
            } else if (operations.get(i) == PLUS) {
                b = subtract(b, key);
            } else if (operations.get(i) == MINUS) {
                b = plus(b, key);
            } else if (operations.get(i) == XOR_PLUS) {
                b = xorPlusInverse(b, key);
            } else if (operations.get(i) == XOR_MINUS) {
                b = xorMinusInverse(b, key);
            }
        }
        return b;
    }

    private static byte xor(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
        }
        return b;
    }

    private static byte not(byte b) {
        b &= 0xFF;
        return b;
    }

    private static byte plus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b += c;
        }
        return b;
    }

    private static byte subtract(byte b, String key) {
        for (char c : key.toCharArray()) {
            b -= c;
        }
        return b;
    }

    private static byte xorPlus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
            b += c;
        }
        return b;
    }

    private static byte xorPlusInverse(byte b, String key) {
        char[] chars = key.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            b -= chars[i];
            b ^= chars[i];
        }
        return b;
    }

    private static byte xorMinus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
            b -= c;
        }
        return b;
    }

    private static byte xorMinusInverse(byte b, String key) {
        char[] chars = key.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            b += chars[i];
            b ^= chars[i];
        }
        return b;
    }

    private static byte[] checksum(String[] keys) {
        byte[] bytes = new byte[keys.length];
        for (int i = 0; i < keys.length; i++) {
            byte b = 0;
            char[] chars = keys[i].toCharArray();
            for (int j = 0; j < keys[i].length(); j++) {
                b += chars[j];
            }
            bytes[i] = b;
        }
        return bytes;
    }

    private static String rotate(String key, long rotation) {
        if (rotation % key.length() == 0) {
            return key;
        }

        rotation = rotation % key.length();
        char[] chars = key.toCharArray();
        char[] output = new char[key.length()];
        for (int i = 0; i < chars.length; i++) {
            output[(int)(i + rotation) % chars.length] = chars[i];
        }

        StringBuilder st = new StringBuilder();
        for (char c : output) {
            st.append(c);
        }
        return st.toString();
    }

    public static byte[] toBytes(String s) {
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

    public static String toHex(char[] chars) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (char c : chars) {
            if (t) st.append(' ');
            st.append(String.format("%02X", (byte)c));
            t = true;
        }
        return st.toString();
    }

    public static String toHex(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (byte b : bytes) {
            if (t) st.append(' ');
            st.append(String.format("%02X", b));
            t = true;
        }
        return st.toString();
    }

    public static String toString(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append((char)b);
        }
        return st.toString();
    }

}