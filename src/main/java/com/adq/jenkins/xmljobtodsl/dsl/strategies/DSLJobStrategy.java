package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.JobDescriptor;

public class DSLJobStrategy extends AbstractDSLStrategy {

    private final JobDescriptor jobDescriptor;

    public DSLJobStrategy(JobDescriptor jobDescriptor) {
        super(jobDescriptor);
        this.jobDescriptor = jobDescriptor;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.job"), getProperty(jobDescriptor.getProperties().get(0)).getValue()
                , jobDescriptor.getName(), getChildrenDSL());
    }
}
