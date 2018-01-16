package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSLGitHubMethodStrategy extends DSLMethodStrategy {

	private static final Pattern pattern = Pattern.compile("(?:\\.com/?:?)([a-zA-Z0-9\\-_]+/[a-zA-Z0-9\\-_]+)");

	public DSLGitHubMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, null, methodName, false);

		List<PropertyDescriptor> list = new ArrayList<>();
		PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor("url", propertyDescriptor.getParent(), list);

		String method = getMethod(propertyDescriptor.getValue());
		String url = getRepositoryInformationFromUrl(propertyDescriptor.getValue());

		PropertyDescriptor urlProperty = new PropertyDescriptor("github.url", newPropertyDescriptor, url);
		list.add(urlProperty);

		PropertyDescriptor methodProperty = new PropertyDescriptor("method", newPropertyDescriptor, method);
		list.add(methodProperty);

		setDescriptor(newPropertyDescriptor);
		initChildren(newPropertyDescriptor);
	}

	public String getRepositoryInformationFromUrl(String url) {
		Matcher matcher = pattern.matcher(url);
		String repoInfo = url;
		if (matcher.find()) {
			repoInfo = matcher.group(1);
		}
		return repoInfo;
	}

	private String getMethod(String url) {
		if (url.startsWith("https")) {
			return "https";
		}
		if (url.startsWith("git@") || url.startsWith("ssh")) {
			return "ssh";
		}
		return "";
	}
}
