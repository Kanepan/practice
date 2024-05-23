package com.kane.practice.program.logicexecutor.meta.condition;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Condition;

import java.math.BigDecimal;

public class OverAmountCondition implements Condition {

    @Override
    public boolean execute(LogicContext context) {
        BigDecimal amount = (BigDecimal) context.getResource("OrderAmountResource");
        BigDecimal overAmount = (BigDecimal) context.getParam("overAmount");

        boolean result = false;
        if (amount.compareTo(overAmount) >= 0) {
            result = true;
        }

        System.out.println("OverAmountCondition execute: " + result);
        return result;
    }
}
