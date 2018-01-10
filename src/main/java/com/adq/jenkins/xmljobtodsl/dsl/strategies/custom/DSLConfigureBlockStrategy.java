package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.AbstractDSLStrategy;

import java.util.ArrayList;

public class DSLConfigureBlockStrategy extends AbstractDSLStrategy {

	private final String name;

	public DSLConfigureBlockStrategy(int tabs, PropertyDescriptor descriptor, String name) {
		super(tabs, descriptor, false);

		this.name = name;

		descriptor.getParent().getProperties().remove(descriptor);
		PropertyDescriptor configure = findConfigureBlock();
		configure.getProperties().add(descriptor);
		initChildren(configure);
	}

	private PropertyDescriptor findConfigureBlock() {
		PropertyDescriptor root = findRoot((PropertyDescriptor) getDescriptor());
		for (PropertyDescriptor descriptor : root.getProperties()) {
			if (descriptor.getName().equals("configure")) {
				return descriptor;
			}
		}
		PropertyDescriptor configure = new PropertyDescriptor("configure", null, new ArrayList<PropertyDescriptor>());
		root.getProperties().get(0).getProperties().add(configure);
		return configure;
	}

	private PropertyDescriptor findRoot(PropertyDescriptor descriptor) {
		if (descriptor.getParent() == null) {
			return descriptor;
		}
		return findRoot(descriptor.getParent());
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, getChildrenDSL()), getTabs());
	}
}
