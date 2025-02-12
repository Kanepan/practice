package com.kane.practice.program.cyclefee;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDifferenceCalculator {

    // 计算【非自然年】（基于 startDate 的周年）
    public static int getNonNaturalYearDiff(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int years = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);

        if (endCal.get(Calendar.MONTH) < startCal.get(Calendar.MONTH) ||
                (endCal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH) && endCal.get(Calendar.DAY_OF_MONTH) < startCal.get(Calendar.DAY_OF_MONTH))) {
            years--;
        }
        return years;
    }

    // 计算【自然年】（直接取年份差）
//    public static int getNaturalYearDiff(Date startDate, Date endDate) {
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        return endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);
//    }

    public static int getNaturalYearDiff(Date startDate, Date endDate) {
        return (int) DateUtil.betweenYear(startDate, endDate, true);
    }

    // 计算【非自然月】（基于 startDate）
    public static int getNonNaturalMonthDiff(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int months = (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 +
                (endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH));

        if (endCal.get(Calendar.DAY_OF_MONTH) < startCal.get(Calendar.DAY_OF_MONTH)) {
            months--;
        }
        return months;
    }

    // 计算【自然月】（直接计算年*12+月的差）
//    public static int getNaturalMonthDiff(Date startDate, Date endDate) {
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        return (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 +
//                (endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH));
//    }

    public static int getNaturalMonthDiff(Date startDate, Date endDate) {
        return (int) DateUtil.betweenMonth(startDate, endDate, true);
    }

    // 计算【非自然周】（基于 startDate，每7天为一周）
    public static int getNonNaturalWeekDiff(Date startDate, Date endDate) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24 * 7));
    }

    // 计算【自然周】（以星期一为基准计算跨周数）
//    public static int getNaturalWeekDiff(Date startDate, Date endDate) {
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 归到最近的星期一
//
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        endCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 归到最近的星期一
//
//        long diffInMillies = endCal.getTimeInMillis() - startCal.getTimeInMillis();
//        return (int) (diffInMillies / (1000 * 60 * 60 * 24 * 7));
//    }

    public static int getNaturalWeekDiff(Date startDate, Date endDate) {
        return (int) DateUtil.between(startDate, endDate, DateUnit.WEEK, true);
    }

    // 计算【非自然日】
    public static int getNonNaturalDayDiff(Date startDate, Date endDate) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }

    // 计算【自然日】（同非自然日）
    public static int getNaturalDayDiff(Date startDate, Date endDate) {
        return (int) DateUtil.between(startDate, endDate, DateUnit.DAY, true);
    }

//    public static int getNaturalDayDiff(Date startDate, Date endDate) {
//        return getNonNaturalDayDiff(startDate, endDate);
//    }



    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse("2025-02-12");
        Date endDate = sdf.parse("2026-02-10");

        System.out.println("非自然年相差: " + getNonNaturalYearDiff(startDate, endDate) + " 年");
        System.out.println("自然年相差: " + getNaturalYearDiff(startDate, endDate) + " 年");

        System.out.println("非自然月相差: " + getNonNaturalMonthDiff(startDate, endDate) + " 个月");
        System.out.println("自然月相差: " + getNaturalMonthDiff(startDate, endDate) + " 个月");

        System.out.println("非自然周相差: " + getNonNaturalWeekDiff(startDate, endDate) + " 周");
        System.out.println("自然周相差: " + getNaturalWeekDiff(startDate, endDate) + " 周");

        System.out.println("非自然日相差: " + getNonNaturalDayDiff(startDate, endDate) + " 天");
        System.out.println("自然日相差: " + getNaturalDayDiff(startDate, endDate) + " 天");
    }
}
