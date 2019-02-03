package io.jenkins.plugins.gcr.github;

public class GithubResponse {

    private boolean success;

    private String message;

    public GithubResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
