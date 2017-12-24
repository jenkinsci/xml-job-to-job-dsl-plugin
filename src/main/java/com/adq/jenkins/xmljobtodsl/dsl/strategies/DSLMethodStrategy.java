package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLMethodStrategy extends AbstractDSLStrategy {

    private final String methodName;
    private final PropertyDescriptor propertyDescriptor;

    public DSLMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(propertyDescriptor, tabs);
        this.methodName = methodName;
        this.propertyDescriptor = propertyDescriptor;
    }

    @Override
    public String toDSL() {
        if (propertyDescriptor.getValue() != null) {
            return String.format(getSyntaxProperties().getProperty("syntax.method_call"),
                    methodName, printValueAccordingOfItsType(propertyDescriptor.getValue()));
        } else {
            return String.format(getSyntaxProperties().getProperty("syntax.method_call"),
                    methodName, getChildrenDSL());
        }
    }
}
