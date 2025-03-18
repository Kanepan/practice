package com.kane.practice.program.cyclefee.release3;

import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CycleTaskScheduler {
    private static void generateCycleDetail(CycleConfig config, Date now) {
        if (config.getStartDate().after(now)) {
            System.out.println("周期配置未开始，停止生成周期明细");
            return;
        }
        if (config.getEndDate().before(now)) {
            System.out.println("周期配置已结束，停止生成周期明细");
            return;
        }
        long calCyleNum = 0;
        String cycleKey = null;
        if (config.getCurrentCycle() != null) {
            if (config.isBeforeCurrentCycle(now)) {
//                System.out.println("当前周期已生成明细，跳过: " + cycleKey);
                return;
            }

            calCyleNum = config.calCycleNum(now);
            if (calCyleNum <= config.getCurrentCycleNum().longValue()) {
//                System.out.println("当前周期已生成明细，跳过: " + cycleKey);
                return;
            }

            Date currentStart = config.updateCurrentCycleStartDate(now);
            if (currentStart == null) {
                System.out.println("周期配置已结束，停止生成周期明细");
                return;
            }
            cycleKey = config.genCycleKey(now);

            if (!config.canGenCycle(now)) {
                return;
            }
        } else {
            if (config.isPayNextCycle()) {
                // 判断是不是相差一个周期如果不是 则不生成
                Date firstCycleStart = config.calCurrentCycleStartDate(config.getStartDate());
                Date endCycleStart = config.calCurrentCycleEndDate(firstCycleStart);

                if (endCycleStart.after(now)) {
//                    System.out.println("下周周期才执行");
                    return;
                }
                // 更新起点为下一个周期的起点
                cycleKey = config.genCycleKey(now);
            } else {
                cycleKey = config.genCycleKey(now);
            }

            if (!config.canGenCycle(now)) {
                return;
            }

            calCyleNum = config.calCycleNum(now);

        }


        config.setCurrentCycle(cycleKey);
        config.setCurrentCycleNum((int)calCyleNum);

        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(now));
        System.out.println("生成周期明细: " + cycleKey + ", 金额: " + config.getAmount() + ", 当前周期数: " + config.getCurrentCycleNum() + " 当前周几开始时间" + config.getCurrentCycleStartDate());

    }

    public static void main(String[] args) {
        testMonth(false);
//        testYear(false);
//        testYear2(true);
//        testWeek(true);
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
        for (int i = 0; i < 100; i++) { // 模拟10天运行
            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
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
        for (int i = 0; i < 3000; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

        // 模拟日期变化（每天+1天）
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 350);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 30);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        // 模拟日期变化（每天+1天）
//        calendar.add(Calendar.DATE, 320);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 1);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());


//        System.out.println("已生成周期数: " + generatedCycles.size());
    }


    public static void testYear2(boolean natural) {
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
        for (int i = 0; i < 2000; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());
            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

        // 模拟日期变化（每天+1天）
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 290);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 30);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        // 模拟日期变化（每天+1天）
//        calendar.add(Calendar.DATE, 310);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());
//
//        calendar.add(Calendar.DATE, 1);
//        System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        generateCycleDetail(config, calendar.getTime());


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


        calendar.add(Calendar.DATE, 2);
        for (int i = 0; i < 15; i++) { // 模拟10天运行
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
