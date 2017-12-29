package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLObjectStrategy extends AbstractDSLStrategy {

    private final String name;

    public DSLObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor);
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
