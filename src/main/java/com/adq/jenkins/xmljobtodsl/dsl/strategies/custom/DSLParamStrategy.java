package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLParamStrategy extends DSLMethodStrategy {

	private final String methodName;

	public DSLParamStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, propertyDescriptor, methodName);
		this.methodName = methodName;
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.method_call"),
				methodName, getOrderedChildrenDSL()), getTabs());
	}

	public String getOrderedChildrenDSL() {
		PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
		if (propertyDescriptor.getName().equals("hudson.model.StringParameterDefinition")) {
			String defaultValue = getChildrenByName("defaultValue").toDSL();
			String name = getChildrenByName("name").toDSL();
			String description = getChildrenByName("description").toDSL();
			return name + ", " + defaultValue + ", " + description;

		} else {
			String variable = getChildrenByName("variable").toDSL();
			String credentialsId = getChildrenByName("credentialsId").toDSL();
			return variable + ", " + credentialsId;
		}
	}

	private DSLStrategy getChildrenByName(String name) {
		for (DSLStrategy strategy : getChildren()) {
			if (strategy.getDescriptor().getName().equals(name)) {
				return strategy;
			}
		}
		throw new RuntimeException(String.format("Child with name: %s not found", name));
	}
}
