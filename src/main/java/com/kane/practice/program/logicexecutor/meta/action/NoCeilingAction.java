package com.kane.practice.program.logicexecutor.meta.action;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Action;

public class NoCeilingAction implements Action {
    @Override
    public boolean execute(LogicContext context) {
        Integer num = context.getOrderSum();
        context.setMultiple(num.intValue());
        return true;
    }
}
