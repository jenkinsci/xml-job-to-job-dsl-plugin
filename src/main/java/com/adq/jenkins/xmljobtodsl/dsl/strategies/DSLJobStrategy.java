package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.JobDescriptor;

public class DSLJobStrategy extends AbstractDSLStrategy {

    public DSLJobStrategy(JobDescriptor jobDescriptor) {
        super(jobDescriptor);
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.job"), getProperty(getDescriptor().getProperties().get(0)).getValue()
                , getDescriptor().getName(), getChildrenDSL());
    }
}
