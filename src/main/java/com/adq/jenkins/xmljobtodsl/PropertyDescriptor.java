package com.adq.jenkins.xmljobtodsl;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class PropertyDescriptor {
    private String name;
    private List<PropertyDescriptor> properties;
    private Map<String, String> attributes;
    private String value;

    public PropertyDescriptor(String name, String value) {
        this(name, value, null);
    }

    public PropertyDescriptor(String name, String value, Map<String, String> attributes) {
        this(name, value, null, attributes);
    }

    public PropertyDescriptor(String name, List<PropertyDescriptor> properties) {
        this(name, properties, null);
    }

    public PropertyDescriptor(String name, Map<String, String> attributes) {
        this(name, null, null, attributes);
    }

    public PropertyDescriptor(String name, List<PropertyDescriptor> properties, Map<String, String> attributes) {
        this(name, null, properties, attributes);
    }

    public PropertyDescriptor(String name, String value, List<PropertyDescriptor> properties, Map<String, String> attributes) {
        this.name = name;
        this.value = value;
        this.properties = properties;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public List<PropertyDescriptor> getProperties() {
        return properties;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getValue() {
        return value;
    }
}