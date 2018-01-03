package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLValueStrategy extends AbstractDSLStrategy implements IValueStrategy {

    private final PropertyDescriptor propertyDescriptor;

    public DSLValueStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
        this.propertyDescriptor = propertyDescriptor;
    }


    @Override
    public String toDSL() {
        return printValueAccordingOfItsType(propertyDescriptor.getValue());
    }
}
