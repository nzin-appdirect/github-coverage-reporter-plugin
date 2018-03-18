package io.jenkins.plugins.gcr.models;

import io.jenkins.plugins.gcr.models.Coverage;

public abstract class XmlCoverage implements Coverage {

    @Override
    public double getOverallRate() {
        // This is as close to sonar coverage as we can get
        // regardless of the XML type used
        // TODO: try and improve this in the future
        return (getLineRate() + getBranchRate()) / 2.0;
    }
}
