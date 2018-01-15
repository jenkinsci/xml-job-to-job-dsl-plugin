package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLBlockOnStrategy extends DSLObjectStrategy {

	public DSLBlockOnStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
		super(tabs, propertyDescriptor, name, true);
	}

	@Override
	public String toDSL() {
		for (PropertyDescriptor descriptor : getDescriptor().getProperties()) {
			if (descriptor.getName().equals(getPropertyByName("useBuildBlocker"))) {
				if (!Boolean.parseBoolean(descriptor.getValue())) {
					return "";
				}
				break;
			}
		}
		return super.toDSL();
	}
}
