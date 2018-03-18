package io.jenkins.plugins.gcr.models;

public interface Coverage {

    double getLineRate();

    double getBranchRate();

    double getOverallRate();

}
