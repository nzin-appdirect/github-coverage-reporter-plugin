package io.jenkins.plugins.gcr.sonar;

import java.io.IOException;

public class SonarException extends Exception {

    public SonarException(String message, IOException ex) {
        super(message, ex);
    }
}
