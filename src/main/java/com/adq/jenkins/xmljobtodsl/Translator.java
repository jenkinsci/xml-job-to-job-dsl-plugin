package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class Translator {

    private static final String PREFIX_XML = "xml.";
    private static final String PREFIX_GROOVY = "groovy.";
    private static final String SUFIX_GROOVY_TYPE = ".type";
    private static final String TYPE_CONST = "CONST";
    private static final String TYPE_INNER = "INNER";
    private static final String TYPE_OBJECT_ASSIGN = "OBJECT_ASSIGN";
    private static final String TYPE_PARAMETER = "PARAMETER";
    private static final String TYPE_METHOD_OBJECT_INIT = "METHOD_OBJECT_INIT";
    private static final String TYPE_METHOD_OBJECT_END = "METHOD_OBJECT_END";
    private static final String TYPE_METHOD_WITH_OBJECT_PARAMETERS = "METHOD_WITH_OBJECT_PARAMETERS";

	private String variableDeclarations = "";
    private String objectDeclarations = "";
    private String closureDeclarations = "";
    private String jobDeclarations = "";
    private String viewDeclarations = "";

    private JobDescriptor[] jobDescriptors;

    private Properties syntaxProperties;
    private Properties translatorProperties;

    private int tabs = 0;

    public Translator(JobDescriptor[] jobDescriptors) throws IOException {
        this.jobDescriptors = jobDescriptors;
        initProperties();
    }

    private void initProperties() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("syntax.properties");
        syntaxProperties = new Properties();
        syntaxProperties.load(in);

        in = getClass().getClassLoader().getResourceAsStream("translator.properties");
        translatorProperties = new Properties();
        translatorProperties.load(in);
    }

    public String toDSL() {
        for (JobDescriptor jobDescriptor : jobDescriptors) {
            jobDeclarations += String.format(syntaxProperties.getProperty("syntax.job"), "job", jobDescriptor.getName(),
                getDSLofJobProperties(jobDescriptor.getProperties()));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(variableDeclarations);
        stringBuilder.append(objectDeclarations);
        stringBuilder.append(closureDeclarations);
        stringBuilder.append(jobDeclarations);
        stringBuilder.append(viewDeclarations);
        return stringBuilder.toString();
    }

    public String getDSLofJobProperties(List<PropertyDescriptor> jobProperties) {
        String result = "";
        for (PropertyDescriptor propertyDescriptor : jobProperties) {
            result += proccessPropertyDescriptor(propertyDescriptor);
        }
        return result;
    }

    public String tabs() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tabs; i++) {
            builder.append("\t");
        }
        return builder.toString();
    }

    public String proccessPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        String key = String.format("%s%s", PREFIX_GROOVY, propertyDescriptor.getName());
        String property = translatorProperties.getProperty(key);

        String result = "";

        if (property == null) {
            return "";
        }

        String type = String.format("%s%s", key, SUFIX_GROOVY_TYPE);
        
        if (translatorProperties.containsKey(type)) {
            return processSpecialTypes(propertyDescriptor, property, type);
        }

        if (TYPE_INNER.equals(property)) {
            tabs--;
            result += getDSLofJobProperties(propertyDescriptor.getProperties());
            return result;
        }

        if (translatorProperties.contains(property)) {
            result += insertMethodCall(propertyDescriptor, property);
        }
        return result;
    }

    private String processSpecialTypes(PropertyDescriptor propertyDescriptor, String property, String type) {
        String propertyType = translatorProperties.getProperty(type);

        if (TYPE_CONST.equals(propertyType)) {
            return insertVariable(propertyDescriptor, property);
        }
        if (TYPE_OBJECT_ASSIGN.equals(propertyType)) {
            return insertObjectAssign(propertyDescriptor, property);
        }
        if (TYPE_PARAMETER.equals(propertyType)) {
            return insertParameter(propertyDescriptor);
        }
        if (TYPE_METHOD_WITH_OBJECT_PARAMETERS.equals(propertyType)) {
            return insertMethodCallWithParameters(propertyDescriptor, property);
        }
        if (TYPE_METHOD_OBJECT_INIT.equals(propertyType)) {
            return insertObjectInit(propertyDescriptor, property);
        }
        if (TYPE_METHOD_OBJECT_END.equals(propertyType)) {
            return insertObjectEnd(propertyDescriptor, property);
        }
        return "";
    }

    private String insertObject(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
                syntaxProperties.getProperty("syntax.object"),
                getDSLofJobProperties(propertyDescriptor.getProperties()));
    }

    private String insertObjectInit(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
                syntaxProperties.getProperty("syntax.object_init"),
                insertMethodCall(propertyDescriptor, property));
    }

    private String insertObjectEnd(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
                syntaxProperties.getProperty("syntax.object_end"),
                insertMethodCall(propertyDescriptor, property));
    }

    private String insertParameter(PropertyDescriptor propertyDescriptor) {
        return String.format(syntaxProperties.getProperty("syntax.method_param"),
                printValueAccordingOfItsType(propertyDescriptor.getValue()));
    }

    public String insertVariable(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(syntaxProperties.getProperty("syntax.string_variable"),
            property, printValueAccordingOfItsType(propertyDescriptor.getValue()));
    }

    public String insertMethodCall(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
            syntaxProperties.getProperty("syntax.method_call"), property, printValueAccordingOfItsType(propertyDescriptor.getValue()));
    }

    public String insertMethodCallWithParameters(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
                syntaxProperties.getProperty("syntax.method_call"), property,
                getDSLofJobProperties(propertyDescriptor.getProperties()));
    }

    public String insertObjectAssign(PropertyDescriptor propertyDescriptor, String property) {
        return String.format(
                syntaxProperties.getProperty("syntax.object_with_object"), property,
                getDSLofJobProperties(propertyDescriptor.getProperties()));
    }

    public String printValueAccordingOfItsType(String value) {
        if (value == null) {
            return "null";
        }
        if (value.equals("true") || value.equals("false")) {
            return value;
        }
        if (value.matches("[0-9.]+")) {
            return value;
        }
        if (value.contains("\n")) {
            return "\"\"\"" + value + "\"\"\"";
        }
        return "\"" + value + "\"";
    }

    /*public void removeMethodCallAndAddClosure(List<String> methods, PropertyDescriptor propertyDescriptor, String property) {
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
*/
}
