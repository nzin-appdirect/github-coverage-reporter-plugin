package io.jenkins.plugins.gcr;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;

public class PluginConfiguration extends AbstractDescribableImpl<PluginConfiguration> {

    @Extension
    public static final PluginConfigurationDescriptor DESCRIPTOR = new PluginConfigurationDescriptor();

    @DataBoundConstructor
    public PluginConfiguration() {
    }

    @Override
    public Descriptor<PluginConfiguration> getDescriptor() {
        return DESCRIPTOR;
    }

    public static final class PluginConfigurationDescriptor extends Descriptor<PluginConfiguration> {

        private String githubAccessToken;

        private String githubEnterpriseUrl;

        public PluginConfigurationDescriptor() {
            load();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            githubAccessToken = json.getString("githubAccessToken");
            githubEnterpriseUrl = json.getString("githubEnterpriseUrl");
            return super.configure(req, json);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            // TODO: localise
            return "GitHub Coverage Reporter";
        }

        public String getGithubAccessToken() {
            return githubAccessToken;
        }

        public String getGithubEnterpriseUrl() {
            return githubEnterpriseUrl;
        }

    }

}
