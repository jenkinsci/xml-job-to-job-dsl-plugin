package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLJobStrategy;

import java.io.IOException;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class DSLTranslator {

	private JobDescriptor[] jobDescriptors;

    public DSLTranslator(JobDescriptor[] jobDescriptors) throws IOException {
        this.jobDescriptors = jobDescriptors;
    }

    public DSLTranslator(JobDescriptor jobDescriptor) {
        this.jobDescriptors = new JobDescriptor[] { jobDescriptor };
    }

    public String toDSL() {
        StringBuilder builder = new StringBuilder();
        for (JobDescriptor job : jobDescriptors) {
            builder.append(new DSLJobStrategy(job).toDSL());
        }
        return builder.toString().trim();
    }
}
