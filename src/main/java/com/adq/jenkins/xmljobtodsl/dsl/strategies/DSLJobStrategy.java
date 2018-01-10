package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.ArrayList;

public class DSLJobStrategy extends AbstractDSLStrategy {

    public DSLJobStrategy(JobDescriptor jobDescriptor) {
        super(0, jobDescriptor);
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.job"), getProperty(getDescriptor().getProperties().get(0)).getValue()
                , getDescriptor().getName(), getChildrenDSL());
    }
}
