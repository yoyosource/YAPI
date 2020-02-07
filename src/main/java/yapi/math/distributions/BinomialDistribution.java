package yapi.math.distributions;

import yapi.exceptions.MathException;
import yapi.math.NumberUtils;

import java.math.BigInteger;

public class BinomialDistribution {

    private long size;
    private double probability;

    double mu;
    double sigma;

    public BinomialDistribution(long size, double probability) {
        this.size = size;
        if (probability <= 1 && probability >= 0) {
            this.probability = probability;
        } else {
            throw new MathException("Probability needs to be between 0 and 1");
        }

        mu = size * probability;
        sigma = Math.sqrt(size * probability * (1 - probability));
    }

    public double getBinomial(long experiment) {
        return over(size, experiment) * Math.pow(probability, experiment) * Math.pow(1 - probability, size - experiment);
    }

    private long over(long n, long r) {
        BigInteger bn = BigInteger.valueOf(n);
        BigInteger br = BigInteger.valueOf(r);
        return NumberUtils.factorial(bn).divide(NumberUtils.factorial(br).multiply(NumberUtils.factorial(bn.subtract(br)))).longValue();
    }

    public double getFunction(long experiment) {
        double d = 0;
        for (long i = 0; i <= experiment; i++) {
            d += getBinomial(i);
        }
        return d;
    }

    public double getProbability(long experiment) {
        return getBinomial(experiment);
    }

    public double getProbabilityBetween(int x1, int x2, boolean ignoreEdges) {
        if (x2 < x1) {
            int x3 = x2;
            x2 = x1;
            x1 = x3;
        }
        if (ignoreEdges) {
            x2--;
        } else {
            x1--;
        }
        return getFunction(x2) - getFunction(x1);
    }

    public double getProbabilityBelow(long experiment) {
        return getFunction(experiment);
    }

    public double getProbabilityAbove(long experiment) {
        return 1 - getFunction(experiment);
    }

    public double getMu() {
        return mu;
    }

    public double getSigma() {
        return sigma;
    }

}
