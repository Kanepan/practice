package com.kane.practice.program.logicexecutor.core.domain.param;

public interface Param {
    public enum Type {
        /**
         * 已定义参数值
         */
        DEFINED,

        /**
         * 资源型元数据的参数值
         */
        RESOURCE,

        /**
         * 工具组装时未定义的参数值，在实例化时定义
         */
        SETTING;
    }

    Type getType();

    String getName();

    Object getValue();

}
