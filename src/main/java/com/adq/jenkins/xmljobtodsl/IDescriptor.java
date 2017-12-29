package com.adq.jenkins.xmljobtodsl;

import java.util.List;

public interface IDescriptor {
    String getName();
    List<PropertyDescriptor> getProperties();
}
