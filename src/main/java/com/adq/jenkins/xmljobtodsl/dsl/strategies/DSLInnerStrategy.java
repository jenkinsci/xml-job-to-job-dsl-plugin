package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLInnerStrategy extends AbstractDSLStrategy {

    public DSLInnerStrategy(int tabs, PropertyDescriptor propertyDescriptor) {
        super(tabs, propertyDescriptor);
    }

    @Override
    public String toDSL() {
        return getChildrenDSL();
    }
}
