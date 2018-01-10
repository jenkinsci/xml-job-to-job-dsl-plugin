package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.IDescriptor;
import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.AbstractDSLStrategy;

public class DSLNonSupportedMethodDSL extends AbstractDSLStrategy {

	private final String name;

	public DSLNonSupportedMethodDSL(int tabs, PropertyDescriptor descriptor, String name) {
		super(tabs, descriptor);
		this.name = name;
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.method_call_non_supported"), name,
				((PropertyDescriptor) getDescriptor()).getValue()), getTabs());
	}
}
