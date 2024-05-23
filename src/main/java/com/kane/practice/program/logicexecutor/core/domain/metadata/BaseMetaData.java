package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import lombok.Data;

@Data
public abstract class BaseMetaData<T extends Meta> implements MetaData<T> {

    protected T def;

    protected Param[] params;

    public BaseMetaData(T def, Param[] params) {
        this.def = def;
        this.params = params;
    }

    public Param[] getParams() {
        return params;
    }

    public void setParams(Param[] params) {
        this.params = params;
    }

    public T getMetaDef() {
        return def;
    }


    @Override
    public MetaData<T> and(MetaData<T> other) {
        return new AndMetaData<>(this, other);
    }


    @Override
    public MetaData<T> or(MetaData<T> other) {
        return new OrMetaData<>(this, other);
    }

    @Override
    public MetaData<T> not() {
        return new NotMetaData<>(this);
    }

}
