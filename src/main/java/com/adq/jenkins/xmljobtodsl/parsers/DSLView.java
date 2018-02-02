package com.adq.jenkins.xmljobtodsl.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DSLView {

	private static final String[] columns = new String[]{
		"status",
		"weather",
		"name",
		"lastSuccess",
		"lastFailure",
		"lastDuration",
		"buildButton"
	};

	private final String[] jobNames;
	private final String name;
	private Properties syntaxProperties;

	public DSLView(String name, String[] jobNames) {
		this.jobNames = jobNames.clone();
		this.name = name;
		init();
	}

	public DSLView(String name, JobDescriptor[] jobs) {
		String[] names = new String[jobs.length];
		for (int i = 0; i < jobs.length; i++) {
			names[i] = jobs[i].getName();
		}
		this.jobNames = names;
		this.name = name;
		init();
	}

	private void init() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("syntax.properties");
		syntaxProperties = new Properties();
		try {
			syntaxProperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generateViewDSL() {
		StringBuilder jobsBuilder = new StringBuilder();
		for (String name : jobNames) {
			jobsBuilder.append(String.format(syntaxProperties.getProperty("syntax.view.job"), name));
		}
		StringBuilder columnsBuilder = new StringBuilder();
		for (String column : columns) {
			columnsBuilder.append(String.format(syntaxProperties.getProperty("syntax.view.column"), column));
		}
		return String.format(syntaxProperties.getProperty("syntax.view"), name,
				jobsBuilder.toString(), columnsBuilder.toString());
	}
}
