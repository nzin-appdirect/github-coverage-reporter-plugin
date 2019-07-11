/*
 * The MIT License
 *
 * Copyright 2015 CloudBees Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jenkins.plugins.gcr.workflow;

import io.jenkins.plugins.gcr.GithubCoveragePublisher;
import hudson.Extension;
import javax.annotation.CheckForNull;

import io.jenkins.plugins.gcr.models.ComparisonOption;
import io.jenkins.plugins.gcr.workflow.PublishCoverageStepExecution;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Publishes HTML reports in Workflows.
 * @author Oleg Nenashev
 */
public class PublishCoverageStep extends AbstractStepImpl {

	private String filepath;

	private String coverageXmlType;

	private String coverageRateType;

	private ComparisonOption comparisonOption;

	@DataBoundConstructor
	public PublishCoverageStep(String filepath, String coverageXmlType, String coverageRateType, ComparisonOption comparisonOption) throws IOException {
		this.filepath = filepath;
		this.coverageXmlType = coverageXmlType;
		this.coverageRateType = coverageRateType;
		this.comparisonOption = comparisonOption;
	}

	public String getFilePath() {
		return filepath;
	}
	public String getCoverageXmlType() {
		return coverageXmlType;
	}
	public String getCoverageRateType() {
		return coverageRateType;
	}
	public ComparisonOption getComparisonOption() {
		return comparisonOption;
	}


	@Extension
	public static class DescriptorImpl extends AbstractStepDescriptorImpl {

		public DescriptorImpl() {
			super(PublishCoverageStepExecution.class);
		}

		@Override
		public String getFunctionName() {
			return "publishCoverageGithub";
		}

		@Override
		public String getDisplayName() {
			return "Publish Coverage reports to Github";
		}
	}
}
