package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.*;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLDoNotDisplayEmptyMethodStrategy extends DSLMethodStrategy {
    private final String methodName;

    public DSLDoNotDisplayEmptyMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, methodName, shouldInitChildren);
        this.methodName = methodName;
        this.setTabs(tabs);
    }
    public DSLDoNotDisplayEmptyMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        this(tabs, propertyDescriptor, methodName, true);
    }

    public DSLDoNotDisplayEmptyMethodStrategy(PropertyDescriptor descriptor) {
        this(0, descriptor, null, true);
    }

    @Override
    public String toDSL() {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        if (propertyDescriptor.getValue() != null) {

            boolean isParentAMethod = propertyDescriptor.getParent() != null &&
                    getType(propertyDescriptor.getParent()).equals(DSLStrategyFactory.TYPE_METHOD);

            if (isParentAMethod) {
                return getStrategyForObject(propertyDescriptor).toDSL();
            }

            return replaceTabs(String.format(getSyntax("syntax.method_call"),
                    methodName, printValueAccordingOfItsType(propertyDescriptor.getValue())), getTabs());

        } else if (propertyDescriptor.getValue() == null) {
            return "";
        }
        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, getChildrenDSL()), getTabs());
    }

    private DSLStrategy getStrategyForObject(PropertyDescriptor propertyDescriptor) {
        List<PropertyDescriptor> siblings = getChildrenOfType(propertyDescriptor.getParent(), DSLStrategyFactory.TYPE_METHOD);

        propertyDescriptor.getParent().getProperties().clear();

        List<PropertyDescriptor> children = new ArrayList<>();
        for (PropertyDescriptor descriptor : siblings) {
            children.add(new PropertyDescriptor(descriptor.getName(), null,
                    descriptor.getValue(), descriptor.getProperties(),
                    descriptor.getAttributes()));
        }
        PropertyDescriptor object = new PropertyDescriptor(null, null, children);
        return new DSLObjectStrategy(getTabs(), object, null);
    }

}
