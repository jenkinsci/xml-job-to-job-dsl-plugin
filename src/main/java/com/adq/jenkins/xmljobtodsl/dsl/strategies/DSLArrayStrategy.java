package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

public class DSLArrayStrategy extends DSLMethodStrategy implements IValueStrategy {

    public DSLArrayStrategy(PropertyDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.array"), getChildrenDSL());
    }
}
