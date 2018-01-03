package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLParameterStrategy extends AbstractDSLStrategy implements IValueStrategy {

    public DSLParameterStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    @Override
    public String toDSL() {
        return printValueAccordingOfItsType(((PropertyDescriptor) getDescriptor()).getValue());
    }
}
