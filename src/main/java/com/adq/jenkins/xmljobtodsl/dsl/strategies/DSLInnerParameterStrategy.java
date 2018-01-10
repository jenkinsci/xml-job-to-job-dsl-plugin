package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLInnerParameterStrategy extends AbstractDSLStrategy implements IValueStrategy {

    public DSLInnerParameterStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    @Override
    public String toDSL() {
        return getChildrenDSL();
    }
}
