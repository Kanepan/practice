package com.kane.practice.program.logicexecutor.meta;

public interface Meta {
    String version = "1.0.0";

    default String getMetaName() {
        return this.getClass().getSimpleName();
    }


}
