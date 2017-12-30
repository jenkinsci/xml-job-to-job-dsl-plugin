package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.IDescriptor;

public class DSLArrayStrategy extends AbstractDSLStrategy {

    public DSLArrayStrategy(IDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.array"), getChildrenDSL());
    }
}
