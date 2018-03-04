package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.JacocoParser;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

public class ParserTests {

    @Test
    public void testParseCobertura() throws Exception {
        CoberturaParser parser = new CoberturaParser();

        File coverageFile = TestUtils.loadResource("cobertura-coverage.xml");
        Coverage coverage = parser.parse(coverageFile.getAbsolutePath());

        Assert.assertEquals(0.75, coverage.getLineRate(), 0.05);

        Assert.assertEquals(0.75, coverage.getBranchRate(), 0.05);

//        Assert.assertEquals(2.0, coverage.complexity, 0.05);
    }

    @Test
    public void testParseJacoco() throws Exception {
        JacocoParser parser = new JacocoParser();

        File coverageFile = TestUtils.loadResource("jacoco-coverage.xml");
        Coverage coverage = parser.parse(coverageFile.getAbsolutePath());

        Assert.assertEquals(0.22, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.28, coverage.getBranchRate(), 0.05);
    }

}
