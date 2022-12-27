package com.adq.jenkins.xmljobtodsl.parsers;

import java.util.List;

public interface IDescriptor {
    String getName();
    List<PropertyDescriptor> getProperties();

    List<PropertyDescriptor> getAddedProperties();
}
