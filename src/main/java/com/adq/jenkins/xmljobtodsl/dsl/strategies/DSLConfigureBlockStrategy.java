package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLConfigureBlockStrategy extends AbstractDSLStrategy {

	private final String name;

	public DSLConfigureBlockStrategy(int tabs, PropertyDescriptor descriptor, String name) {
		super(tabs, descriptor);

		this.name = name;
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, getChildrenDSL()), getTabs());
	}
}
