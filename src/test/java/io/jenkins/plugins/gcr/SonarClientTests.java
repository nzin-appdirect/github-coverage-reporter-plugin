package io.jenkins.plugins.gcr;

import com.github.paweladamski.httpclientmock.HttpClientMock;
import com.google.common.io.Files;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.sonar.SonarClient;
import io.jenkins.plugins.gcr.sonar.SonarException;
import io.jenkins.plugins.gcr.sonar.models.SonarProject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class SonarClientTests {

    @Test
    public void testSonarClientListProjects() throws Exception {

        HttpClientMock clientMock = new HttpClientMock();
        File mockFile = TestUtils.loadResource("sonar-projects.json");
        clientMock.onGet("http://localhost:9000/api/components/search")
                .withParameter("qualifiers", "TRK")
                .doReturn(Files.toString(mockFile, Charset.forName("utf-8")));


        SonarClient client = new SonarClient(clientMock);

        List<SonarProject> projects = client.listProjects();

        Assert.assertEquals(projects.get(0).getName(), "Test name");
    }

    @Test
    public void testGetCoverageForProject() throws Exception {
        HttpClientMock clientMock = new HttpClientMock();
        File mockFile = TestUtils.loadResource("sonar-metrics.json");
        clientMock.onGet("http://localhost:9000/api/measures/component")
                .withParameter("component", "io.jenkins.plugins:github-coverage-reporter")
                .withParameter("metricKeys", "line_coverage,branch_coverage,coverage")
                .doReturn(Files.toString(mockFile, Charset.forName("utf-8")));

        SonarClient client = new SonarClient(clientMock);

        Coverage coverage = client.getCoverageForProject("io.jenkins.plugins:github-coverage-reporter");

        Assert.assertEquals(0.3, coverage.getLineRate(), 0.05);

        Assert.assertEquals(0.1, coverage.getBranchRate(), 0.05);

        // TODO: overall coverage
    }

    @Test
    public void testGetCoverageForProjectIOException() {
        HttpClientMock clientMock = new HttpClientMock();
        clientMock.onGet("http://localhost:9000/api/measures/component")
                .withParameter("component", "io.jenkins.plugins:github-coverage-reporter")
                .withParameter("metricKeys", "line_coverage,branch_coverage,coverage")
                .doThrowException(new IOException());

        SonarClient client = new SonarClient(clientMock);

        try {
            Coverage coverage = client.getCoverageForProject("io.jenkins.plugins:github-coverage-reporter");
            Assert.fail();
        } catch (SonarException ex) {
            Assert.assertNotNull(ex);
        }
    }

}
