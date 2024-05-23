package com.kane.practice.program.logicexecutor.meta.condition;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Condition;

public class OverSumCondition implements Condition {
//    @Override
//    public String getMetaName() {
//        return "";
//    }

    @Override
    public boolean execute(LogicContext context) {
        Integer sum = (Integer) context.getResource("OrderSumResource");
        Integer overSum = (Integer) context.getParam("overSum");

        boolean result = false;
        if (sum >= overSum) {
            result = true;
        }

//        boolean result = RandomUtils.nextBoolean();
        System.out.println("OverSumCondition execute: " + result);
        return result;
    }
}
