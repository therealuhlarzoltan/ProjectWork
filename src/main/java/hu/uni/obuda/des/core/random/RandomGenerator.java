package hu.uni.obuda.des.core.random;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;

public final class RandomGenerator {

    private static final org.apache.commons.math3.random.RandomGenerator SHARED_RANDOM_GENERATOR = new Well19937c();
    private static final RandomDataGenerator RANDOM_DATA_GENERATOR = new RandomDataGenerator(SHARED_RANDOM_GENERATOR);

    private LogNormalDistribution logNormalDistribution;
    private GammaDistribution gammaDistribution;
    private NormalDistribution normalDistribution;

    public void setLogNormalParameters(double scale, double shape) {
        if (shape <= 0) {
            throw new IllegalArgumentException("Shape parameter must be positive");
        }
        logNormalDistribution = new LogNormalDistribution(SHARED_RANDOM_GENERATOR, scale, shape);
    }

    public void setGammaParameters(double shape, double scale) {
        if (shape <= 0 || scale <= 0) {
            throw new IllegalArgumentException("Shape and scale parameters must be positive");
        }
        gammaDistribution = new GammaDistribution(SHARED_RANDOM_GENERATOR, shape, scale);
    }


    public void setNormalParameters(double mean, double stddev) {
        if (stddev <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive");
        }
        normalDistribution = new NormalDistribution(SHARED_RANDOM_GENERATOR, mean, stddev);
    }

    public double generateLogNormal() {
        if (logNormalDistribution == null) {
            throw new IllegalStateException("LogNormal distribution parameters are not set");
        }
        return logNormalDistribution.sample();
    }

    public double generateGamma() {
        if (gammaDistribution == null) {
            throw new IllegalStateException("Gamma distribution parameters are not set");
        }
        return gammaDistribution.sample();
    }

    public double generateUniform(double lowerBound, double upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }
        return RANDOM_DATA_GENERATOR.nextUniform(lowerBound, upperBound);
    }

    public double generateNormal() {
        if (normalDistribution == null) {
            throw new IllegalStateException("Normal distribution parameters are not set");
        }
        return normalDistribution.sample();
    }
}
