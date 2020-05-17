package yapi.encryption.diffihellman;

import yapi.encryption.fastencryption.FastEncrytptionSymmetric;
import yapi.math.base.BaseConversion;

import java.math.BigInteger;
import java.security.SecureRandom;

public class DiffieHellman {

    public static void main(String[] args) {
        DiffieHellman diffieHellman1 = new DiffieHellman();
        DiffieHellman diffieHellman2 = new DiffieHellman(diffieHellman1.getPublicG(), diffieHellman1.getPublicP());

        BigInteger bigInteger1 = diffieHellman1.getValue();
        BigInteger bigInteger2 = diffieHellman2.getValue();

        diffieHellman1.init().round(bigInteger2).finish();
        diffieHellman2.init().round(bigInteger1).finish();

        System.out.println(diffieHellman1.getDiffieHellmanResult().getSharedSecret());
        System.out.println(diffieHellman2.getDiffieHellmanResult().getSharedSecret());

        System.out.println(createKey(diffieHellman1.getDiffieHellmanResult().getSharedSecret()));
    }

    private static int bitLength = 1024;

    private class Values {

        private BigInteger g;
        private BigInteger p;

        private BigInteger privateValue = new BigInteger(bitLength, new SecureRandom());

        private BigInteger temp = BigInteger.ZERO;
        private BigInteger yourValue = BigInteger.ZERO;

        private void destroy() {
            g = BigInteger.ZERO;
            p = BigInteger.ZERO;
            privateValue = BigInteger.ZERO;
            temp = BigInteger.ZERO;
            yourValue = BigInteger.ZERO;
        }

        private void init() {
            g = BigInteger.probablePrime(bitLength, new SecureRandom());
            while (true) {
                BigInteger bigInteger = BigInteger.probablePrime(bitLength, new SecureRandom());
                if (bigInteger.compareTo(g) != 0) {
                    p = bigInteger;
                    break;
                }
            }
        }

        private void init(BigInteger g, BigInteger p) {
            this.g = g;
            this.p = p;
        }
    }

    private Values values;
    private DiffieHellmanResult diffieHellmanResult;

    public DiffieHellman() {
        values = new Values();
        values.init();
    }

    public DiffieHellman(String g, String p) {
        this(new BigInteger(g), new BigInteger(p));
    }

    public DiffieHellman(BigInteger g, BigInteger p) {
        values = new Values();
        values.init(g, p);
    }

    private class DiffieHellmanFactory {

        private Values values;
        private DiffieHellman diffieHellman;

        public DiffieHellmanFactory(Values values, DiffieHellman diffieHellman) {
            this.values = values;
            this.diffieHellman = diffieHellman;
        }

        public DiffieHellmanFactory round(BigInteger x) {
            values.temp = x.modPow(values.privateValue, values.p);
            return this;
        }

        public DiffieHellmanResult finish() {
            diffieHellman.diffieHellmanResult = new DiffieHellmanResult(values.temp);
            values.destroy();
            return diffieHellman.diffieHellmanResult;
        }

    }

    private class DiffieHellmanResult {

        private BigInteger sharedSecret;

        public DiffieHellmanResult(BigInteger sharedSecret) {
            this.sharedSecret = sharedSecret;
        }

        public BigInteger getSharedSecret() {
            return sharedSecret;
        }

        public void destroy() {
            sharedSecret = BigInteger.ZERO;
        }

    }

    public BigInteger getPublicG() {
        if (values.yourValue.compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalStateException();
        }
        return values.g;
    }

    public BigInteger getPublicP() {
        if (values.yourValue.compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalStateException();
        }
        return values.p;
    }

    public BigInteger getValue() {
        values.yourValue = values.g.modPow(values.privateValue, values.p);
        return values.yourValue.add(BigInteger.ZERO);
    }

    public DiffieHellmanFactory init() {
        if (diffieHellmanResult != null) {
            throw new IllegalStateException();
        }
        if (values.yourValue.compareTo(BigInteger.ZERO) == 0) {
            throw new IllegalStateException("Use getValue() first");
        }
        return new DiffieHellmanFactory(values, this);
    }

    public DiffieHellmanResult getDiffieHellmanResult() {
        if (diffieHellmanResult == null) {
            throw new IllegalStateException();
        }
        return diffieHellmanResult;
    }

    public static String createKey(BigInteger sharedSecret) {
        String s = sharedSecret.toString();
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String b2 = BaseConversion.toBase2(s.charAt(i) - 0x30);
            binary.append("0".repeat(4 - b2.length())).append(b2);
        }

        String base64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";
        binary.append("0".repeat(6 - binary.length() % 6));

        StringBuilder password = new StringBuilder();
        while (binary.length() >= 6) {
            String b = binary.substring(0, 6);
            password.append(base64.charAt(BaseConversion.fromBase2toInt(b)));
            binary.delete(0, 6);
        }

        return FastEncrytptionSymmetric.createKey(password.toString());
    }

}
