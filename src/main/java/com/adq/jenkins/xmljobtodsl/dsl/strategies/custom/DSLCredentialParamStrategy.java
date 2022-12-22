package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

public class DSLCredentialParamStrategy extends DSLParamStrategy {

    private final String methodName;

    public DSLCredentialParamStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(tabs, propertyDescriptor, methodName);
        this.methodName = methodName;
    }

    @Override
    public String toDSL() {
        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, getOrderedChildrenDSL()), getTabs());
    }

    @Override
    public String getOrderedChildrenDSL() {
        String variable = getChildrenByName("variable").toDSL();
        String credentialsId = getChildrenByName("credentialsId").toDSL();
        return variable + ", " + credentialsId;
    }

}
