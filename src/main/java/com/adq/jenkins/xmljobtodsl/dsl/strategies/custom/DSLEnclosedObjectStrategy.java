package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

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

You will need to define the property twice in translator.properties -- once as its true name, again as trueNameEnclosed
Then define the enclosing tag & its type. Example:

hudson.plugins.copyartifact.CopyArtifact.selector.parameterName = parameterName
hudson.plugins.copyartifact.CopyArtifact.selector.parameterName.type = com.adq.jenkins.xmljobtodsl.dsl.strategies.custom.DSLEnclosedObjectStrategy
parameterName.enclosing_tag = buildParameter

buildParameter = buildParameter
buildParameter.type = OBJECT

parameterNameEnclosed = parameterName
 */
public class DSLEnclosedObjectStrategy extends DSLObjectStrategy {
    private final String name;
    public DSLEnclosedObjectStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, true);

        // It's easier to grab all the siblings and place them under the same parent at once.
        List<PropertyDescriptor> children = propertyDescriptor.getParent().getProperties();
        List<PropertyDescriptor> renamedChildren = new ArrayList<>();

        String enclosingTagName = getPropertyByName(String.format("%s.enclosing_tag", propertyDescriptor.getName()));

        // check if we've done this before
        if(children.size() == 1 && children.get(0).getName().equals(enclosingTagName)){
            this.name = null;
        } else {
            // Rename the children so we can give them a new type in translator.properties
            for(PropertyDescriptor child : children){
                PropertyDescriptor newChild = new PropertyDescriptor(
                        String.format("%sEnclosed", child.getName()),
                        child.getParent(),
                        child.getValue(),
                        child.getProperties(),
                        child.getAttributes());
                renamedChildren.add(newChild);
            }

            this.name = enclosingTagName;
            PropertyDescriptor enclosingPropertyDescriptor = new PropertyDescriptor(enclosingTagName, propertyDescriptor.getParent(), renamedChildren);

            // Replace the parent's properties w/ our new parent so we know if we've done this before
            List<PropertyDescriptor> newProperties = new ArrayList<>();
            newProperties.add(enclosingPropertyDescriptor);
            propertyDescriptor.getParent().replaceProperties(newProperties);

            initChildren(enclosingPropertyDescriptor);
        }
    }

    @Override
    public String toDSL() {
        String childrenDSL = getChildrenDSL();
        if (childrenDSL.isEmpty()) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}
