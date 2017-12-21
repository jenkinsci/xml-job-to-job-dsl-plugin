package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class Translator {

    private static final String PREFIX_XML = "xml.";
    private static final String PREFIX_GROOVY = "groovy.";
    private static final String SUFIX_GROOVY_TYPE = ".type";
    private static final String TYPE_CONST = "CONST";
    private static final String TYPE_INNER = "INNER";

	private List<String> variableDeclarations = new ArrayList<>();
    private List<String> objectDeclarations = new ArrayList<>();
    private List<String> closureDeclarations = new ArrayList<>();
    private List<String> jobDeclarations = new ArrayList<>();
    private List<String> viewDeclarations = new ArrayList<>();

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
            jobDeclarations.add(
                String.format(syntaxProperties.getProperty("syntax.job"), "job", jobDescriptor.getName(),
                getDSLofJobProperties(jobDescriptor.getProperties())));
        }
    }

    public List<String> getDSLofJobProperties(List<PropertyDescriptor> jobProperties) {
        List<String> methodsInsideOfJob = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : jobProperties) {
            methodsInsideOfJob.addAll(proccessPropertyDescriptor(propertyDescriptor));
        }
        return methodsInsideOfJob;
    }

    public List<String> proccessPropertyDescriptor(List<String> methodsInsideOfJob, PropertyDescriptor propertyDescriptor) {
        String key = String.format("%s%s", PREFIX_GROOVY, propertyDescriptor.getName());
        String property = translatorProperties.getProperty(key);
        String type = String.format("%s%s", key, SUFIX_GROOVY_TYPE);
        
        List<String> methods = new ArrayList<>();
        if (translatorProperties.containsKey(type)) {
            if (TYPE_CONST.equals(type)) {
                insertVariable(propertyDescriptor, property);
            }
        }

        if (TYPE_INNER.equals(property)) {
            methods.add(convertListInString(getDSLofJobProperties(propertyDescriptor.getProperties())));
        }

        if (translatorProperties.contains(property)) {
            String method = insertMethodCall(methodsInsideOfJob, propertyDescriptor, property);
            if (method != null) {
                methods.add(method);
            }
        }
        return methods;
    }

    public void insertVariable(PropertyDescriptor propertyDescriptor, String property) {
        if (variableDeclarations.contains(property)) {
            return;
        }

        variableDeclarations.add( 
            String.format(syntaxProperties.getProperty("syntax.string_variable"), 
            property, propertyDescriptor.getValue()));
    }

    public String insertMethodCall(List<String> methodsInsideOfJob, 
            PropertyDescriptor propertyDescriptor, String property) {
        
        if (methodsInsideOfJob.contains(property)) {
            removeMethodCallAndAddClosure(methodsInsideOfJob, propertyDescriptor, property);
            return null;
        }

        return String.format(
            syntaxProperties.getProperty("syntax.method_call"), property, propertyDescriptor.getValue());
    }

    public void removeMethodCallAndAddClosure(List<String> methods, PropertyDescriptor propertyDescriptor, String property) {
        for (Iterator<String> iterator = methods.iterator(); iterator.hasNext();) {
            
            String line = iterator.next();
            
            if (line.contains(property)) {
                if (!line.contains(propertyDescriptor.getValue())) {
                    createClosureWithParameter();
                } else {
                    createClosure();
                }
                iterator.remove();
                break;
            }
        }
    }

    public String createClosure() {
        return "";
    }

    public String createClosureWithParameter() {
        return "";
    }

    public String convertListInString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : list) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }
}
