package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLPropertyStrategy extends AbstractDSLStrategy {

    private String property;

    public DSLPropertyStrategy(int tabs, PropertyDescriptor propertyDescriptor, String property) {
        super(tabs, propertyDescriptor);
        this.property = property;
    }

    @Override
    public String toDSL() {
        return replaceTabs(String.format(getSyntax("syntax.property"), property,
                getChildrenDSL()), getTabs());
    }
}
