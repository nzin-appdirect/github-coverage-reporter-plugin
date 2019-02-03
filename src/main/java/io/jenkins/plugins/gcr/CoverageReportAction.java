package io.jenkins.plugins.gcr;

import hudson.model.Action;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.CoverageRateType;
import io.jenkins.plugins.gcr.models.CoverageType;

import static io.jenkins.plugins.gcr.models.CoverageType.JACOCO;

public class CoverageReportAction implements Action {

    private Coverage coverage;

    private Coverage expectedCoverage;

    private CoverageRateType coverageType;

    public CoverageReportAction(Coverage coverage, Coverage expectedCoverage, CoverageRateType coverageType) {
        this.coverage = coverage;
        this.expectedCoverage = expectedCoverage;
        this.coverageType = coverageType;
    }

    // Action description generation

    private double toChosenCoverageValue(Coverage coverage) {
        switch (coverageType) {
            case LINE:
                return coverage.getLineRate();
            case BRANCH:
                return coverage.getBranchRate();
            case OVERALL:
                return coverage.getOverallRate();
        }
        return 0.0; // TODO: throw exception?
    }

    private String toRateFragment(Coverage coverage) {
        return String.format("%.2f%%", toChosenCoverageValue(coverage) * 100.0f);
    }

    private String toRateDifferenceFragment(double expectedRate, double actualRate) {
        // TODO: Not in use yet
        if (isAcceptableCoverage()) {
            double difference = actualRate - expectedRate;
            return String.format("+%.2f%%", difference * 100.0);
        } else {
            double difference = expectedRate - actualRate;
            return String.format("-%.2f%%", difference * 100.0);
        }
    }


    // Public interface

    public String getStatusDescription() {
        // TODO: localise
        final String template = "Coverage of %s is %s expected %s.";

        final String fragment = isAcceptableCoverage() ? "greater than or equal to" : "lower than";
        final String actualRate = toRateFragment(coverage);
        final String expectedRate = toRateFragment(expectedCoverage);

        return String.format(template, actualRate, fragment, expectedRate);
    }

    public String getStatusName() {
        return isAcceptableCoverage() ? "success" : "failure";
    }

    public boolean isAcceptableCoverage() {
        return toChosenCoverageValue(coverage) >= toChosenCoverageValue(expectedCoverage);
    }


    // Required fields

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }
}
