package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLConstantStrategy extends AbstractDSLStrategy {

    private final String name;

    public DSLConstantStrategy(PropertyDescriptor propertyDescriptor, String name) {
        super(propertyDescriptor);
        this.name = name;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.string_variable"),
                name, printValueAccordingOfItsType(((PropertyDescriptor) getDescriptor()).getValue()));
    }
}
