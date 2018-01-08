package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;

public class DSLMandatoryStringStrategy extends DSLMethodStrategy {

	private final String methodName;

	public DSLMandatoryStringStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, propertyDescriptor, methodName);
		this.methodName = methodName;
	}

	@Override
	public String toDSL() {
		PropertyDescriptor descriptor = (PropertyDescriptor) getDescriptor();
		String value = descriptor.getValue();
		if (value == null || value.trim().equals("")) {
			return replaceTabs(String.format(getSyntax("syntax.method_call"),
					methodName, "\"\""), getTabs());
		}
		return super.toDSL();
	}
}
