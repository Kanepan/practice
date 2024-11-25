package com.kane.practice.program.cyclefee;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class NonNaturalMonthUtils {
    public static Date[] calculateCustomMonthRange(Date startDate) {
        // 周期开始时间
        Date periodStart = DateUtil.beginOfDay(startDate);
        // 周期结束时间
        Date periodEnd = DateUtil.offsetMonth(periodStart, 1);

        return new Date[]{periodStart, periodEnd};
    }
}
