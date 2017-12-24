package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLInnerStrategy extends AbstractDSLStrategy {

    public DSLInnerStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor, 0);
    }

    @Override
    public String toDSL() {
        return getChildrenDSL();
    }
}
