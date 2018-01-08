package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.AbstractDSLStrategy;

public class DSLPropertyAsMethodParametersStrategy extends AbstractDSLStrategy {

	public DSLPropertyAsMethodParametersStrategy(int tabs, PropertyDescriptor descriptor, String name) {
		super(tabs, descriptor);
	}

	@Override
	public String toDSL() {
		String[] variables = ((PropertyDescriptor) getDescriptor()).getValue().split("(\\s+|\t)");
		StringBuilder methods = new StringBuilder();

		for (String var : variables) {
			String[] keyValue = var.split("=");
			methods.append(replaceTabs(
					String.format(getSyntax("syntax.method_call"), getProperty(
							(PropertyDescriptor) getDescriptor()).getValue(),
							printValueAccordingOfItsType(keyValue[0]) + ", " +
									printValueAccordingOfItsType(keyValue[1])), getTabs()));
		}
		return methods.toString();
	}
}
