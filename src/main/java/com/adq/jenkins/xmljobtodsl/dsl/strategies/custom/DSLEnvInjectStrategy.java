package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DSLEnvInjectStrategy extends DSLObjectStrategy {
    private final String name;

    private HashMap<String, String> expectedProperties = new HashMap<String, String>(){{
        put("propertiesFilePath", "");
        put("scriptFilePath", "");
        put("scriptContent", "");
        put("loadFilesFromMaster", "false");
        put("secureGroovyScript", "");
    }};

    public DSLEnvInjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
        addMissingProperties(propertyDescriptor);
    }

    public DSLEnvInjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
        addMissingProperties(propertyDescriptor);
    }

    private void addMissingProperties(PropertyDescriptor propertyDescriptor){
        // Make a copy of the properties that we can iterate over and remove elements as we go.
        List<PropertyDescriptor> properties = new ArrayList<>();
        properties.addAll(propertyDescriptor.getProperties());

        for (HashMap.Entry<String, String> expectedProp : expectedProperties.entrySet()) {
            boolean foundProperty = false;
            int indexToRemove = 0;

            for(PropertyDescriptor existingProp : properties){
                if(existingProp.getName() == expectedProp.getKey()){
                    foundProperty = true;
                    indexToRemove = properties.indexOf(existingProp);
                    break;
                }
            }

            if(foundProperty){
                properties.remove(indexToRemove);
            } else {
                PropertyDescriptor newChild = new PropertyDescriptor(
                        expectedProp.getKey(),
                        propertyDescriptor.getParent(),
                        expectedProp.getValue(),
                        null
                );

                propertyDescriptor.getProperties().add(newChild);

        }

            initChildren(propertyDescriptor);
        }
    }

    @Override
    public String toDSL() {
        String childrenDSL = getChildrenDSL();

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}
