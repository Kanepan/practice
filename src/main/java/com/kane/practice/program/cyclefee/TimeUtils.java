package com.kane.practice.program.cyclefee;

import cn.hutool.core.date.DateUtil;
import com.kane.practice.program.cyclefee.release3.CycleTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class TimeUtils {
    private boolean naturalCycle = false;
    private CycleTypeEnum cycleType = CycleTypeEnum.YEAR;
    private Date startDate;

    public static void main(String[] args) {
        TimeUtils timeUtils = new TimeUtils();
        timeUtils.setStartDate(DateUtil.parse("2025-02-12"));

        System.out.println(timeUtils.calCycleNum(DateUtil.parse("2025-03-13")));

        System.out.println(timeUtils.calCycleNum(DateUtil.parse("2026-02-10")));

        System.out.println(timeUtils.calCycleNum(DateUtil.parse("2026-02-17")));

        System.out.println(timeUtils.calCycleNum(DateUtil.parse("2027-01-13")));

        System.out.println(timeUtils.calCycleNum(DateUtil.parse("2027-02-14")));
    }

    public long calCycleNum(Date now) {
        long cycleNum = 0;
        Date firstCycleStart = null;
        if (naturalCycle) {
            // 自然周期的起点
            if (cycleType == CycleTypeEnum.DAY) {
                firstCycleStart = DateUtil.beginOfDay(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalDayDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.WEEK) {
                firstCycleStart = DateUtil.beginOfWeek(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalWeekDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.MONTH) {
                firstCycleStart = DateUtil.beginOfMonth(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalMonthDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.YEAR) {
                firstCycleStart = DateUtil.beginOfYear(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalYearDiff(startDate, now);
            } else {
                throw new RuntimeException("不支持的周期类型");
            }
        } else {
            // 非自然周期的起点
//            firstCycleStart = DateUtil.beginOfDay(date);
            firstCycleStart = DateUtil.beginOfDay(startDate);
            if (cycleType == CycleTypeEnum.DAY) {
                cycleNum = DateDifferenceCalculator.getNonNaturalDayDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.WEEK) {
                cycleNum = DateDifferenceCalculator.getNonNaturalWeekDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.MONTH) {
                //计算 firstCycleStart 和 now 之间相差的月份，非自然年
                cycleNum = DateDifferenceCalculator.getNonNaturalMonthDiff(startDate, now);
            } else if (cycleType == CycleTypeEnum.YEAR) {
                //计算 firstCycleStart 和 now 之间相差的年份，非自然年

//                long diffInMs = now.getTime() - firstCycleStart.getTime();
//                // 转换为天，保留正负值
//                cycleNum = diffInMs / (DateUnit.DAY.getMillis() * 365);
                cycleNum = DateDifferenceCalculator.getNonNaturalYearDiff(startDate, now);
            }
        }
        return ++cycleNum;
    }


}
