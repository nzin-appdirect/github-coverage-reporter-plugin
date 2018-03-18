package io.jenkins.plugins.gcr.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "coverage")
public class CoberturaCoverage extends XmlCoverage {

    @XmlAttribute(name = "line-rate")
    public double lineRate;

    @XmlAttribute(name = "branch-rate")
    public double branchRate;

    // Complexity

    @XmlAttribute(name = "complexity")
    public double complexity = -1.0f;

    // Coverage Interface

    @Override
    public double getLineRate() {
        return lineRate;
    }

    @Override
    public double getBranchRate() {
        return branchRate;
    }

    // TODO: complexity

    // Constructor

    public CoberturaCoverage() {

    }

    // toString

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        builder.append(String.format("lineRate=%f, branchRate=%f", lineRate, branchRate));
        builder.append(" ]");
        return builder.toString();
    }
}