package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.*;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

// Methods with null values (e.g., baseUrl() teamDomain()), cause errors when creating jobs in the DSL. If a propertyDescriptor has a null value, do not render it in the converted DSL.
// Otherwise, convert as normal with descriptor and value
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
}
