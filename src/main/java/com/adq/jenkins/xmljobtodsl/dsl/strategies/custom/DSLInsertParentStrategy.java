package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.AbstractDSLStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.IDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class DSLInsertParentStrategy extends DSLObjectStrategy {


    public DSLInsertParentStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, false);

        if (propertyDescriptor.getName() == "triggers" && !hasPropertyWithName(propertyDescriptor.getProperties(), "cron")) {
            propertyDescriptor.getProperties().add(0, new PropertyDescriptor("cron", propertyDescriptor, "test"));
            System.out.println(propertyDescriptor);
            System.out.println(propertyDescriptor.getProperties());
//            propertyDescriptor.getProperties().add()


        }

//        PropertyDescriptor parent = propertyDescriptor.getParent().getParent();
//        PropertyDescriptor cron = new PropertyDescriptor("cron", propertyDescriptor);
//        System.out.println(parent.getProperties());
//        System.out.println(parent.getName());
//        propertyDescriptor.getProperties().add(0, cron);
//        System.out.println(propertyDescriptor);
    }

    private boolean hasPropertyWithName(List<PropertyDescriptor> propertyDescriptorList, String name) {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
            if (propertyDescriptor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
