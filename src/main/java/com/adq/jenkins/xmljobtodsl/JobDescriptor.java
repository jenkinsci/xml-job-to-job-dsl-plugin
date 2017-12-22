package com.adq.jenkins.xmljobtodsl;

import java.util.List;

public class JobDescriptor {

    private String name;
    private List<PropertyDescriptor> properties;

    public JobDescriptor(String name, List<PropertyDescriptor> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public List<PropertyDescriptor> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobDescriptor)) {
            return false;
        }
        JobDescriptor other = (JobDescriptor) obj;
        if (!other.getName().equals(this.getName())) {
            return false;
        }
        return other.getProperties().equals(this.getProperties());
    }

    @Override
    public String toString() {
        return String.format("{%n    name: \"%s\",%n    properties: %s%n}", getName(),
                getProperties() == null ? "null" : String.format("[%n%s%n]", getProperties().toString()));
    }
}