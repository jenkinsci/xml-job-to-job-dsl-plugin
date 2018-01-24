package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DSLJobStrategy extends AbstractDSLStrategy {

    public DSLJobStrategy(JobDescriptor jobDescriptor) {
        super(0, jobDescriptor, false);

        checkNeedsBlockAndCreateIt(jobDescriptor, "configure", DSLStrategyFactory.TYPE_CONFIGURE);
        checkNeedsBlockAndCreateIt(jobDescriptor, "properties", DSLStrategyFactory.TYPE_PROPERTIES);
        initChildren(jobDescriptor);
    }

    private void checkNeedsBlockAndCreateIt(JobDescriptor jobDescriptor, String blockName, String blockType) {
        List<PropertyDescriptor> configureBlocks = findConfigureBlockStrategyProperty(jobDescriptor.getProperties(), blockType);
        if (configureBlocks.size() > 0) {
            PropertyDescriptor configureBlockProperty = new PropertyDescriptor(blockName, null, configureBlocks);
            jobDescriptor.getProperties().add(configureBlockProperty);
        }
    }

    private List<PropertyDescriptor> findConfigureBlockStrategyProperty(List<PropertyDescriptor> properties, String blockType) {
        List<PropertyDescriptor> configureBlocksList = new ArrayList<>();
        Iterator<PropertyDescriptor> iterator = properties.iterator();
        while (iterator.hasNext()) {
            PropertyDescriptor descriptor = iterator.next();
            String type = getType(descriptor);
            if (blockType.equals(type)) {
                configureBlocksList.add(descriptor);
                iterator.remove();
            }
            if (descriptor.getProperties() != null && descriptor.getProperties().size() > 0) {
                configureBlocksList.addAll(findConfigureBlockStrategyProperty(descriptor.getProperties(), blockType));
            }
        }

        return configureBlocksList;
    }

    @Override
    public String toDSL() {
        return String.format(getSyntax("syntax.job"),
                getProperty(getDescriptor().getProperties().get(0)).getValue(),
                getDescriptor().getName(), getChildrenDSL());
    }
}
