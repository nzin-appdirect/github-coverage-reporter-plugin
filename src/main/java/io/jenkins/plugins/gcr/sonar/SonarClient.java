package io.jenkins.plugins.gcr.sonar;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.DefaultCoverage;
import io.jenkins.plugins.gcr.sonar.models.SonarProject;
import io.jenkins.plugins.gcr.sonar.parsers.SonarCoverageParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SonarClient {

    private static final String METRIC_KEY = "line_coverage,branch_coverage,coverage";

    private HttpClient httpClient;

    private String sonarHost;

    private int sonarPort;

    public SonarClient() {
        this(HttpClientBuilder.create().build(),"localhost");
    }

    public SonarClient(HttpClient client) {
        this(client, "localhost");
    }

    public SonarClient(HttpClient client, String sonarHost) {
        this(client, sonarHost, 9000);
    }

    public SonarClient(HttpClient client, String sonarHost, int sonarPort) {
        this.sonarHost = sonarHost;
        this.sonarPort = sonarPort;
        this.httpClient = client;
    }

    // Requests

    /**
     * Fetches a list of sonar projects
     *
     * @return a list of sonar project metadata models
     * @throws IOException
     */
    public List<SonarProject> listProjects() throws IOException {

        String url = fullUrl("api/components/search?qualifiers=TRK");
        HttpUriRequest request = new HttpGet(url);

        ResponseHandler<List<SonarProject>> responseHandler = (HttpResponse httpResponse) -> {
            String responseText = EntityUtils.toString(httpResponse.getEntity());

            JSONObject jsonObject = JSONObject.fromObject(responseText);
            JSONArray projectArray = jsonObject.getJSONArray("components");

            List<SonarProject> projects = new ArrayList<>();

            projectArray.forEach(item -> {
                JSONObject obj = (JSONObject)item;
                SonarProject project = new SonarProject(
                        obj.getString("name"),
                        obj.getString("key")
                );
                projects.add(project);
            });


            return projects;
        };

        List<SonarProject> projects = this.httpClient.execute(request, responseHandler);

        return projects;

    }

    /**
     * Fetches the coverage statistics for a given sonar project
     *
     * @param projectKey The key identifier for the sonar project
     * @return A coverage model
     * @throws SonarException
     */
    public Coverage getCoverageForProject(String projectKey) throws SonarException {
        // TODO: Add support for multi-component projects?
        String url = fullUrl(String.format("api/measures/component?component=%s&metricKeys=%s", projectKey, METRIC_KEY));
        HttpUriRequest request = new HttpGet(url);

        ResponseHandler<Coverage> responseHandler = (HttpResponse httpResponse) -> {
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            final JSONObject jsonResponse = JSONObject.fromObject(responseText);

            SonarCoverageParser parser = new SonarCoverageParser();
            return parser.parse(jsonResponse);
        };

        try {
            Coverage result = this.httpClient.execute(request, responseHandler);
            // TODO: Branch rate doesn't come back for some projects ...
            return result;
        } catch (IOException ex) {
            // TODO: Localise
            String message = String.format("Failed to retrieve coverage from sonar project '%s'", projectKey);
            throw new SonarException(message, ex);
        }
    }

    // Utilities

    private String fullUrl(String path) {
        return String.format("http://%s:%s/%s", this.sonarHost, this.sonarPort, path);
    }

}
