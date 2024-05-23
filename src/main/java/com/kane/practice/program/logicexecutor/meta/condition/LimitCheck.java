package com.kane.practice.program.logicexecutor.meta.condition;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Condition;

public class LimitCheck implements Condition {


    @Override
    public boolean execute(LogicContext context) {
        Integer limit = (Integer) context.getParam("limitCheck");
        Integer orderSum = (Integer) context.getResource("OrderSumResource");
        if (orderSum > limit) {
            return false;
        }
        return true;
    }
}
