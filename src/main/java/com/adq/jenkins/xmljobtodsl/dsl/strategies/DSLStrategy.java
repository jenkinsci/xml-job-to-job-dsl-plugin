package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import java.util.List;

public interface DSLStrategy {

    List<DSLStrategy> getChildren();

    void addChild(DSLStrategy strategy);

    String toDSL();

    int getTabs();
}
