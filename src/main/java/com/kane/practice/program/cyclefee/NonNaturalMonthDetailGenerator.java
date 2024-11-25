package com.kane.practice.program.cyclefee;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class NonNaturalMonthDetailGenerator {
    private static Set<String> generatedCycles = new HashSet<>();

    /**
     * 生成非自然月的周期款明细
     */
    public static void generateNonNaturalCycleDetail(CustomCycleConfig config, Date now) {
        Date[] cycleRange = NonNaturalMonthUtils.calculateCustomMonthRange(config.getCurrentStartDate());

        Date periodStart = cycleRange[0];
        Date periodEnd = cycleRange[1];


        // 判断是否是当前周期
        if (now.before(periodStart) || now.after(periodEnd)) {
            //新周期
            config.setCurrentStartDate(now);
            cycleRange = NonNaturalMonthUtils.calculateCustomMonthRange(config.getCurrentStartDate());
            periodStart = cycleRange[0];
            periodEnd = cycleRange[1];
        }

        String cycleKey = periodStart.toString() + "_" + periodEnd.toString();

        if (generatedCycles.contains(cycleKey)) {
//            System.out.println("周期已生成明细，跳过: " + cycleKey);
            return;
        }

        // 生成周期明细
        System.out.printf("生成周期款明细: 周期[%s ~ %s], 金额: %s%n",
                periodStart, periodEnd, config.getAmount());

        // 记录已生成周期
        generatedCycles.add(cycleKey);
    }

    public static void main(String[] args) {
        // 配置非自然月
        CustomCycleConfig config = new CustomCycleConfig();
        config.setAmount(new BigDecimal("1000.00"));
        config.setCycleType("MONTH");
        config.setCustomStartDate(new Date()); // 例如 15 日为起点
        Calendar calendar = Calendar.getInstance();
        config.setCurrentStartDate(config.getCustomStartDate());
        // 模拟每日运行
        for (int i = 0; i < 80; i++) { // 模拟 60 天

            generateNonNaturalCycleDetail(config, calendar.getTime());
            // 模拟日期变化

            calendar.add(Calendar.DATE, 1);
//            config.setCustomStartDate(calendar.getTime());
        }
    }
}
