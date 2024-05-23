package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;

public interface MetaData<T extends Meta> {
    T getMetaDef();

    Param[] getParams();

    void setParams(Param[] params);

    MetaData<T> and(MetaData<T> other);

    /**
     * 组装为逻辑或的关系
     *
     * @param other
     * @return 组装后的元数据
     */
    MetaData<T> or(MetaData<T> other);

    /**
     * 组装为逻辑非的关系
     *
     * @return 组装后的元数据
     */
    MetaData<T> not();
}
