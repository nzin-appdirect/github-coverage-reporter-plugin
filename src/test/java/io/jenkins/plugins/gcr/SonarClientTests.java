package io.jenkins.plugins.gcr;

import com.github.paweladamski.httpclientmock.HttpClientMock;
import com.google.common.io.Files;
import io.jenkins.plugins.gcr.sonar.SonarClient;
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

}
