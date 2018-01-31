package com.adq.jenkins.xmljobtodsl.parsers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyDescriptor implements IDescriptor {

    private String name;
    private PropertyDescriptor parent;
    private List<PropertyDescriptor> properties;
    private Map<String, String> attributes;
    private String value;

    public PropertyDescriptor(String name, PropertyDescriptor parent) {
        this(name, parent, null, null, null);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , String value) {
        this(name, parent, value, null);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , String value, Map<String, String> attributes) {
        this(name, parent, value, null, attributes);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , List<PropertyDescriptor> properties) {
        this(name, parent, properties, null);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , Map<String, String> attributes) {
        this(name, parent, null, null, attributes);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , List<PropertyDescriptor> properties, Map<String, String> attributes) {
        this(name, parent, null, properties, attributes);
    }

    public PropertyDescriptor(String name, PropertyDescriptor parent , String value, List<PropertyDescriptor> properties, Map<String, String> attributes) {
        this.name = name;
        this.value = value;
        this.properties = properties;
        this.attributes = attributes;
        this.parent = parent;
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

    public PropertyDescriptor getParent() {
        return parent;
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

        return true;
    }

    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getName().equals("parent");
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });
        return builder.create().toJson(this).replaceAll(Pattern.quote("\\r"), Matcher.quoteReplacement(""));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}