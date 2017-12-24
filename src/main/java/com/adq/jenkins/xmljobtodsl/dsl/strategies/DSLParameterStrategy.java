package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLParameterStrategy extends AbstractDSLStrategy {

    private final PropertyDescriptor propertyDescriptor;

    public DSLParameterStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor, 0);
        this.propertyDescriptor = propertyDescriptor;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntaxProperties().getProperty("syntax.method_param"),
                printValueAccordingOfItsType(propertyDescriptor.getValue()));
    }
}
