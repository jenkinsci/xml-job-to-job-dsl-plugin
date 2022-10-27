package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
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
        List<PropertyDescriptor> leftoverProperties = new ArrayList<>();

        String enclosingTagName = getPropertyByName(String.format("%s.enclosing_tag", propertyDescriptor.getName()));

        // Rename the children, so we can give them a new type in translator.properties
        for(PropertyDescriptor child : children){
            // Make sure the enclosing tag of each sibling matches the current tag / exists
            String childEnclosingTag = getPropertyByName(String.format("%s.enclosing_tag", child.getName()));

            if(childEnclosingTag != null && childEnclosingTag.equals(enclosingTagName)) {
                PropertyDescriptor newChild = new PropertyDescriptor(
                        String.format("%sEnclosed", child.getName()),
                        child.getParent(),
                        child.getValue(),
                        child.getProperties(),
                        child.getAttributes());
                renamedChildren.add(newChild);
            } else {
                leftoverProperties.add(child);
            }
        }

        if(renamedChildren.size() == 0){
            this.name = null;
        } else {
            this.name = enclosingTagName;
            PropertyDescriptor enclosingPropertyDescriptor = new PropertyDescriptor(enclosingTagName, propertyDescriptor.getParent(), renamedChildren);

            // Add the new parent to the children properties and attach to the "grandparent" so we know if we've done this before
            leftoverProperties.add(enclosingPropertyDescriptor);
            propertyDescriptor.getParent().replaceProperties(leftoverProperties);

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
