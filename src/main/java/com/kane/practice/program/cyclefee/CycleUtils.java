package com.kane.practice.program.cyclefee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CycleUtils {
    /**
     * 获取当前日期的 "年-月" 编号 (如: 2024-11)
     */
    public static String getYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    /**
     * 获取当前日期的 "年-周" 编号 (如: 2024-46)
     */
    public static String getYearWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // 设置周一为一周的第一天
        calendar.setTime(date);

//        int year = calendar.get(Calendar.YEAR);
//        int week = calendar.get(Calendar.WEEK_OF_YEAR);
//
//        // 修正跨年问题
//        if (week == 1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
//            year += 1;
//        }
//        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY && week >= 52) {
//            year -= 1;
//        }
//
//        return String.format("%d-%02d", year, week);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-WW");
        return sdf.format(date);
    }

    /**
     * 获取当前日期的 "年-月-日" 编号 (如: 2024-11-17)
     */
    public static String getYearDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取当前日期的 "年" 编号 (如: 2024)
     */
    public static String getYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 获取当前周的起始日期（周一）
     */
    public static Date getStartOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /**
     * 获取当前周的结束日期（周日）
     */
    public static Date getEndOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }
}
