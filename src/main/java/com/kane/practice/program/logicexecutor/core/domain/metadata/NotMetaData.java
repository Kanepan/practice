package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.meta.Meta;

public class NotMetaData<T extends Meta> extends MultiMetaData<T> {
    protected MetaData<T> meta;

    public NotMetaData(MetaData<T> meta) {
        super(null);
        this.meta = meta;
    }

    @Override
    public MetaData<T> not() {
        return meta;
    }

    public MetaData<T> getMetaData() {
        return meta;
    }


}
