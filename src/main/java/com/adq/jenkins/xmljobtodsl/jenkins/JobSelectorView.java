package com.adq.jenkins.xmljobtodsl.jenkins;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;

@Extension
public class JobSelectorView implements UnprotectedRootAction {

	@Override
	public String getIconFileName() {
		return "plugin.png";
	}

	@Override
	public String getDisplayName() {
		return "XML Job To DSL";
	}

	@Override
	public String getUrlName() {
		return "xmltodsl";
	}

	public JobCollector getData() {
		return new JobCollector();
	}
}
