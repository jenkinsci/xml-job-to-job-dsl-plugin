package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLObjectIgnoreParentStrategy extends DSLObjectStrategy {

    private final String name;
    private Boolean ignore = false;

    public DSLObjectIgnoreParentStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
        
        if(propertyDescriptor.getAttributes() == null || !propertyDescriptor.getAttributes().containsKey("ignoredParent")){
            ignoreParentNode(propertyDescriptor);
            this.ignore = true;
        }
    }

    public DSLObjectIgnoreParentStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
        if(propertyDescriptor.getAttributes() == null || !propertyDescriptor.getAttributes().containsKey("ignoredParent")){
            ignoreParentNode(propertyDescriptor);
            this.ignore = true;
        }
    }

    private void ignoreParentNode(PropertyDescriptor propertyDescriptor){
        PropertyDescriptor grandparent = propertyDescriptor.getParent().getParent();

        // Change the current node's attributes to indicate it's been touched already
        // it'll be processed again in AbstractDSLStrategy
        propertyDescriptor.addAttribute("ignoredParent", "1");
        grandparent.addProperty(propertyDescriptor);
    }

    @Override
    public String toDSL() {
        String childrenDSL = getChildrenDSL();
        if (childrenDSL.isEmpty() || ignore) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}

//public class DSLObjectIgnoreParentStrategy extends DSLObjectStrategy {
//
//    private final String name;
//
//    public DSLObjectIgnoreParentStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
//        super(tabs, propertyDescriptor, name, shouldInitChildren);
//        this.name = name;
//
//        PropertyDescriptor grandparent = propertyDescriptor.getParent().getParent();
//
//        // Remove the current node as a child from its parent
//        List<PropertyDescriptor> siblingProperties = propertyDescriptor.getParent().getProperties();
//        siblingProperties.remove(propertyDescriptor);
//        propertyDescriptor.getParent().replaceProperties(siblingProperties);
//
//        // Make a new list of children including the current node and its parents' siblings
//        List<PropertyDescriptor> newChildren = grandparent.getProperties();
//        newChildren.add(propertyDescriptor);
//
//        // Replace the children of the grandparent
//        grandparent.replaceProperties(newChildren);
//        initChildren(grandparent);
//    }
//
//    @Override
//    public String toDSL() {
//        String childrenDSL = getChildrenDSL();
//        if (childrenDSL.isEmpty()) {
//            return "";
//        }
//
//        if (name != null) {
//            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
//        } else {
//            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
//        }
//    }
//}
