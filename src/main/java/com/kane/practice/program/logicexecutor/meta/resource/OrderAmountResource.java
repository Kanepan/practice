package com.kane.practice.program.logicexecutor.meta.resource;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Resource;

public class OrderAmountResource implements Resource {
    @Override
    public Object getResource(LogicContext context) {
        return context.getAmount();
    }
}
