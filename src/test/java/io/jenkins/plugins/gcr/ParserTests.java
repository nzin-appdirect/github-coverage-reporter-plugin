package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.JacocoParser;
import io.jenkins.plugins.gcr.parsers.ParserException;
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
        Assert.assertEquals(0.75, coverage.getOverallRate(), 0.05);
    }

    @Test
    public void testParseCoberturaFailure() throws Exception {
        CoberturaParser parser = new CoberturaParser();

        File coverageFile = TestUtils.loadResource("cobertura-coverage-corrupted.xml");
        try {
            Coverage coverage = parser.parse(coverageFile.getAbsolutePath());
            Assert.fail();
        } catch(ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }

    @Test
    public void testParseJacoco() throws Exception {
        JacocoParser parser = new JacocoParser();

        File coverageFile = TestUtils.loadResource("jacoco-coverage.xml");
        Coverage coverage = parser.parse(coverageFile.getAbsolutePath());

        Assert.assertEquals(0.22, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.28, coverage.getBranchRate(), 0.05);
        Assert.assertEquals(0.25, coverage.getOverallRate(), 0.05);
    }

    @Test
    public void testParseJacocoFailure() throws Exception {
        JacocoParser parser = new JacocoParser();

        File coverageFile = TestUtils.loadResource("jacoco-coverage-corrupted.xml");
        try {
            Coverage coverage = parser.parse(coverageFile.getAbsolutePath());
            Assert.fail();
        } catch(ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }

}
