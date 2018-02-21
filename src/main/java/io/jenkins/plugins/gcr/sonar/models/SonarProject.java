package io.jenkins.plugins.gcr.sonar.models;

public class SonarProject {

    private String name;

    private String key;

    public SonarProject(String name, String key) {
        this.name = name;
        this.key = key;
    }

    // Getters / Setters

    public String getName() {
        return name;
    }

    public String getKey() { return key; }

}
