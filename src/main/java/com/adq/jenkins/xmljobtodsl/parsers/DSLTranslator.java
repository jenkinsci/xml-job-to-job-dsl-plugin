package com.adq.jenkins.xmljobtodsl.parsers;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLJobStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class DSLTranslator {

	private JobDescriptor[] jobDescriptors;
	private List<PropertyDescriptor> notTranslated = new ArrayList<>();

    public DSLTranslator(JobDescriptor[] jobDescriptors) throws IOException {
        this.jobDescriptors = jobDescriptors;
    }

    public DSLTranslator(JobDescriptor jobDescriptor) {
        this.jobDescriptors = new JobDescriptor[] { jobDescriptor };
    }

    public String toDSL() {
        StringBuilder builder = new StringBuilder();
        for (JobDescriptor job : jobDescriptors) {
            DSLJobStrategy jobStrategy = new DSLJobStrategy(job);
            builder.append(jobStrategy.toDSL());
            notTranslated.addAll(jobStrategy.getNotTranslatedList());
        }
        return builder.toString().trim();
    }

    public List<PropertyDescriptor> getNotTranslated() {
        return notTranslated;
    }
}
