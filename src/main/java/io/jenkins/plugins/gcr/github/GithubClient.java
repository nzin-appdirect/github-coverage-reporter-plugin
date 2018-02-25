package io.jenkins.plugins.gcr.github;

import io.jenkins.plugins.gcr.models.PluginEnvironment;
import net.sf.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URL;

public class GithubClient {

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
            object.put("status", getStatus());
            object.put("targetUrl", getTargetUrl());
            object.put("description", getDescription());
            object.put("context", getContext());
            return object;
        }

        public String toJSONString() {
            return toJSONObject().toString();
        }
    }

    private PluginEnvironment environment;

    private HttpClient httpClient;

    public GithubClient(PluginEnvironment environment) {
//        final Settings settingsRepository = ServiceRegistry.getSettingsRepository();
//        final String apiUrl = settingsRepository.getGitHubApiUrl();
//        final String personalAccessToken = settingsRepository.getPersonalAccessToken();

        this.httpClient = HttpClientBuilder.create().build();
        this.environment = environment;
    }


    public void sendCommitStatus(GithubPayload githubPayload) throws Exception {

        URL gitUrl = new URL(environment.getGitUrl());
        String gitHost = gitUrl.getHost();

        URL gitApiUrl = new URL(gitHost.concat("/api/v3/repos/:owner/:repo/statuses/:sha"));

        HttpPost postRequest = new HttpPost();
        StringEntity entity = new StringEntity(githubPayload.toJSONString());
        postRequest.setEntity(entity);

        postRequest.setEntity(entity);
    }

}
