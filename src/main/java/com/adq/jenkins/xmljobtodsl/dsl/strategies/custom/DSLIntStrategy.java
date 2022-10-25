package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLIntStrategy extends DSLMethodStrategy {

    private final String methodName;

    public DSLIntStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(tabs, propertyDescriptor, methodName);
        this.methodName = methodName;
    }


    @Override
    public String toDSL() {
        PropertyDescriptor descriptor = (PropertyDescriptor) getDescriptor();
        String value = descriptor.getValue();
        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, value), getTabs());
    }

}
