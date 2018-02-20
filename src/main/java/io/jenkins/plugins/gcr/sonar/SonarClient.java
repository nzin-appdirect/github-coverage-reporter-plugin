package io.jenkins.plugins.gcr.sonar;

import io.jenkins.plugins.gcr.sonar.models.SonarProject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SonarClient {

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

    public List<SonarProject> listProjects() throws IOException {

        String url = fullUrl("api/components/search?qualifiers=TRK");

        HttpUriRequest request = new HttpGet(url);

        ResponseHandler<List<SonarProject>> responseHandler = (HttpResponse httpResponse) -> {
            HttpEntity entity = httpResponse.getEntity();
            String responseText = EntityUtils.toString(entity);

            JSONObject jsonObject = JSONObject.fromObject(responseText);
            JSONArray projectArray = jsonObject.getJSONArray("components");

            List<SonarProject> projects = new ArrayList<>();

            projectArray.forEach(item -> {
                JSONObject obj = (JSONObject)item;
                projects.add(new SonarProject(obj.getString("name")));
            });


            return projects;
        };

        List<SonarProject> projects= this.httpClient.execute(request, responseHandler);

        return projects;

    }

    // Utilities

    public String fullUrl(String path) {
        return String.format("http://%s:%s/%s", this.sonarHost, this.sonarPort, path);
    }

}
