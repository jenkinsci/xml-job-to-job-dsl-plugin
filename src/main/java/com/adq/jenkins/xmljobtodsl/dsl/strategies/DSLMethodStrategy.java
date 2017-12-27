package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.List;

public class DSLMethodStrategy extends AbstractDSLStrategy {

    private final String methodName;
    private final PropertyDescriptor propertyDescriptor;

    public DSLMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(propertyDescriptor, tabs);
        this.methodName = methodName;
        this.propertyDescriptor = propertyDescriptor;
    }

    @Override
    public String toDSL() {
        if (propertyDescriptor.getValue() != null) {
            String method = "";
            boolean isParentAMethod = propertyDescriptor.getParent() != null &&
                    getType(propertyDescriptor.getParent()).equals(TYPE_METHOD);

            List<PropertyDescriptor> siblings = null;

            if (isParentAMethod) {
                siblings = getChildrenOfType(propertyDescriptor.getParent(), TYPE_METHOD);

                if (siblings.get(0).equals(propertyDescriptor)) {
                    method = "{\n";
                }
            }

            method += String.format(getSyntaxProperties().getProperty("syntax.method_call"),
                    methodName, printValueAccordingOfItsType(propertyDescriptor.getValue()));

            if (isParentAMethod &&
                    siblings.get(siblings.size() - 1).equals(propertyDescriptor)) {
                method += " }";
            }
            return method;
        } else {
            return String.format(getSyntaxProperties().getProperty("syntax.method_call"),
                    methodName, getChildrenDSL());
        }
    }
}
