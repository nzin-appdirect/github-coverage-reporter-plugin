package io.jenkins.plugins.gcr.models;

public class DefaultCoverage implements Coverage {

    private double lineRate;

    private double branchRate;

    public DefaultCoverage(double lineRate, double branchRate) {
        this.lineRate = lineRate;
        this.branchRate = branchRate;
    }

    @Override
    public double getLineRate() {
        return lineRate;
    }

    @Override
    public double getBranchRate() {
        return branchRate;
    }
}
