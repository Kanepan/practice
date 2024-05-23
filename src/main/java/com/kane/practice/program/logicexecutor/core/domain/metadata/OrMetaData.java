package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.meta.Meta;

import java.util.List;

public class OrMetaData<T extends Meta> extends MultiMetaData<T> {
    OrMetaData(MetaData<T> a, MetaData<T> b) {
        super(a, b);
    }

    public OrMetaData(List<MetaData<T>> list) {
        super(list);
    }

    @Override
    public MetaData<T> or(MetaData<T> other) {
        add(other);
        return this;
    }

}
