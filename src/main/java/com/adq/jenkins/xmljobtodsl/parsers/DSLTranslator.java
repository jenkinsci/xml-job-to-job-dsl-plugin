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
	private String viewName;
	private List<PropertyDescriptor> notTranslated = new ArrayList<>();

    public DSLTranslator(JobDescriptor[] jobDescriptors, String viewName) throws IOException {
        this.jobDescriptors = jobDescriptors;
        this.viewName = viewName;
    }

    public DSLTranslator(JobDescriptor jobDescriptor, String viewName) throws IOException {
        this(new JobDescriptor[] { jobDescriptor }, viewName);
    }

    public DSLTranslator(JobDescriptor[] jobDescriptors) throws IOException {
        this(jobDescriptors, null);
    }

    public DSLTranslator(JobDescriptor jobDescriptor) throws IOException {
        this(jobDescriptor, null);
    }

    public String toDSL() {
        StringBuilder builder = new StringBuilder();
        for (JobDescriptor job : jobDescriptors) {
            DSLJobStrategy jobStrategy = new DSLJobStrategy(job);
            builder.append(jobStrategy.toDSL());
            notTranslated.addAll(jobStrategy.getNotTranslatedList());
        }
        if (viewName != null) {
            builder.append(new DSLView(viewName, jobDescriptors).generateViewDSL());
        }
        return builder.toString().trim();
    }

    public List<PropertyDescriptor> getNotTranslated() {
        return notTranslated;
    }
}
