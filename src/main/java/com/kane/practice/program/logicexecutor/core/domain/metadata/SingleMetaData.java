package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;

public class SingleMetaData<T extends Meta> extends BaseMetaData<T> {
//    private T meta;

    protected Param[] params;

    public SingleMetaData(T meta, Param[] params) {
        super(meta, params);
//        this.meta = meta;
    }

//    public Meta getMeta() {
//        return meta;
//    }

}
