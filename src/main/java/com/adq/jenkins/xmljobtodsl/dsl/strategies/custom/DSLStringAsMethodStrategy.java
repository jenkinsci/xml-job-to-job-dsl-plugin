package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DSLStringAsMethodStrategy extends DSLObjectStrategy {

	public DSLStringAsMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs - 1, propertyDescriptor, name, false);

		List<Pair<String, String>> pairs = getProps(propertyDescriptor);

		List<PropertyDescriptor> children = new ArrayList<>();

		for (Pair<String, String> pair : pairs) {
			List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
			propertyDescriptors.add(new PropertyDescriptor("properties.key", propertyDescriptor.getParent(), pair.getKey()));
			propertyDescriptors.add(new PropertyDescriptor("properties.value", propertyDescriptor.getParent(), pair.getValue()));
			children.add(new PropertyDescriptor(name, propertyDescriptor.getParent(), propertyDescriptors));
		}

		initChildren(new PropertyDescriptor(propertyDescriptor.getName(), propertyDescriptor.getParent(), children));
	}

	private List<Pair<String, String>> getProps(PropertyDescriptor propertyDescriptor) {
		String[] values = propertyDescriptor.getValue().split("\\r?\\n");
		List<Pair<String, String>> pairs = new ArrayList<>();
		for (String value : values) {
			String[] pair = value.split("=");
			pairs.add(new Pair(pair[0], pair[1]));
		}
		return pairs;
	}

	@Override
	public String toDSL() {
		return getChildrenDSL();
	}
}
