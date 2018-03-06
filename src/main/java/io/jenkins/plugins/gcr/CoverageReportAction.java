package io.jenkins.plugins.gcr;

import hudson.model.Action;
import io.jenkins.plugins.gcr.models.Coverage;

public class CoverageReportAction implements Action {

    private Coverage coverage;

    private Coverage expectedCoverage;

    private String lineRateDescription;

    private String branchRateDescription;

    private String expectedLineRateDescription;

    public CoverageReportAction(Coverage coverage, Coverage expectedCoverage) {
        this.coverage = coverage;
        this.expectedCoverage = expectedCoverage;

        this.lineRateDescription = String.format("%.2f%%", coverage.getLineRate() * 100.0);
        this.branchRateDescription = String.format("%.2f%%", coverage.getBranchRate() * 100.0);
    }

    // Getters

    public Coverage getCoverage() {
        return coverage;
    }

    public String getLineRateDescription() {
        return lineRateDescription;
    }

    public String getBranchRateDescription() {
        return branchRateDescription;
    }

    public String getExpectedLineRateDescription() { return expectedLineRateDescription; }

    public String getLineRateDifference() {
        if (isAcceptableCoverage()) {
            double difference = coverage.getLineRate() - expectedCoverage.getLineRate();
            return String.format("+%.2f", difference);
        } else {
            double difference = expectedCoverage.getLineRate() - coverage.getLineRate();
            return String.format("-%.2f", difference);
        }
    }

    public boolean isAcceptableCoverage() {
        return coverage.getLineRate() >= expectedCoverage.getLineRate();
    }

    public String getStatusDescription() {
        // TODO: localise
        final String template = "Coverage of %.2f%% is %s expected %.2f%%.";
        final String adjective = isAcceptableCoverage() ? "greater than or equal to" : "lower than";
        final double rate = coverage.getLineRate() * 100.0;
        final double expectedRate = expectedCoverage.getLineRate() * 100.0;
        return String.format(template, rate, adjective, expectedRate);
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
