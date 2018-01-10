package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;

public class DSLChoiceParamStrategy extends DSLMethodStrategy {

	private final String methodName;

	public DSLChoiceParamStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, propertyDescriptor, methodName);
		this.methodName = methodName;
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.method_call"),
				methodName, getOrderedChildrenDSL()), getTabs());
	}

	public String getOrderedChildrenDSL() {
		String choices = getChildrenByName("choices").toDSL();
		String name = getChildrenByName("name").toDSL();
		String description = getChildrenByName("description").toDSL();
		return name + ", " + choices + ", " + description;
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
