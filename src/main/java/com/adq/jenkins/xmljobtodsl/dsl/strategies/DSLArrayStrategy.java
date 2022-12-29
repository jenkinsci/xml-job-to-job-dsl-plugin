package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLArrayStrategy extends DSLMethodStrategy implements IValueStrategy {
	private String methodName;
	private String parentType;
	public DSLArrayStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, propertyDescriptor, methodName);
		this.methodName = methodName;
		this.setTabs(tabs);

		parentType = getType(propertyDescriptor.getParent());
	}

	@Override
	public String toDSL() {
		String childrenDSL = getChildrenDSL();
		// Assume if the parent is an object that this is likely supposed to be a method as opposed to a parameter
		if (parentType.equals("OBJECT")) {
			return replaceTabs(String.format(getSyntax("syntax.method_call"),
					methodName, String.format("[%s]", childrenDSL)), getTabs());
		} else {
			return replaceTabs(String.format(getSyntax("syntax.array"), getChildrenDSL()), getTabs());
		}
	}
}
