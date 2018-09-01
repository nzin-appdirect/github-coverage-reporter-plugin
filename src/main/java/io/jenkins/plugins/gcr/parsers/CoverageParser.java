package io.jenkins.plugins.gcr.parsers;

import hudson.FilePath;
import io.jenkins.plugins.gcr.models.Coverage;

public interface CoverageParser {

    public Coverage parse(FilePath filepath) throws ParserException;

}
