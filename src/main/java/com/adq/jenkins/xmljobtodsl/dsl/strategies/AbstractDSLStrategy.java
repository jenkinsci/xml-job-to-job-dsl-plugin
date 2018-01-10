package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.IDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class AbstractDSLStrategy implements DSLStrategy {

    public static final String SUFIX_GROOVY_TYPE = ".type";

    private Properties syntaxProperties;
    private Properties translatorProperties;

    private List<DSLStrategy> children = new ArrayList<>();

    private int tabs = 0;

    private List<PropertyDescriptor> notTranslatedList = new ArrayList<>();
    private IDescriptor propertyDescriptor;

    public AbstractDSLStrategy(IDescriptor descriptor) {
        this(0, descriptor, true);
    }

    public AbstractDSLStrategy(int tabs, IDescriptor descriptor) {
        this(tabs, descriptor, true);
    }

    public AbstractDSLStrategy(int tabs, IDescriptor descriptor, boolean shouldInitChildren) {
        this.tabs = tabs;
        this.propertyDescriptor = descriptor;
        try {
            initProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (shouldInitChildren) {
            initChildren(descriptor);
        }
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
        Pair property = getProperty(propertyDescriptor);

        if (property.getValue() == null) {
            return null;
        }

        if (DSLStrategyFactory.TYPE_INNER.equals(property.getValue())) {
            return DSLStrategyFactory.TYPE_INNER;
        }

        String type = String.format("%s%s", property.getKey(), SUFIX_GROOVY_TYPE);
        String propertyType = translatorProperties.getProperty(type);

        if (propertyType == null) {
            propertyType = DSLStrategyFactory.TYPE_METHOD;
        }
        return propertyType;
    }

    protected Pair<String, String> getProperty(PropertyDescriptor propertyDescriptor) {
        String property = null;
        String key = null;
        if (propertyDescriptor.getParent() != null) {
            key = String.format("%s.%s", propertyDescriptor.getParent().getName(), propertyDescriptor.getName());
            property = translatorProperties.getProperty(key);
        }
        if (property == null) {
            key = propertyDescriptor.getName();
            property = translatorProperties.getProperty(key);
        }
        return new Pair(key, property);
    }

    protected String getPropertyByName(String name) {
        return translatorProperties.getProperty(name);
    }

    public DSLStrategy getStrategyByPropertyDescriptorType(PropertyDescriptor propertyDescriptor) {
        String type = getType(propertyDescriptor);

        if (type == null) {
            notTranslatedList.add(propertyDescriptor);
            return null;
        }

        String property = getProperty(propertyDescriptor).getValue();
        return new DSLStrategyFactory().getDSLStrategy(type, propertyDescriptor, property, getTabs() + 1);
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
        if (descriptor == null || descriptor.getProperties() == null) {
            return;
        }
        children.clear();
        List<PropertyDescriptor> properties = descriptor.getProperties();
        Iterator<PropertyDescriptor> iterator = properties.iterator();

        while (iterator.hasNext()) {
            PropertyDescriptor propertyDescriptor = iterator.next();
            DSLStrategy strategy = getStrategyByPropertyDescriptorType(propertyDescriptor);
            if (strategy != null) {
                addChild(strategy);
                notTranslatedList.addAll(strategy.getNotTranslatedList());
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
        value = value.replaceAll("\\\\", "\\\\\\\\");
        if (value.contains("${") && value.contains("}")) {
            return "'" + value + "'";
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

    public List<PropertyDescriptor> getNotTranslatedList() {
        return notTranslatedList;
    }

    public void setDescriptor(IDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public IDescriptor getDescriptor() {
        return propertyDescriptor;
    }
}
