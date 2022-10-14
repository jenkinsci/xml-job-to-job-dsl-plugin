package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

/*
Strategy for attributes where in XML they have 1 tag but in DSL they are nested attributes.
Example:
<selector class="hudson.plugins.copyartifact.ParameterizedBuildSelector">
    <parameterName>BUILD_SELECTOR</parameterName>
</selector>

Translates to

selector {
    buildParameter {
        // Name of the "build selector" parameter.
        parameterName(String value)
    }
}

So we inject an enclosing tag called "buildParameter"
 */
public class DSLEnclosedObjectStrategy extends DSLObjectStrategy {
    public DSLEnclosedObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, false);

        PropertyDescriptor currentParent = propertyDescriptor.getParent();

        PropertyDescriptor newParent = new PropertyDescriptor(
                getPropertyByName(String.format("%s.enclosing_tag", propertyDescriptor.getName())),
                currentParent);

        PropertyDescriptor newPropertyDescriptor = new PropertyDescriptor(newParent.getName(), newParent.getParent(),
                currentParent.getProperties(), newParent.getAttributes());

        initChildren(newPropertyDescriptor);
    }
}
