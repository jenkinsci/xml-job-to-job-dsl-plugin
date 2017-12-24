package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLPropertyStrategy extends AbstractDSLStrategy {

    private String property;

    public DSLPropertyStrategy(int tabs, PropertyDescriptor propertyDescriptor, String property) {
        super(propertyDescriptor, tabs);
        this.property = property;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntaxProperties().getProperty("syntax.property"), property,
                getChildrenDSL());
    }
}
