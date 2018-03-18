package io.jenkins.plugins.gcr.models;

public enum CoverageType {

    JACOCO("jacoco"),
    COBERTURA("cobertura"),
    SONARQUBE("sonarqube");

    private String identifier;

    CoverageType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static CoverageType fromIdentifier(String identifier) {
        if (JACOCO.getIdentifier().equals(identifier)) {
            return JACOCO;
        }
        if (COBERTURA.getIdentifier().equals(identifier)) {
            return COBERTURA;
        }
        if (SONARQUBE.getIdentifier().equals(identifier)) {
            return SONARQUBE;
        }
        return null;
    }
}
