package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.IDescriptor;
import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLMethodStrategy extends AbstractDSLStrategy {

    private final String methodName;
    private final PropertyDescriptor propertyDescriptor;

    public DSLMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(propertyDescriptor);
        this.methodName = methodName;
        this.propertyDescriptor = propertyDescriptor;
        this.setTabs(tabs);
    }

    public DSLMethodStrategy(PropertyDescriptor descriptor) {
        super(descriptor);
        methodName = null;
        propertyDescriptor = descriptor;
    }

    @Override
    public String toDSL() {
        if (propertyDescriptor.getValue() != null) {

            boolean isParentAMethod = propertyDescriptor.getParent() != null &&
                    getType(propertyDescriptor.getParent()).equals(TYPE_METHOD);

            if (isParentAMethod) {
                return getStrategyForObject().toDSL();
            }

            return replaceTabs(String.format(getSyntax("syntax.method_call"),
                    methodName, printValueAccordingOfItsType(propertyDescriptor.getValue())), getTabs());
        }

        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, getChildrenDSL()), getTabs());
    }

    private DSLStrategy getStrategyForObject() {
        List<PropertyDescriptor> siblings = getChildrenOfType(propertyDescriptor.getParent(), TYPE_METHOD);

        propertyDescriptor.getParent().getProperties().clear();

        List<PropertyDescriptor> children = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : siblings) {
            children.add(new PropertyDescriptor(propertyDescriptor.getName(), null,
                    propertyDescriptor.getValue(), propertyDescriptor.getProperties(),
                    propertyDescriptor.getAttributes()));
        }
        PropertyDescriptor object = new PropertyDescriptor(null, null, children);
        return new DSLObjectStrategy(getTabs(), object, null);
    }

    @Override
    protected String getChildrenDSL() {
        StringBuilder dsl = new StringBuilder();

        int size = getChildren().size();

        for (int index = 0; index < size; index++) {
            DSLStrategy strategy = getChildren().get(index);
            String strategyDsl = strategy.toDSL();
            dsl.append(strategyDsl);
            if (index < size - 1 && (strategy instanceof IValueStrategy)) {
                dsl.append(", ");
            }
        }
        return dsl.toString();
    }
}
