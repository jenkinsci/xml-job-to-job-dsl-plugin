package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSLGitHubMethodStrategy extends DSLMethodStrategy {

	private static final Pattern pattern = Pattern.compile("\\w+\\:\\/\\/\\w+?\\.\\w+?\\.?\\w+?\\/(.+?)\\.\\w+");

	public DSLGitHubMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, null, methodName, false);

		List<PropertyDescriptor> list = new ArrayList<>();
		PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor("url", propertyDescriptor.getParent(), list);

		Matcher matcher = pattern.matcher(propertyDescriptor.getValue());
		String url = propertyDescriptor.getValue();
		if (matcher.find()) {
			url = matcher.group(1);
		}

		PropertyDescriptor urlProperty = new PropertyDescriptor("github.url", newPropertyDescriptor, url);
		list.add(urlProperty);

		PropertyDescriptor method = new PropertyDescriptor("method", newPropertyDescriptor, getMethod(propertyDescriptor));
		list.add(method);

		setDescriptor(newPropertyDescriptor);
		initChildren(newPropertyDescriptor);
	}

	private String getMethod(PropertyDescriptor propertyDescriptor) {
		return propertyDescriptor.getValue().split(":")[0];
	}
}
