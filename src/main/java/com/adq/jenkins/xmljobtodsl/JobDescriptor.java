package com.adq.jenkins.xmljobtodsl;

import java.util.List;

public class JobDescriptor {
    private List<PropertyDescriptor> properties;

    public JobDescriptor(List<PropertyDescriptor> properties) {
        this.properties = properties;
    }

    public List<PropertyDescriptor> getProperties() {
        return properties;
    }
}