package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;

import java.util.ArrayList;
import java.util.List;

public class DSLHiddenTagStrategy extends DSLObjectStrategy {

	public DSLHiddenTagStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs, propertyDescriptor, name, false);

		List<PropertyDescriptor> children = propertyDescriptor.getProperties();

		PropertyDescriptor child = new PropertyDescriptor(
				getPropertyByName(String.format("%s.hidden_tag", propertyDescriptor.getName())),
				propertyDescriptor, children);
		List<PropertyDescriptor> directChildren = new ArrayList<>();
		directChildren.add(child);

		PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor(propertyDescriptor.getName(), propertyDescriptor.getParent(),
				directChildren, propertyDescriptor.getAttributes());

		initChildren(newPropertyDescriptor);
	}
}
