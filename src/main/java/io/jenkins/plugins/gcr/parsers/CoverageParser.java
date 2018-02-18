package io.jenkins.plugins.gcr.parsers;

import io.jenkins.plugins.gcr.models.Coverage;

public interface CoverageParser {

    public Coverage parse(String filepath) throws Exception;

}
