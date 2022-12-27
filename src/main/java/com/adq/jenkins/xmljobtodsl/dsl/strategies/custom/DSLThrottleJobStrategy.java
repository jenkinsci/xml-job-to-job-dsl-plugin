package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
throttleJobProperty is in the xml for all jobs whether or not it is being used.
If any of the properties are left out of the dsl or don't contain a value, there are errors building the job
This class will skip printing all the values if the throttle is disabled and will add missing properties if the feature is enabled
 */
public class DSLThrottleJobStrategy extends DSLObjectStrategy {
    private final String name;

    // This is the map of all the elements we want to guarantee will show up with throttleJobProperty and default values.
    // matrixOptions being the exception which is taken care of later in the class.
    private HashMap<String, String> expectedProperties = new HashMap<String, String>(){{
        put("maxConcurrentPerNode", "0");
        put("maxConcurrentTotal", "1");
        put("categories", "");
        put("throttleEnabled", "false");
        put("throttleOption", "");
        put("limitOneJobWithMatchingParams", "false");
        put("paramsToUseForLimit", "");
        put("matrixOptions", "");
    }};

    public DSLThrottleJobStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
        addMissingProperties(propertyDescriptor);
    }

    public DSLThrottleJobStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
        addMissingProperties(propertyDescriptor);
    }

    private void addMissingProperties(PropertyDescriptor propertyDescriptor){
        // Make a copy of the properties that we can iterate over and remove elements as we go.
        List<PropertyDescriptor> properties = new ArrayList<>();
        properties.addAll(propertyDescriptor.getProperties());

        boolean featureEnabled = true;

        for (HashMap.Entry<String, String> expectedProp : expectedProperties.entrySet()) {
            boolean foundProperty = false;
            int indexToRemove = 0;

            if(!featureEnabled){
                break;
            }

            for(PropertyDescriptor existingProp : properties){
                if(existingProp.getName() == expectedProp.getKey()){
                    foundProperty = true;
                    indexToRemove = properties.indexOf(existingProp);
                    break;
                }
                if(existingProp.getName().equals("throttleEnabled") && existingProp.getValue().equals("false")){
                    featureEnabled = false;
                    break;
                }
            }

            if(foundProperty){
                properties.remove(indexToRemove);
            } else if (featureEnabled){
                List<PropertyDescriptor> grandChildren = new ArrayList<>();
                // matrixOptions is special as it's an object with subchildren that we'll create on the fly
                if(expectedProp.getKey().equals("matrixOptions")){
                    PropertyDescriptor throttleMatrixBuildsChild = new PropertyDescriptor(
                        "throttleMatrixBuilds",
                        null,
                        "false"
                    );

                    PropertyDescriptor throttleMatrixConfigurationsChild = new PropertyDescriptor(
                            "throttleMatrixConfigurations",
                            null,
                            "false"
                    );

                    grandChildren.add(throttleMatrixBuildsChild);
                    grandChildren.add(throttleMatrixConfigurationsChild);
                }

                PropertyDescriptor newChild = new PropertyDescriptor(
                        expectedProp.getKey(),
                        propertyDescriptor.getParent(),
                        expectedProp.getValue(),
                        grandChildren,
                        null
                );

                propertyDescriptor.getProperties().add(newChild);
            }
        }

        if(featureEnabled){
            initChildren(propertyDescriptor);
        }
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
