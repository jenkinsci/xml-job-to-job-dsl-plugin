package com.adq.jenkins.xmljobtodsl;

import java.util.List;
import java.util.Map;

public class PropertyDescriptor {
    private String name;
    private List<PropertyDescriptor> properties;
    private Map<String, String> attributes;
    private String value;

    public PropertyDescriptor(String name) {
        this(name, null, null, null);
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyDescriptor)) {
            return false;
        }
        PropertyDescriptor other = (PropertyDescriptor) obj;
        if (!other.getName().equals(this.getName())) {
            return false;
        }

        if (other.getAttributes() != null && this.getAttributes() != null) {
            if (!other.getAttributes().equals(this.getAttributes())) {
                return false;
            }
        }

        if (other.getProperties() != null && this.getProperties() != null) {
            if (!other.getProperties().equals(this.getProperties())) {
                return false;
            }
        }

        if (other.getValue() != null && this.getValue() != null) {
            if (!other.getValue().equals(this.getValue())) {
                return false;
            }
        }
        return other.getProperties().equals(this.getProperties());
    }

    @Override
    public String toString() {
        return String.format("{%n    name: \"%s\",%n    value: \"%s\",%n    attributes: %s,%n    properties: %s%n}",
                getName(), getValue(),
                getAttributes() == null ? "null" : String.format("[%n%s%n]", getAttributes().toString()),
                getProperties() == null ? "null" : String.format("[%n%s%n]", getProperties().toString()));
    }
}