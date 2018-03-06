package io.jenkins.plugins.gcr.github;

import net.sf.json.JSONObject;

public class GithubPayload {

    private String status;

    private String targetUrl;

    private String description;

    private String context;

    public GithubPayload(String status, String targetUrl, String description, String context) {
        this.status = status;
        this.targetUrl = targetUrl;
        this.description = description;
        this.context = context;
    }

    public String getStatus() {
        return status;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getContext() {
        return context;
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("state", getStatus());
        object.put("target_url", getTargetUrl());
        object.put("description", getDescription());
        object.put("context", getContext());
        return object;
    }

    public String toJSONString() {
        return toJSONObject().toString();
    }
}