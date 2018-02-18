package io.jenkins.plugins.gcr;

import hudson.model.Action;
import io.jenkins.plugins.gcr.models.Coverage;

public class CoverageReportAction implements Action {

    private Coverage coverage;

    private String lineRateDescription;

    private String branchRateDescription;

    public CoverageReportAction(Coverage coverage) {
        this.coverage = coverage;

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
