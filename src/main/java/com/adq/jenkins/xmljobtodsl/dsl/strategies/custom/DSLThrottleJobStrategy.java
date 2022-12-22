package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

/*
throttleJobProperty is in the xml for all jobs whether or not it is being used.
If any of the properties are left out of the dsl or don't contain a value, there are errors building the job
This class will skip printing all the values if the throttle is disabled
And gives us power in the future to handle things more flexibly.
 */
public class DSLThrottleJobStrategy extends DSLObjectStrategy {
    private final String name;

    public DSLThrottleJobStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLThrottleJobStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
    }

    @Override
    public String toDSL() {
        String childrenDSL = getChildrenDSL();

        if (childrenDSL.isEmpty() || childrenDSL.contains("throttleEnabled(false)")) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}
