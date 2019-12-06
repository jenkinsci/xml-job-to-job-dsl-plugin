package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;

import hudson.model.listeners.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSLReferencedParameterStrategy extends DSLMethodStrategy {

    private final String methodName;

    public DSLReferencedParameterStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(tabs, propertyDescriptor, methodName);
        this.methodName = methodName;
    }

    @Override
    public String toDSL() {

        // tokenize referenceParameters in to multiple referencedParameter

        StringBuilder dsl = new StringBuilder();
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();

        String referencedParams  = propertyDescriptor.getValue();

        String[] splitParams = referencedParams.split(",");

        for (String param : splitParams) {
          dsl.append(replaceTabs(String.format(getSyntax("syntax.method_call"), 
                  methodName, '"' + param.trim() + '"'), getTabs()));
        }

        return dsl.toString();
    }
  
  private static final Logger LOGGER = Logger.getLogger(DSLReferencedParameterStrategy.class.getName());
}

