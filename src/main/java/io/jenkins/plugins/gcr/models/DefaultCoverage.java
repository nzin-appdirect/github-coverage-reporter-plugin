package io.jenkins.plugins.gcr.models;

public class DefaultCoverage implements Coverage {

    private double lineRate;

    private double branchRate;

    private double overallRate;

    public DefaultCoverage(double overallRate, double lineRate, double branchRate) {
        this.lineRate = lineRate;
        this.branchRate = branchRate;
        this.overallRate = overallRate;
    }

    @Override
    public double getLineRate() {
        return lineRate;
    }

    @Override
    public double getBranchRate() {
        return branchRate;
    }

    @Override
    public double getOverallRate() { return overallRate; }
}
