// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.distributions;

import yapi.exceptions.MathException;
import yapi.math.NumberUtils;

import java.math.BigInteger;

public class BinomialDistribution {

    private long size;
    private double probability;

    private double mu;
    private double sigma;

    public BinomialDistribution(long size, double probability) {
        this.size = size;
        if (probability <= 1 && probability >= 0) {
            this.probability = probability;
        } else {
            throw new MathException("Probability needs to be between 0 and 1");
        }
        calcMu();
        calcSigma();
    }

    private void calcMu() {
        mu = size * probability;
    }

    private void calcSigma() {
        sigma = Math.sqrt(size * probability * (1 - probability));
    }

    public double getBinomial(long experiment) {
        if (experiment < 0 || experiment > size) {
            return -1;
        }
        return over(size, experiment) * Math.pow(probability, experiment) * Math.pow(1 - probability, size - experiment);
    }

    public double[] getBinomial(long startExperiment, long stopExperiment) {
        if (stopExperiment < startExperiment) {
            return new double[0];
        }
        double[] doubles = new double[(int)(stopExperiment - startExperiment + 1)];
        int j = 0;
        for (long i = startExperiment; i <= stopExperiment; i++) {
            doubles[j] = getBinomial(i);
            j++;
        }
        return doubles;
    }

    public double[] getBinomial() {
        return getBinomial(0, size);
    }

    private long over(long n, long r) {
        return NumberUtils.over(BigInteger.valueOf(n), BigInteger.valueOf(r)).longValue();
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

    public double getProbabilityEqual(long experiment) {
        return getBinomial(experiment);
    }

    public double getProbabilityNotEqual(long experiment) {
        return 1 - getBinomial(experiment);
    }

    public double getProbabilityBetween(int x1, int x2) {
        if (x2 < x1) {
            int x3 = x2;
            x2 = x1;
            x1 = x3;
        }
        x2--;
        return getFunction(x2) - getFunction(x1);
    }

    public double getProbabilityBetweenAndEqual(int x1, int x2) {
        if (x2 < x1) {
            int x3 = x2;
            x2 = x1;
            x1 = x3;
        }
        x1--;
        return getFunction(x2) - getFunction(x1);
    }

    public double getProbabilityBelow(long experiment) {
        return getFunction(experiment - 1);
    }

    public double getProbabilityBelowAndEqual(long experiment) {
        return getFunction(experiment);
    }

    public double getProbabilityAbove(long experiment) {
        return 1 - getFunction(experiment);
    }

    public double getProbabilityAboveAndEqual(long experiment) {
        return 1 - getFunction(experiment - 1);
    }

    public double getMu() {
        return mu;
    }

    public double getExpectedValue() {
        return getMu();
    }

    public double getSigma() {
        return sigma;
    }

    public double getExpectedDeviation() {
        return getSigma();
    }

}