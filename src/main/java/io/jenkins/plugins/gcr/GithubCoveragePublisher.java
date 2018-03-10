package io.jenkins.plugins.gcr;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.gcr.github.GithubClient;
import io.jenkins.plugins.gcr.github.GithubPayload;
import io.jenkins.plugins.gcr.models.*;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.CoverageParser;
import io.jenkins.plugins.gcr.parsers.ParserException;
import io.jenkins.plugins.gcr.parsers.ParserFactory;
import io.jenkins.plugins.gcr.sonar.SonarClient;
import io.jenkins.plugins.gcr.sonar.SonarException;
import io.jenkins.plugins.gcr.sonar.models.SonarProject;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class GithubCoveragePublisher extends Recorder implements SimpleBuildStep {

    public static final int COMPARISON_SONAR = 0;
    public static final int COMPARISON_FIXED = 1;

    private final String filepath;

    private String coverageXmlType;

    private ComparisonOption comparisonOption;

    @DataBoundConstructor
    public GithubCoveragePublisher(String filepath, String coverageXmlType, ComparisonOption comparisonOption) throws IOException {
        this.filepath = filepath;
        this.coverageXmlType = coverageXmlType;
        this.comparisonOption = comparisonOption;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    // Getters / Setters

    public String getFilepath() {
        return filepath;
    }

    public String getCoverageXmlType() {
        return coverageXmlType;
    }

    @DataBoundSetter
    public void setCoverageXmlType(String coverageXmlType) {
        this.coverageXmlType = coverageXmlType;
    }

    public ComparisonOption getComparisonOption() {
        return comparisonOption;
    }

    @DataBoundSetter
    public void setComparisonOption(ComparisonOption comparisonOption) {
        this.comparisonOption = comparisonOption;
    }

    public String getSonarProject() {
        return this.comparisonOption.getSonarProject();
    }

    // Runner

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("Attempting to parse file of type, " + coverageXmlType + "");

        PluginEnvironment environment = new PluginEnvironment(run.getEnvironment(listener));
        String githubAccessToken = PluginConfiguration.DESCRIPTOR.getGithubAccessToken();
        String githubUrl = PluginConfiguration.DESCRIPTOR.getGithubEnterpriseUrl();
        GithubClient githubClient = new GithubClient(environment, githubUrl, githubAccessToken);

        FilePath pathToFile = new FilePath(workspace, this.filepath);

        if (!pathToFile.exists()) {
            listener.error("The coverage file at the provided path does not exist");
            run.setResult(Result.FAILURE);
            return;
        } else {
            listener.getLogger().println(String.format("Found file '%s'", this.filepath));
            String xmlString = FileUtils.readFileToString(new File(pathToFile.toURI()));
        }

        File file = new File(pathToFile.toURI());
        listener.getLogger().println(String.format("Attempting parse of file: %s", file.getAbsolutePath()));

        try {
            CoverageReportAction coverageReport = generateCoverageReport(file);
            run.addAction(coverageReport);
            run.save();

            GithubPayload payload = generateGithubCovergePayload(coverageReport, environment.getBuildUrl());
            githubClient.sendCommitStatus(payload);

            run.setResult(Result.SUCCESS);
        } catch (Exception ex) {
            listener.error(ex.getMessage());
            ex.printStackTrace();
            run.setResult(Result.FAILURE);
        }

    }

    private CoverageReportAction generateCoverageReport(File file) throws ParserException, SonarException {
        CoverageParser parser = ParserFactory.instance.parserForType(coverageXmlType);

        Coverage coverage = parser.parse(file.getAbsolutePath());
        Coverage expectedCoverage = getExpectedCoverage(comparisonOption);

        return new CoverageReportAction(coverage, expectedCoverage);
    }


    private Coverage getExpectedCoverage(ComparisonOption comparisonOption) throws SonarException {
        Coverage expectedCoverage;
        if (comparisonOption.isTypeSonarProject()) {
            SonarClient client = new SonarClient();
            expectedCoverage = client.getCoverageForProject(comparisonOption.getSonarProject());
        } else if (comparisonOption.isTypeFixedCoverage()) {
            double fixedValue = comparisonOption.fixedCoverageAsDouble();
            // TODO: Have two separate inputs for this
            expectedCoverage = new DefaultCoverage(fixedValue, fixedValue);
        } else {
            expectedCoverage = null;
        }
        return expectedCoverage;
    }

    private GithubPayload generateGithubCovergePayload(CoverageReportAction coverageReport, String targetUrl) {
        String status = coverageReport.isAcceptableCoverage() ? "success" : "failure";
        String description = coverageReport.getStatusDescription();
        String context = "coverage";
        GithubPayload payload = new GithubPayload(status, targetUrl, description, context);

        return payload;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {


        private ListBoxModel sonarProjectModel;

        public ListBoxModel doFillCoverageXmlTypeItems() {
            ListBoxModel model = new ListBoxModel();
            model.add("Cobertura XML", CoverageType.COBERTURA);
            model.add("Jacoco XML", CoverageType.JACOCO);
            return model;
        }

        public ListBoxModel doFillSonarProjectItems() {
            sonarProjectModel = new ListBoxModel();

            SonarClient client = new SonarClient();
            try {
                List<SonarProject> projects = client.listProjects();
                projects.forEach(project -> sonarProjectModel.add(project.getName(), project.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sonarProjectModel;
        }

        public FormValidation doCheckSonarProject(@QueryParameter String value) {
            // TODO: Use localized Messages strings
            if (sonarProjectModel == null || sonarProjectModel.isEmpty()) {
                return FormValidation.error("SonarQube server unreachable.");
            }
            if (value == null || value.equals("")) {
                return FormValidation.error("Invalid project selection. check that your SonarQube server is not unreachable.");
            }

            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.GithubCoveragePublisher_DescriptorImpl_DisplayName();
        }

    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
}
