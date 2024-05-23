package com.kane.practice.program.logicexecutor.core.domain.param;

import lombok.Data;

@Data
public class DefaultParam implements Param {
    private String name;
    protected Object value;

    /**
     * 参数值的来源类型
     */
    protected Param.Type type;

    public DefaultParam(
            String name,
            Param.Type type,
            Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public DefaultParam(
            String name,
            Param.Type type) {
        this.name = name;
        this.type = type;
    }
}
