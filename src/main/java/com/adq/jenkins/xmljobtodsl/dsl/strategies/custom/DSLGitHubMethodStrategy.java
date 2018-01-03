package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;

import java.util.ArrayList;
import java.util.List;

public class DSLGitHubMethodStrategy extends DSLMethodStrategy {

	public DSLGitHubMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, null, methodName);

		List<PropertyDescriptor> list = new ArrayList<>();
		PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor("url", propertyDescriptor.getParent(), list);

		PropertyDescriptor url = new PropertyDescriptor("github.url", newPropertyDescriptor, propertyDescriptor.getValue());
		list.add(url);

		PropertyDescriptor method = new PropertyDescriptor("method", newPropertyDescriptor, getMethod(propertyDescriptor));
		list.add(method);

		setDescriptor(newPropertyDescriptor);
		initChildren(newPropertyDescriptor);
	}

	private String getMethod(PropertyDescriptor propertyDescriptor) {
		return propertyDescriptor.getValue().split(":")[0];
	}
}
