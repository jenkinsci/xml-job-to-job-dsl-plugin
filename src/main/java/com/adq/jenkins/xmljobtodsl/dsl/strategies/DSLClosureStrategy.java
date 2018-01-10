package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLClosureStrategy extends AbstractDSLStrategy {

	private String name;
	private String parameter;

	public DSLClosureStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs, propertyDescriptor);
		this.name = name;
		parameter = propertyDescriptor.getProperties().get(0).getValue();
		propertyDescriptor.getProperties().remove(0);
		initChildren(propertyDescriptor);
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.method_call_closure"),
				name, printValueAccordingOfItsType(parameter), getChildrenDSL()), getTabs());
	}
}
