package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLMethodStrategy extends AbstractDSLStrategy {

    private final String methodName;

    public DSLMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, shouldInitChildren);
        this.methodName = methodName;
        this.setTabs(tabs);
    }

    public DSLMethodStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        this(tabs, propertyDescriptor, methodName, true);
    }

    public DSLMethodStrategy(PropertyDescriptor descriptor) {
        this(0, descriptor, null, true);
    }

	@Override
    public String toDSL() {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        if (propertyDescriptor.getValue() != null) {

            boolean isParentAMethod = propertyDescriptor.getParent() != null &&
                    getType(propertyDescriptor.getParent()).equals(DSLStrategyFactory.TYPE_METHOD);

            if (isParentAMethod) {
                return getStrategyForObject(propertyDescriptor).toDSL();
            }

            return replaceTabs(String.format(getSyntax("syntax.method_call"),
                    methodName, printValueAccordingOfItsType(propertyDescriptor.getValue())), getTabs());
        }

        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, getChildrenDSL()), getTabs());
    }

    protected DSLStrategy getStrategyForObject(PropertyDescriptor propertyDescriptor) {
        List<PropertyDescriptor> siblings = getChildrenOfType(propertyDescriptor.getParent(), DSLStrategyFactory.TYPE_METHOD);

        propertyDescriptor.getParent().getProperties().clear();

        List<PropertyDescriptor> children = new ArrayList<>();
        for (PropertyDescriptor descriptor : siblings) {
            children.add(new PropertyDescriptor(descriptor.getName(), null,
                    descriptor.getValue(), descriptor.getProperties(),
                    descriptor.getAttributes()));
        }
        PropertyDescriptor object = new PropertyDescriptor(null, null, children);
        return new DSLObjectStrategy(getTabs(), object, null);
    }

    @Override
    protected String getChildrenDSL() {
        StringBuilder dsl = new StringBuilder();

        int size = getChildren().size();

        for (int index = 0; index < size; index++) {
            DSLStrategy strategy = getChildren().get(index);

            // check if strategy is "viable" ie not an INNER
            if(strategy instanceof IValueStrategy){
                String strategyDsl = strategy.toDSL();
                dsl.append(strategyDsl);
                if (index < size - 1) {
                    dsl.append(", ");
                }
            } else {
                // check for viable children
                ArrayList<DSLStrategy> viableChildren = new ArrayList<>();
                viableChildren.addAll(findViableChildren(strategy));
                int viableChildrenSize = viableChildren.size();

                for(int j = 0; j < viableChildrenSize; j++){
                    DSLStrategy viableChild = viableChildren.get(j);
                    String viableChildDsl = viableChild.toDSL();
                    dsl.append(viableChildDsl);
                    if(index < size - 1 || j < viableChildrenSize - 1){
                        dsl.append(", ");
                    }
                }
            }
        }
        return dsl.toString();
    }

    protected ArrayList<DSLStrategy> findViableChildren(DSLStrategy strategy){
        ArrayList<DSLStrategy> viableChildren = new ArrayList<>();
        List<DSLStrategy> children = strategy.getChildren();

        for(DSLStrategy child : children){
            if(child instanceof IValueStrategy && child.getChildren().size() == 0){
                viableChildren.add(child);
            } else {
                viableChildren.addAll(findViableChildren(child));
            }
        }

        return viableChildren;
    }
}
