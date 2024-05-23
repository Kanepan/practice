package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.meta.Meta;

import java.util.List;

public class AndMetaData<T extends Meta> extends MultiMetaData<T> {
    AndMetaData(MetaData<T> a, MetaData<T> b) {
        super(a, b);
    }

    public AndMetaData(List<MetaData<T>> list) {
        super(list);
    }

    public MetaData<T> and(MetaData<T> other) {
        add(other);
        return this;
    }
}
