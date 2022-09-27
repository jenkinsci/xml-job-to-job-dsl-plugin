package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class himom extends DSLObjectStrategy {

    private List<PropertyDescriptor> promoSteps;

    public himom(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, false);

        this.promoSteps = propertyDescriptor.getProperties();
        initChildren(propertyDescriptor);
    }

    public String getNestedConfigs() {
        String himomRet = "";

        for(PropertyDescriptor step : this.promoSteps){
            step.set(0, "name");
            himomRet += replaceTabs(String.format(getSyntax("syntax.object_with_name"), "promotion", step), getTabs());
            // Here get DSL from child config.xml
        }
        return himomRet;
    }

    @Override
    public String toDSL() {
        return getNestedConfigs();
    }

}