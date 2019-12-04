package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;

import hudson.model.listeners.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSLChoiceTypeStrategy extends DSLMethodStrategy {

    private final String methodName;

    public DSLChoiceTypeStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(tabs, propertyDescriptor, methodName);
        this.methodName = methodName;
    }

    @Override
    public String toDSL() {

        // convert XML choiceType to jobDSL required choiceType

        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        String choiceType = propertyDescriptor.getValue();

        String fixedChoiceType = printValueAccordingOfItsType(choiceType.substring(choiceType.indexOf("_") + 1));

        return replaceTabs(String.format(getSyntax("syntax.method_call"), methodName, fixedChoiceType), getTabs());
    }

    private static final Logger LOGGER = Logger.getLogger(DSLChoiceTypeStrategy.class.getName());
}

