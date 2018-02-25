package io.jenkins.plugins.gcr.models;

import hudson.EnvVars;
import hudson.model.Run;

public class PluginEnvironment {

    private String pullRequestId;

    private String gitUrl;

    // Constructor

    public PluginEnvironment(EnvVars env) throws IllegalArgumentException {
        pullRequestId = get("ghprbPullId", env);
        gitUrl = get("GIT_URL", env);
    }

    // Getters / Setters

    public String getPullRequestId() {
        return pullRequestId;
    }

    public String getGitUrl() {
        return gitUrl;
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
