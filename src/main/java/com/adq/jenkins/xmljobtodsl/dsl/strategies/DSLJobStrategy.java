package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.JobDescriptor;

public class DSLJobStrategy extends AbstractDSLStrategy {

    public static final String JOB_TYPE_JOB = "job";
    public static final String JOB_TYPE_PIPELINE = "pipelineJob";

    private final JobDescriptor jobDescriptor;

    public DSLJobStrategy(JobDescriptor jobDescriptor) {
        super(jobDescriptor);
        this.jobDescriptor = jobDescriptor;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.job"),
                JOB_TYPE_JOB, jobDescriptor.getName(), getChildrenDSL());
    }
}
