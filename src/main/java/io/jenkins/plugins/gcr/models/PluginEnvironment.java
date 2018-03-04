package io.jenkins.plugins.gcr.models;

import hudson.EnvVars;
import hudson.model.Run;

public class PluginEnvironment {

    private String pullRequestId;

    private String pullRequestRepository;

    private String gitUrl;

    private String gitHash;

    private String buildUrl;

    // Constructor

    public PluginEnvironment(EnvVars env) throws IllegalArgumentException {
        pullRequestId = get("ghprbPullId", env);
        pullRequestRepository = get("ghprbGhRepository", env);
        gitUrl = get("ghprbAuthorRepoGitUrl", env);
        gitHash = get("ghprbActualCommit", env);
        buildUrl = get("BUILD_URL", env);
    }

    // Getters / Setters

    public String getPullRequestId() {
        return pullRequestId;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public String getGitHash() {
        return gitHash;
    }

    public String getPullRequestRepository() {
        return pullRequestRepository;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    // Helpers

    private String get(String key, EnvVars env) throws IllegalArgumentException {
        if (env.containsKey(key)) {
            return env.get(key);
        } else {
            // TODO: localize
            throw new IllegalArgumentException(String.format("Failed to get required environmental variable '%s'", key));
        }
    }

}
