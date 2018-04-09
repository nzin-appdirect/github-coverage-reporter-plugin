package io.jenkins.plugins.gcr.models;

import java.util.Arrays;
import java.util.stream.Stream;

public enum CoverageRateType {
    LINE("Line"),
    BRANCH("Branch"),
    OVERALL("Overall");

    private String name;

    CoverageRateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CoverageRateType fromName(String name) {
        Stream<CoverageRateType> stream = Arrays.stream(CoverageRateType.values());
        return stream.filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }
}
