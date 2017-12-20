package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class Translator {

    private String variableDeclarations;
    private String objectDeclarations;
    private String closureDeclarations;
    private String jobDeclarations = "";
    private String viewDeclarations;

    private JobDescriptor[] jobDescriptors;

    private Properties syntaxProperties;
    private Properties translatorProperties;

    public Translator(JobDescriptor[] jobDescriptors) throws IOException {
        this.jobDescriptors = jobDescriptors;
        initProperties();
    }

    private void initProperties() throws IOException {
        InputStream in = getClass().getResourceAsStream("syntax.properties");
        syntaxProperties = new Properties();
        syntaxProperties.load(in);

        in = getClass().getResourceAsStream("translator.properties");
        translatorProperties = new Properties();
        translatorProperties.load(in);
    }

    public void toDSL() {
        for (JobDescriptor jobDescriptor : jobDescriptors) {
            jobDeclarations += String.format(syntaxProperties.getProperty("syntax.job"), "job", jobDescriptor.getName(),
                    getDSLofJobProperties(jobDescriptor.getProperties());
        }
    }

    private String getDSLofJobProperties(List<PropertyDescriptor> jobProperties) {
        return "";
    }
}
