package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.IValueStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
A custom class for methods where the children must be returned in a specific order. To use, add a new entry to the
supportedAttributes hashmap with the correct order of children attributes. Note that the names used will be how they
appear in the XML, not what they're called in DSL.
 */
public class DSLCustomOrderedChildrenStrategy extends DSLMethodStrategy {
    private final String methodName;
    private HashMap<String, ArrayList> supportedAttributes = new HashMap<String, ArrayList>(){{
        put("tasks", new ArrayList<String>(){{
            add("logText");
            add("script");
            add("EscalateStatus");
            add("RunIfJobSuccessful");
        }});
    }};

    public DSLCustomOrderedChildrenStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, methodName, shouldInitChildren);
        this.methodName = methodName;
        this.setTabs(tabs);
    }

    public DSLCustomOrderedChildrenStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        this(tabs, propertyDescriptor, methodName, true);
    }

    public DSLCustomOrderedChildrenStrategy(PropertyDescriptor descriptor) {
        this(0, descriptor, null, true);
    }

    @Override
    protected String getChildrenDSL() {
        StringBuilder dsl = new StringBuilder();

        List<DSLStrategy> children = getChildren();
        int size = children.size();

        // Get all nested viable children out and add all to a single collection
        for (int index = 0; index < size; index++) {
            DSLStrategy strategy = children.get(index);

            // check if strategy is "viable" ie not an INNER
            if(strategy instanceof IValueStrategy == false){
                // check for viable children
                children.addAll(findViableChildren(strategy));
            }
        }

        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        ArrayList<String> childrenOrder = supportedAttributes.get(propertyDescriptor.getName());

        for (int index = 0; index < childrenOrder.size(); index++) {
            String child = childrenOrder.get(index);
            String strategyDsl = getChildrenByName(child).toDSL();
            dsl.append(strategyDsl);
            if (index < childrenOrder.size() - 1) {
                dsl.append(", ");
            }
        }

        return dsl.toString();
    }
}
