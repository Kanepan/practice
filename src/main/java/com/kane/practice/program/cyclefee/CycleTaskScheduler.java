package com.kane.practice.program.cyclefee;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CycleTaskScheduler {

    private static Set<String> generatedCycles = new HashSet<>(); // 存储已生成的周期标识

    /**
     * 根据周期类型生成明细
     */
    public static void generateCycleDetail(CycleConfig config, Date now) {
        if (now == null) {
            now = new Date();
        }

        // 判断配置是否结束
        if (config.getEndDate().before(now)) {
            System.out.println("周期配置已结束，停止生成周期明细");
            return;
        }

        String cycleKey = null;

        // 根据周期类型生成周期标识
        switch (config.getCycleType()) {
            case "YEAR":
                cycleKey = CycleUtils.getYear(now);
                break;
            case "MONTH":
                cycleKey = CycleUtils.getYearMonth(now);
                break;
            case "WEEK":
                cycleKey = CycleUtils.getYearWeek(now);
                break;
            case "DAY":
                cycleKey = CycleUtils.getYearDay(now);
                break;
            default:
                throw new IllegalArgumentException("Unsupported cycle type: " + config.getCycleType());
        }

        // 检查是否已生成
        if (generatedCycles.contains(cycleKey)) {
//            System.out.println("当前周期已生成明细，跳过: " + cycleKey);
            return;
        }

        // 生成周期明细
        String periodDesc = null;
        if ("WEEK".equals(config.getCycleType())) {
            Date startOfWeek = CycleUtils.getStartOfWeek(now);
            Date endOfWeek = CycleUtils.getEndOfWeek(now);
            periodDesc = String.format("周期[%s ~ %s]",
                    new SimpleDateFormat("yyyy-MM-dd").format(startOfWeek),
                    new SimpleDateFormat("yyyy-MM-dd").format(endOfWeek));
        } else {
            periodDesc = "周期[" + cycleKey + "]";
        }

        System.out.printf("生成周期明细: %s, 金额: %s%n", periodDesc, config.getAmount());
        System.out.println("当前时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));

        // 记录已生成的周期
        generatedCycles.add(cycleKey);
        config.setCurrentCycle(cycleKey);
    }

    public static void main(String[] args) {
        // 初始化配置
        CycleConfig config = new CycleConfig();
        config.setAmount(new BigDecimal("500.00"));
        config.setCycleType("WEEK"); // 支持 YEAR / MONTH / WEEK / DAY
        config.setStartDate(new Date());
        config.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 5)); // 1年后

        // 模拟定时任务每日运行
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 15; i++) { // 模拟10天运行
//            System.out.println("模拟日期: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            generateCycleDetail(config, calendar.getTime());

            // 模拟日期变化（每天+1天）
            calendar.add(Calendar.DATE, 1);
        }

        System.out.println("已生成周期数: " + generatedCycles.size());
    }
}
