package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class DSLRichTextPublisherStrategy extends DSLObjectStrategy {

	public DSLRichTextPublisherStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs, propertyDescriptor, name, false);

		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "stableText")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("stableText", propertyDescriptor, ""));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "unstableText")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("unstableText", propertyDescriptor, ""));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "failedText")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("failedText", propertyDescriptor, ""));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "abortedText")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("abortedText", propertyDescriptor, ""));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "abortedAsStable")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("abortedAsStable", propertyDescriptor, "true"));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "unstableAsStable")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("unstableAsStable", propertyDescriptor, "true"));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "failedAsStable")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("failedAsStable", propertyDescriptor, "true"));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "parserName")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("parserName", propertyDescriptor, "HTML"));
		}
		if (!hasPropertyWithName(propertyDescriptor.getProperties(), "nullAction")) {
			propertyDescriptor.getProperties().add(new PropertyDescriptor("nullAction", propertyDescriptor, ""));
		}

		initChildren(propertyDescriptor);
	}

	private boolean hasPropertyWithName(List<PropertyDescriptor> propertyDescriptorList, String name) {
		for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
			if (propertyDescriptor.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
