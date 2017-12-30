package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.IDescriptor;
import com.adq.jenkins.xmljobtodsl.PropertyDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractDSLStrategy implements DSLStrategy {

    public static final String PREFIX_GROOVY = "groovy.";
    public static final String SUFIX_GROOVY_TYPE = ".type";

    public static final String TYPE_CONST = "CONST";
    public static final String TYPE_INNER = "INNER";
    public static final String TYPE_OBJECT = "OBJECT";
    public static final String TYPE_METHOD = "METHOD";
    public static final String TYPE_PARAMETER = "PARAMETER";
    public static final String TYPE_PROPERTY = "PROPERTY";
    public static final String TYPE_ARRAY = "ARRAY";

    private Properties syntaxProperties;
    private Properties translatorProperties;

    private List<DSLStrategy> children = new ArrayList<>();

    private int tabs = 0;

    public AbstractDSLStrategy(IDescriptor descriptor) {
       this(0, descriptor);
    }

    public AbstractDSLStrategy(int tabs, IDescriptor descriptor) {
        this.tabs = tabs;
        try {
            initProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initChildren(descriptor);
    }

    private void initProperties() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("syntax.properties");
        syntaxProperties = new Properties();
        syntaxProperties.load(in);

        in = getClass().getClassLoader().getResourceAsStream("translator.properties");
        translatorProperties = new Properties();
        translatorProperties.load(in);
    }

    public String getType(PropertyDescriptor propertyDescriptor) {
        String key = String.format("%s%s", PREFIX_GROOVY, propertyDescriptor.getName());
        String property = translatorProperties.getProperty(key);

        if (property == null) {
            return null;
        }

        if (TYPE_INNER.equals(property)) {
            return TYPE_INNER;
        }
        String type = String.format("%s%s", key, SUFIX_GROOVY_TYPE);
        String propertyType = translatorProperties.getProperty(type);

        if (propertyType == null) {
            propertyType = TYPE_METHOD;
        }
        return propertyType;
    }

    public DSLStrategy getStrategyByPropertyDescriptorType(PropertyDescriptor propertyDescriptor) {
        String key = String.format("%s%s", PREFIX_GROOVY, propertyDescriptor.getName());
        String property = translatorProperties.getProperty(key);
        String type = getType(propertyDescriptor);

        if (type == null) {
            return null;
        }

        switch (type) {
            case TYPE_INNER:
                return new DSLInnerStrategy(getTabs(), propertyDescriptor);
            case TYPE_CONST:
                return new DSLConstantStrategy(propertyDescriptor, property);
            case TYPE_OBJECT:
                return new DSLObjectStrategy(getTabs() + 1, propertyDescriptor, property);
            case TYPE_PARAMETER:
                return new DSLParameterStrategy(propertyDescriptor);
            case TYPE_PROPERTY:
                return new DSLPropertyStrategy(getTabs() + 1, propertyDescriptor, property);
            case TYPE_ARRAY:
                return new DSLArrayStrategy(propertyDescriptor);
            default:
                return new DSLMethodStrategy(getTabs() + 1, propertyDescriptor, property);
        }
    }

    protected String getSyntax(String key) {
        return syntaxProperties.getProperty(key);
    }

    @Override
    public List<DSLStrategy> getChildren() {
        return children;
    }

    @Override
    public void addChild(DSLStrategy strategy) {
        children.add(strategy);
    }

    protected void initChildren(IDescriptor descriptor) {
        if (descriptor.getProperties() == null) {
            return;
        }
        for (PropertyDescriptor propertyDescriptor : descriptor.getProperties()) {
            DSLStrategy strategy = getStrategyByPropertyDescriptorType(propertyDescriptor);
            if (strategy != null) {
                addChild(strategy);
            }
        }
    }

    protected List<PropertyDescriptor> getChildrenOfType(PropertyDescriptor parent, String type) {
        List<PropertyDescriptor> selectedPropertyDescription = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : parent.getProperties()) {
            if (type.equals(getType(propertyDescriptor))) {
                selectedPropertyDescription.add(propertyDescriptor);
            }
        }
        return selectedPropertyDescription;
    }

    protected String getChildrenDSL() {
        StringBuilder dsl = new StringBuilder();
        for (DSLStrategy strategy : getChildren()) {
            String strategyDsl = strategy.toDSL();
            dsl.append(strategyDsl);
        }
        return dsl.toString();
    }

    protected String replaceTabs(String dsl, int tabs) {
        return dsl.replaceAll("<t>", getTabsString(tabs));
    }

    public String printValueAccordingOfItsType(String value) {
        if (value == null) {
            return "null";
        }
        if (value.equals("true") || value.equals("false")) {
            return value;
        }
        if (value.matches("[0-9.]+")) {
            return value;
        }
        if (value.contains("\n")) {
            return "\"\"\"" + value + "\"\"\"";
        }
        return "\"" + value + "\"";
    }

    @Override
    public int getTabs() {
        return tabs;
    }

    public void setTabs(int tabs) {
        this.tabs = tabs;
    }

    public String getTabsString(int tabs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tabs; i++) {
            builder.append(syntaxProperties.getProperty("syntax.tab"));
        }
        return builder.toString();
    }
}
