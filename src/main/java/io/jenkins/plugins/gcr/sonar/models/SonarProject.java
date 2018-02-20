package io.jenkins.plugins.gcr.sonar.models;

public class SonarProject {

    private String name;

    public SonarProject(String name) {
        this.name = name;
    }

    // Getters / Setters

    public String getName() {
        return name;
    }
}
