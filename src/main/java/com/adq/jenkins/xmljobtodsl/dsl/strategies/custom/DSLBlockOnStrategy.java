package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLBlockOnStrategy extends DSLMethodStrategy {

	private final String name;

	public DSLBlockOnStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs, propertyDescriptor, name);
		this.name = name;
	}

	@Override
	public String toDSL() {
		PropertyDescriptor useBuildBlockerDescriptor = getPropertyByItsName("useBuildBlocker");
		if (!Boolean.parseBoolean(useBuildBlockerDescriptor.getValue())) {
			return "";
		}

		PropertyDescriptor blockingJobs = getPropertyByItsName("blockingJobs");
		String parameters = String.format(getSyntax("syntax.method_param"),
				printValueAccordingOfItsType(blockingJobs.getValue()));

		PropertyDescriptor blockLevelDescriptor = getPropertyByItsName("blockLevel");
		String innerMethods = String.format(getSyntax("syntax.method_call"),
				getProperty(blockLevelDescriptor).getValue(),
				printValueAccordingOfItsType(blockLevelDescriptor.getValue()));

		PropertyDescriptor descriptorOfThirdItem = getPropertyByItsName("scanQueueFor");
		innerMethods += String.format(getSyntax("syntax.method_call"),
				getProperty(descriptorOfThirdItem).getValue(),
				printValueAccordingOfItsType(descriptorOfThirdItem.getValue()));

		innerMethods = replaceTabs(innerMethods, getTabs() + 1);
		parameters += String.format(getSyntax("syntax.object"), innerMethods);
		return replaceTabs(String.format(getSyntax("syntax.method_call"), name, parameters), getTabs());
	}

	private PropertyDescriptor getPropertyByItsName(String name) {
		for (PropertyDescriptor descriptor : getDescriptor().getProperties()){
			String propertyName = getPropertyByName(name);
			if (descriptor.getName().equals(propertyName)) {
				return descriptor;
			}
		}
		throw new RuntimeException(String.format("Couldn't find any descriptor with name: %s", name));
	}
}
