package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLObjectStrategy extends AbstractDSLStrategy {

    private final String name;

    public DSLObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, shouldInitChildren);
        this.name = name;
    }

    @Override
    public String toDSL() {
        String childrenDSL = getChildrenDSL();
        if (childrenDSL.isEmpty()) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}
