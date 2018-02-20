package io.jenkins.plugins.gcr.models;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class ComparisonOption {

    private String value;

    private String sonarProject;

    private String fixedCoverage;

    @DataBoundConstructor
    public ComparisonOption(String value, String sonarProject, String fixedCoverage) {
        this.value = value;
        this.sonarProject = sonarProject;
        this.fixedCoverage = fixedCoverage;
    }

    // Getters / Setters


    public String getValue() {
        return value;
    }

    @DataBoundSetter
    public void setValue(String value) {
        this.value = value;
    }

    public String getSonarProject() {
        return sonarProject;
    }

    @DataBoundSetter
    public void setSonarProject(String sonarProject) {
        this.sonarProject = sonarProject;
    }

    @DataBoundSetter
    public String getFixedCoverage() {
        return fixedCoverage;
    }

    // Helpers

    public boolean isTypeSonarProject() {
        return sonarProject != null;
    }

    public boolean isTypeFixedCoverage() {
        return fixedCoverage != null;
    }

    public double fixedCoverageAsDouble() {
        return Double.parseDouble(getFixedCoverage());
    }
}
