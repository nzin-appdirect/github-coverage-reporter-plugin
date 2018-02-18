package io.jenkins.plugins.gcr;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Result;
import hudson.tasks.*;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.CoverageType;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.CoverageParser;
import io.jenkins.plugins.gcr.parsers.ParserFactory;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundSetter;

public class GithubCoveragePublisher extends Recorder implements SimpleBuildStep {

    private final String filepath;

    private String coverageXmlType;

    @DataBoundConstructor
    public GithubCoveragePublisher(String filepath, String coverageXmlType) {
        this.filepath = filepath;
        this.coverageXmlType = coverageXmlType;
    }

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

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("Attempting to parse file of type, " + coverageXmlType + "");

        FilePath pathToFile = new FilePath(workspace, this.filepath);

        if (!pathToFile.exists()) {
            listener.error("CoberturaCoverage file does not exist");
            run.setResult(Result.FAILURE);
            return;
        } else {
            listener.getLogger().println(String.format("Found file '%s'", this.filepath));
            String xmlString = FileUtils.readFileToString(new File(pathToFile.toURI()));
            listener.getLogger().println("XML Content: \n");
            listener.getLogger().println(xmlString);
            listener.getLogger().println("\n");
        }

        File file = new File(pathToFile.toURI());
        CoverageParser parser = ParserFactory.instance.parserForType(coverageXmlType);

        try {
            listener.getLogger().println(String.format("Attempting parse of file: %s", file.getAbsolutePath()));
            Coverage cov = parser.parse(file.getAbsolutePath());

            listener.getLogger().println("XML Object: ");
            listener.getLogger().println(cov);

            run.addAction(new CoverageReportAction(cov));
            run.save();

            run.setResult(Result.SUCCESS);

        } catch (Exception ex) {
            listener.error("Failed to parse xml, received Exception: ");
            ex.printStackTrace();
            run.setResult(Result.FAILURE);
        }

    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public ListBoxModel doFillCoverageXmlTypeItems() {
            ListBoxModel model = new ListBoxModel();
            model.add("Cobertura XML", CoverageType.COBERTURA);
            model.add("Jacoco XML", CoverageType.JACOCO);
            return model;
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
