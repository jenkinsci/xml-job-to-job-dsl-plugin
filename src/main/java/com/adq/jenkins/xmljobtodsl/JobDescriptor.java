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
}