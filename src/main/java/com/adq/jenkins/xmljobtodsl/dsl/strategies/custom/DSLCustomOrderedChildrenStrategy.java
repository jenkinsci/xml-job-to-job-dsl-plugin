package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.IValueStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;

public class DSLCustomOrderedChildrenStrategy extends DSLMethodStrategy {
    private final String methodName;

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
}
