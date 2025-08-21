package com.kane.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test10 {
    public static void main(String[] args) {

        DateTime startTime1 = DateUtil.parse("2024-05-24 10:00:00");
        DateTime endTime1 = DateUtil.parse("2024-05-24 12:00:00");

        DateTime startTime2 = DateUtil.parse("2024-05-24 11:00:00");
        DateTime endTime2 = DateUtil.parse("2024-05-24 13:00:00");

        boolean hasOverlap = isTimeIntervalOverlap(startTime1, endTime1, startTime2, endTime2);
        System.out.println("时间区间是否有交集: " + hasOverlap);


        testDateRange();

        String dateStr = "2024-05";
        Date startTime = DateUtil.parse(dateStr, DatePattern.NORM_MONTH_PATTERN);
        Date endTime = DateUtil.offsetMonth(startTime, 1);

        System.out.println("开始时间: " + DateUtil.format(startTime, DatePattern.NORM_DATETIME_FORMAT));
        System.out.println("结束时间: " + DateUtil.format(endTime, DatePattern.NORM_DATETIME_FORMAT));


        Date date =  new Date();
        Date endDate = DateUtil.offset(date, DateField.YEAR,1);
        System.out.println("end时间: " +endDate);

    }

    public static boolean isTimeIntervalOverlap(DateTime startTime1, DateTime endTime1, DateTime startTime2, DateTime endTime2) {
        // 判断是否有交集
        return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
    }


    public static void testDateRange() {
        List<String> billingDateList = new ArrayList<>();

        YearMonth start = YearMonth.parse("2025-01"); // 开始月份
        YearMonth now = YearMonth.now();

        while (!start.isAfter(now)) {
            billingDateList.add(start.toString()); // 格式为 yyyy-MM
            start = start.plusMonths(1); // 下一个月
        }
        System.out.println(billingDateList);
    }
}
