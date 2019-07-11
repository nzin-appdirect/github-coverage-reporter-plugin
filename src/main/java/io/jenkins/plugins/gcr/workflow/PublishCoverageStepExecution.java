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

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import hudson.EnvVars;
import hudson.model.BuildListener;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import hudson.AbortException;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.gcr.GithubCoveragePublisher;

/**
 * Execution for {@link PublishCoverageStep}.
 * @author Nicolas Zin
 */
public class PublishCoverageStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
	private static final long serialVersionUID = 1L;

	@StepContextParameter
	private transient TaskListener listener;

	@StepContextParameter
	private transient FilePath ws;

	@StepContextParameter
	private transient Run build;

	@StepContextParameter
	private transient Launcher launcher;

	@Inject
	private transient PublishCoverageStep step;

	@Override
	protected Void run() throws Exception {
		StepContext context = getContext();
		EnvVars env = context.get(EnvVars.class);

		boolean res = GithubCoveragePublisher.publishCoverage(build, ws, listener, env, step.getFilePath(), step.getCoverageXmlType(), step.getComparisonOption(), step.getCoverageRateType());
		if (!res) {
			throw new AbortException("Cannot publish coverage report");
		}
		return null;
	}

}
