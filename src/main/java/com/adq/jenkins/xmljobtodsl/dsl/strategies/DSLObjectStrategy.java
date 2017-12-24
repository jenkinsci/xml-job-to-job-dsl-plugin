package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLObjectStrategy extends AbstractDSLStrategy {

    private final String name;

    public DSLObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(propertyDescriptor, tabs);
        this.name = name;
    }

    @Override
    public String toDSL() {
        if (name != null) {
            return String.format(getSyntaxProperties().getProperty("syntax.object_with_name"), name, getChildrenDSL());
        } else {
            return String.format(getSyntaxProperties().getProperty("syntax.object"), getChildrenDSL());
        }
    }
}
