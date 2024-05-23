package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.meta.Meta;

public class PairMetaData<T extends Meta> extends BaseMetaData<T> {
    protected MetaData<T> conditon;
    protected MetaData<T> action;

    public PairMetaData(MetaData<T> conditon, MetaData<T> action) {
        super(null,null);
        if (conditon == null || action == null) {
            throw new IllegalArgumentException("需要两个不为 null 的元素才能完成组合");
        }
        this.conditon = conditon;
        this.action = action;
    }

    public MetaData<T> getConditon() {
        return conditon;
    }

    public MetaData<T> getAction() {
        return action;
    }
}
