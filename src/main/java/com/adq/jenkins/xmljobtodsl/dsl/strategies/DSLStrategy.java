package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.IDescriptor;
import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.Collection;
import java.util.List;

public interface DSLStrategy {

    List<DSLStrategy> getChildren();

    void addChild(DSLStrategy strategy);

    String toDSL();

    int getTabs();

    void setTabs(int tabs);

    List<PropertyDescriptor> getNotTranslatedList();

    IDescriptor getDescriptor();
}
