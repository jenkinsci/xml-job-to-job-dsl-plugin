package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLWorkflowDefinitionStrategy extends DSLObjectStrategy {

    public DSLWorkflowDefinitionStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, false);

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        PropertyDescriptor firstChild = children.get(0);
        List<PropertyDescriptor> directChildren = new ArrayList<>();
        PropertyDescriptor child;

        if (firstChild.getName().equals("script")) {
            child = new PropertyDescriptor(
                    getPropertyByName(String.format("cps", propertyDescriptor.getName())),
                    propertyDescriptor, children);
        } else {
            child = new PropertyDescriptor(
                    getPropertyByName(String.format("cpsScm", propertyDescriptor.getName())),
                    propertyDescriptor, children);
        }
        directChildren.add(child);

        PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor(propertyDescriptor.getName(), propertyDescriptor.getParent(),
                directChildren, propertyDescriptor.getAttributes());

        initChildren(newPropertyDescriptor);
        }
    }

