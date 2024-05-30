package com.kane.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

public class Test10 {
    public static void main(String[] args) {

            DateTime startTime1 = DateUtil.parse("2024-05-24 10:00:00");
            DateTime endTime1 = DateUtil.parse("2024-05-24 12:00:00");

            DateTime startTime2 = DateUtil.parse("2024-05-24 11:00:00");
            DateTime endTime2 = DateUtil.parse("2024-05-24 13:00:00");

            boolean hasOverlap = isTimeIntervalOverlap(startTime1, endTime1, startTime2, endTime2);
            System.out.println("时间区间是否有交集: " + hasOverlap);


    }

    public static boolean isTimeIntervalOverlap(DateTime startTime1, DateTime endTime1, DateTime startTime2, DateTime endTime2) {
        // 判断是否有交集
        return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
    }
}
