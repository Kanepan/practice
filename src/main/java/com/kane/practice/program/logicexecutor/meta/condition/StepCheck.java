package com.kane.practice.program.logicexecutor.meta.condition;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Condition;

public class StepCheck implements Condition {
    @Override
    public boolean execute(LogicContext context) {
        Integer limit = (Integer) context.getParam("stepCheck");
        Integer orderSum = (Integer) context.getResource("OrderSumResource");
        if (limit <= 0) {
            return false;
        }

        if (isMultiple(orderSum, limit)) {
            return true;
        }
        return false;
    }


    public boolean isMultiple(int a, int b) {

        if (b == 0) {
            throw new IllegalArgumentException("除数不能为零");
        }
        return a % b == 0;
    }


}
