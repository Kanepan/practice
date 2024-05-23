package com.kane.practice.program.logicexecutor.meta;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;

public interface Resource extends Meta {
    Object getResource(LogicContext context);
}
