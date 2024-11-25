package com.kane.practice.program.cyclefee.release;

import lombok.Getter;

@Getter
public enum CycleTypeEnum {
    DAY(1, "按日"),
    WEEK(2, "按周"),
    MONTH(3, "按月"),
    YEAR(4, "按年"),
    ;

    Integer value;
    String desc;

    CycleTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
