package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLParameterStrategy extends AbstractDSLStrategy {

    private final PropertyDescriptor propertyDescriptor;

    public DSLParameterStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
        this.propertyDescriptor = propertyDescriptor;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.method_param"),
                printValueAccordingOfItsType(propertyDescriptor.getValue()));
    }
}
