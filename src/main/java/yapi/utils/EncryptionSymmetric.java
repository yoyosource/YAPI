package yapi.utils;

import yapi.exceptions.EncryptionException;
import yapi.math.NumberRandom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EncryptionSymmetric {

    public static void main(String[] args) {
        if (true) {
            for (int i = 0; i < 100; i++) {
                decrypt(encrypt(createKey(), createKey()), createKey());
            }
        }

        if (false) {
            byte[] bytes = createBytes("55 5E 26 A4 A8 BC BC 4A B4 94 16 E4 48 38 AE 58 B6 59 C3 C8 F4 22 F5 4E 60 4C C9 CA A4 63 4F 04 39 6A 63 6A 40 C9 0E EC B0 4B F1 CE AE 38 2A 15 A9 D9 B4 0D 83 F4 98 2B 6B 78 2E A9 FC 8C 60 B9 A1 66 A8 84 55 4B 08 75 44 B1 A2 A5 CE F4 EB 9B 48 CA D5 A1 9A 85 2E EE 48 A4 49 83 08 7B 35 6E 2E 78 F5 2B 90 1C C8 28 55 D8 A1 13 44 28 31 7D EE 69 EE 2C BB 2F 41 DC 51 B0 11 EA 64 94 59 1A A5 D0 24 A3 4D 21 C0 7C CD 53 70 BC B7 18 1A 8D 44 AA 61 64 FD 0C 98 58 FA 55 4E 4E");
            String s = decrypt(bytes, "h&dX`N`fL>\"Dtvfzj&2vHFtL>@X(BZ`fD&DJT BFB|6Nr b(86h&P4V4^`Vp8|n$L\\xb.hnj(4 `,jZljLz\\N(zfrVzBrJ4&,B*LPXTr20\\n0TF2jjv6>6lp@0>NbZj<:x4\"JDH|^,<L4lt$hXZz(6:4fZ^NLPdLL` 8 j<t^n@>vXBp*TpLX`&Xz R\"N<0|0>Nrpl2f@2:nNnVVp zNRXd8(fVbv,HX6Tt:8LxJVDBJpV`&(VP2Z4d||Zr>n,^.");
            System.out.println(s);
        }

        if (true) {
            String key = ".` rFrN(>8><&Z^0@h\\F8FVxdL.J,&@,f4<nxBPB&z, XV0N\"b*bJhj |XhZ HR|@Bh:@::\\22@@<lrNX6bvFJ|N,jJz*XVT\\4|f(2^D6zd>R\\lTXZzbPZ.V<0ZHpTV*h|(bN:>R(`xJvVDZr`0:j0nnX^Bp$zxr:\"$nBhN(P60JrxF20zx<P6^$*p &hnd8fNR&x:z8n t:\\N>ZZ,.$V,XBL^6J6zz,xXrrP&hnx@4&@8P\\n*^,6FFd|dN&pd>|J$<Br8 >\\`<h\\6Z|TzFH^@^(fRz&L6Z,V.\\R* trJ^.TfHzH,xH`LVdl(H`6Jt8 2RT*Zhp@l&Dp*6<4nhn8hVR<`T&|n<TbPjv\"p|0rrdtR:.p>fz*.Fh|Dnf.>4Z4hrvV2dPnX4 6 BZ^ $FT2>rt4*Dxh.db^8b8flb.rRx6$Z<\\2^>*d\\Vtt\\R2NJ^>6XFD<$(`<f\\XH \">d >xp,H&VdDJvT\\2*^Xl2jhj<J86T0@b(4(>DTh8DHDzbX2`Vh^zF0TFTPVHFBzhh*bX^$P&\\BL@r\\.j<Z02vFDvhzFnDZ&P^Nx@$(x\\F@(,H$LRf&ZJ4Nj&.f2*^,tlfD\\FVB&&&$nN4@VrLp|\"$0b4p| b<2,z\\(P<r*2\"(J4h6x\\*l,Hp`J.,$,hvFn8hF.z`J.@\\d\"BJH\":x2x2&2@v v:P\\&\"bV<(@N4xtT40:>Xx2&\\&P0@r2*2(^DxbX\"X@Jx\\,6v6 Fxl\"@TNf8z<0& BR&J\\^|.Dd(2X2<HzR6|jdb<8<<l(F ZJZ`$&*0$T@.*h**L*\"N6JDlrZLxXJfb<jlT\\F(l|6@jl^:vlzNnPh&$\\vv>Dd\":J4@.$6rx<Ptnpp6\"jB&> \\.PPLF@x,XHbNv f|NBZ|,^:rPDh$b6:lHt``jj\"\"z,b@tjdf0.djZ4F^ftHBJXHZnj2R4@z624\"bFz@nTVrbld6ZD6`62l\"|0Rlb<Bf6&&&BZR0>jt$P 6tpJr,8*N@r.Z>br6ht>6n\"v,dLpH\\*XblL>*.N<`:NhLBV";
            key = createKey(0);
            System.out.println(key);
            String toCrypt = "Ich bin der coder32 und kann ziemlich schnell auf Tastaturen schreiben. Das kann ich, da ich einen Computer habe, seit ich 7 Jahre alt bin.";
            //toCrypt = new NumberRandom().getString(1024);
            long time = System.currentTimeMillis();
            byte[] bytes = encrypt(toCrypt, key);
            time = System.currentTimeMillis() - time;
            System.out.println(StringUtils.toHex(bytes, true));
            System.out.println(time + "ms");
            //key = ".` rFrN(>8><&Z^0@h\\F8FVxdL.J,&@,f4<nxBPB&z, XV0N\"b*bJhj |XhZ HR|@Bh:@::\\22@@<lrNX6bvFJ|N,jJz*XVT\\4|f(2^D6zd>R\\lTXZzbPZ.V<0ZHpTV*h|(bN:>R(`xJvVDZr`0:j0nnX^Bp$zxr:\"$nBhN(P60JrxF20zx<P6^$*p &hnd8fNR&x:z8n t:\\N>ZZ,.$V,XBL^6J6zz,xXrrP&hnx@4&@8P\\n*^,6FFd|dN&pd>|J$<Br8 >\\`<h\\6Z|TzFH^@^(fRz&L6Z,V.\\R* trJ^.TfHzH,xH`LVdl(H`6Jt8 2RT*Zhp@l&Dp*6<4nhn8hVR<`T&|n<TbPjv\"p|0rrdtR:.p>fz*.Fh|Dnf.>4Z4hrvV2dPnX4 6 BZ^ $FT2>rt4*Dxh.db^8b8flb.rRx6$Z<\\2^>*d\\Vtt\\R2NJ^>6XFD<$(`<f\\XH \">d >xp,H&VdDJvT\\2*^Xl2jhj<J86T0@b(4(>DTh8DHDzbX2`Vh^zF0TFTPVHFBzhh*bX^$P&\\BL@r\\.j<Z02vFDvhzFnDZ&P^Nx@$(x\\F@(,H$LRf&ZJ4Nj&.f2*^,tlfD\\FVB&&&$nN4@VrLp|\"$0b4p| b<2,z\\(P<r*2\"(J4h6x\\*l,Hp`J.,$,hvFn8hF.z`J.@\\d\"BJH\":x2x2&2@v v:P\\&\"bV<(@N4xtT40:>Xx2&\\&P0@r2*2(^DxbX\"X@Jx\\,6v6 Fxl\"@TNf8z<0& BR&J\\^|.Dd(2X2<HzR6|jdb<8<<l(F ZJZ`$&*0$T@.*h**L*\"N6JDlrZLxXJfb<jlT\\F(l|6@jl^:vlzNnPh&$\\vv>Dd\":J4@.$6rx<Ptnpp6\"jB&> \\.PPLF@x,XHbNv f|NBZ|,^:rPDh$b6:lHt``jj\"\"z,b@tjdf0.djZ4F^ftHBJXHZnj2R4@z624\"bFz@nTVrbld6ZD6`62l\"|0Rlb<Bf6&&&BZR0>jt$P 6tpJr,8*N@r.Z>br6ht>6n\"v,dLpH\\*XblL>*.N<`:NhLBV";
            time = System.currentTimeMillis();
            String output = decrypt(bytes, key);
            time = System.currentTimeMillis() - time;
            System.out.println(output);
            System.out.println(time + "ms");
        }
    }

    public static String createKey(int security) {
        if (security == 0) {
            return new NumberRandom().getString(256 );
        } else if (security == 1) {
            return new NumberRandom().getString(512);
        } else if (security == 2) {
            return new NumberRandom().getString(1024);
        } else if (security == 3) {
            return new NumberRandom().getString(2048);
        } else if (security == 4) {
            return new NumberRandom().getString(4096);
        } else if (security == 5) {
            return new NumberRandom().getString(8192);
        } else {
            return createKey();
        }
    }

    public static String createKey() {
        return new NumberRandom().getString(1024);
    }

    public static String pad() {
        return new NumberRandom().getString(8);
    }

    public static String pad(int length) {
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

    public static byte[] encrypt(File file, String key) {
        try {
            return encrypt(new FileInputStream(file), key);
        } catch (IOException e) {
            throw new EncryptionException("Exception while opening File");
        }
    }

    public static byte[] encrypt(FileInputStream fileInputStream, String key) {
        try {
            byte[] bytes = fileInputStream.readAllBytes();
            return encrypt(StringUtils.toString(bytes), key);
        } catch (IOException e) {
            throw new EncryptionException("Exception while reading File");
        }
    }

    public static byte[] encrypt(String text, String key) {
        if (!(key.length() == 256 || key.length() == 512 || key.length() == 1024 || key.length() == 2084 || key.length() == 4096 || key.length() == 8192)) {
            throw new EncryptionException("Key needs to be 512, 1024, 2084, 4186 or 8192 bytes long");
        }
        String[] keys = keys(key);
        byte[] checksum = checksum(keys);
        text = pad(checksum.length + 1) + pad() + text;
        char[] chars = text.toCharArray();
        byte[] bytes = new byte[text.length()];
        for (int i = 0; i < checksum.length; i++) {
            bytes[i + 1] = checksum[i];
        }
        for (int i = checksum.length; i < chars.length; i++) {
            if (i < 8) {
                bytes[i] = (xor((byte)chars[i], keys[i]));
            } else {
                byte b = (byte)chars[i];
                int l = i - 1;
                for (int j = l; j >= l - 7; j--) {
                    b = xor(b, keys[((int)bytes[j] + 128) % keys.length]);
                }
                for (int j = 0; j < keys.length; j++) {
                    b = (xor(b, keys[j]));
                }
                bytes[i] = b;
            }
        }
        byte b = 0;
        for (int i = 0; i < bytes.length; i++) {
            b += bytes[i] * i;
        }
        bytes[0] = b;
        return bytes;
    }

    public static String decrypt(File file, String key) {
        try {
            return decrypt(new FileInputStream(file), key);
        } catch (IOException e) {
            throw new EncryptionException("Exception while opening File");
        }
    }

    public static String decrypt(FileInputStream fileInputStream, String key) {
        try {
            byte[] bytes = fileInputStream.readAllBytes();
            return decrypt(bytes, key);
        } catch (IOException e) {
            throw new EncryptionException("Exception while reading File");
        }
    }

    public static String decrypt(byte[] bytes, String key) {
        if (!(key.length() == 256 || key.length() == 512 || key.length() == 1024 || key.length() == 2084 || key.length() == 4096 || key.length() == 8192)) {
            throw new EncryptionException("Key needs to be 512, 1024, 2084, 4186 or 8192 bytes long");
        }
        StringBuilder st = new StringBuilder();
        String[] keys = keys(key);
        byte[] checksum = checksum(keys);
        for (int i = 1; i < checksum.length; i++) {
            if (bytes[i] != checksum[i - 1]) {
                keys[i - 1] = pad(32);
            }
        }
        byte c = 0;
        for (int i = 1; i < bytes.length; i++) {
            c += bytes[i] * i;
        }
        if (bytes[0] != c) {
            keys[0] = pad(32);
        }
        for (int i = bytes.length - 1; i >= 8 + checksum.length + 1; i--) {
            byte b = bytes[i];
            for (int j = keys.length - 1; j >= 0; j--) {
                b = xorInverse(b, keys[j]);
            }
            int l = i - 1;
            for (int j = l - 7; j <= l; j++) {
                b = xorInverse(b, keys[((int)bytes[j] + 128) % keys.length]);
            }
            st.append((char)b);
        }
        return st.reverse().toString();
    }

    private static byte xor(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
            b += c;
        }
        return b;
    }

    private static byte xorInverse(byte b, String key) {
        char[] chars = key.toCharArray();
        for (int i = key.length() - 1; i >= 0; i--) {
            b -= chars[i];
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

    public static byte[] createBytes(String s) {
        String[] strings = s.split(" ");
        byte[] bytes = new byte[strings.length];
        for (int i = 0; i < strings.length; i++) {
            bytes[i] = (byte)(Integer.parseInt(strings[i], 16));
        }
        return bytes;
    }

}