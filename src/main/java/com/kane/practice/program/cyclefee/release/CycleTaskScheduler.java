package com.kane.practice.program.cyclefee.release;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CycleTaskScheduler {
    private static void generateCycleDetail(CycleConfig config, Date now) {
        if (config.getStartDate().after(now)) {
            System.out.println("周期配置未开始，停止生成周期明细");
            return;
        }
        // 判断配置是否结束
        if (config.getEndDate().before(now)) {
            System.out.println("周期配置已结束，停止生成周期明细");
            return;
        }


        String cycleKey = config.genCycleKey();
        if (config.getCurrentCycle() != null) {
            if (config.isNowInCycle(now)) {
//                System.out.println("当前周期已生成明细，跳过: " + cycleKey);
                return;
            }
        }

        // 生成周期明细
        if (config.getCurrentCycle() == null) {
            config.setCurrentCycle(cycleKey);
            config.initCurrentCycleNUm();
        } else {
            config.updateCurrentCycleStartDate();
            cycleKey = config.genCycleKey();
            config.setCurrentCycle(cycleKey);
            config.increaseCycleNum();
        }

        System.out.println("生成周期明细: " + cycleKey + ", 金额: " + config.getAmount());


    }

    private static Pair<Date, Date> getCurrentCycleRange(CycleConfig config) {
        Date start = config.calCurrentCycleStartDate();
        Date end = config.calCurrentCycleEndDate(start);
        return Pair.of(start, end);
    }


    public static void main(String[] args) {
//        testMonth(true);
//        testYear(false);
        testWeek(false);
//        testDay();

    }


    public static void testMonth(boolean natural) {
        // 初始化配置
        CycleConfig config = new CycleConfig();
        config.setAmount(new BigDecimal("500.00"));
        config.setCycleType(CycleTypeEnum.MONTH); // 支持 YEAR / MONTH / WEEK / DAY
        config.setStartDate(new Date());
        config.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)); // 1年后
        config.setPayNextCycle(false);
        config.setNaturalCycle(natural);

        // 模拟定时任务每日运行
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 15; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }
//        System.out.println("已生成周期数: " + generatedCycles.size());
    }


    public static void testYear(boolean natural) {
        // 初始化配置
        CycleConfig config = new CycleConfig();
        config.setAmount(new BigDecimal("500.00"));
        config.setCycleType(CycleTypeEnum.YEAR); // 支持 YEAR / MONTH / WEEK / DAY
        config.setStartDate(new Date());
        config.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)); // 5年后
        config.setPayNextCycle(false);
        config.setNaturalCycle(natural);

        // 模拟定时任务每日运行
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 456; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

//        System.out.println("已生成周期数: " + generatedCycles.size());
    }

    public static void testWeek(boolean natural) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);

        // 初始化配置
        CycleConfig config = new CycleConfig();
        config.setAmount(new BigDecimal("500.00"));
        config.setCycleType(CycleTypeEnum.WEEK); // 支持 YEAR / MONTH / WEEK / DAY
        config.setStartDate(calendar.getTime());
        config.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)); // 5年后
        config.setPayNextCycle(false);
        config.setNaturalCycle(natural);

        // 模拟定时任务每日运行

        for (int i = 0; i < 8; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

//        System.out.println("已生成周期数: " + generatedCycles.size());
    }


    public static void testDay() {
        // 初始化配置
        CycleConfig config = new CycleConfig();
        config.setAmount(new BigDecimal("500.00"));
        config.setCycleType(CycleTypeEnum.DAY); // 支持 YEAR / MONTH / WEEK / DAY
        config.setStartDate(new Date());
        config.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)); // 5年后
        config.setPayNextCycle(false);
        config.setNaturalCycle(true);

        // 模拟定时任务每日运行
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 10; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

//        System.out.println("已生成周期数: " + generatedCycles.size());
    }
}
