package com.kane.practice.program.logicexecutor.utils;

import java.math.BigDecimal;
import java.util.Date;

public enum ValueType {
    String(String.class, 0),
    Date(Date.class, 1),
    Boolean(Boolean.class, 2),
    Double(Double.class, 3),
    Long(Long.class, 4),
    Resource(Object.class, 5),
    Integer(Integer.class, 6),
    BigDecimal(BigDecimal.class, 7)
    ;


    private final Class type;
    private final int index;

    private ValueType(Class type, int index) {
        this.type = type;
        this.index = index;
    }

    public final Class getType() {
        return type;
    }

    public final int getIndex() {
        return index;
    }
}
