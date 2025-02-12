package com.kane.practice.program.cyclefee.release3;

import cn.hutool.core.date.DateField;
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

    public DateField getUnit() {
        switch (this) {
            case DAY:
                return DateField.DAY_OF_YEAR;
            case WEEK:
                return DateField.WEEK_OF_YEAR;
            case MONTH:
                return DateField.MONTH;
            case YEAR:
                return DateField.YEAR;
            default:
                return DateField.DAY_OF_YEAR;
        }
    }
}
