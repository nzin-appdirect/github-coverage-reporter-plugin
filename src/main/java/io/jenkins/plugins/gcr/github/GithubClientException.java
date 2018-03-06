package io.jenkins.plugins.gcr.github;

public class GithubClientException extends Exception {

    public GithubClientException(String message) {
        super(message);
    }

    public GithubClientException(String message, Exception ex) {
        super(message, ex);
    }

}
