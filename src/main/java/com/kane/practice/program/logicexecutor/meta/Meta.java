package com.kane.practice.program.logicexecutor.meta;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;

public interface Meta {
    default String getMetaName() {
        return this.getClass().getSimpleName();
    }

}
