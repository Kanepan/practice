package com.kane.practice.program.logicexecutor.meta;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;

public interface Action extends Meta{
    boolean execute(LogicContext context);
}
