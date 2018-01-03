package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLValueStrategy extends AbstractDSLStrategy implements IValueStrategy {

    public DSLValueStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }


    @Override
    public String toDSL() {
        return printValueAccordingOfItsType(((PropertyDescriptor) getDescriptor()).getValue());
    }
}
