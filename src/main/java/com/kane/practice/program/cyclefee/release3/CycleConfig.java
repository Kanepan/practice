package com.kane.practice.program.cyclefee.release3;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.kane.practice.program.cyclefee.DateDifferenceCalculator;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Data
public class CycleConfig {
    private BigDecimal amount; // 周期金额
    private CycleTypeEnum cycleType; // 周期类型：DAY, WEEK, MONTH, YEAR
    private Date startDate;   // 周期起始日期
    private Date endDate;// 周期结束日期

    private String currentCycle;
    private Integer currentCycleNum;
    private Date currentCycleStartDate; // 自定义周期起点（如15日开始）
    private Date currentCycleEndDate; // 自定义周期起点（如15日开始）
    private boolean naturalCycle; // 是否自然周期
    private boolean payNextCycle;

    public boolean isInCycle(Date now) {
        Date start = calCurrentCycleStartDate(now);
        Date end = calCurrentCycleEndDate(start);
        return now.after(start) && now.before(end);
    }

    public long calCycleNum(Date now) {
        long cycleNum = 0;
        Date firstCycleStart = null;
        if (naturalCycle) {
            // 自然周期的起点
            if (cycleType == CycleTypeEnum.DAY) {
                firstCycleStart = DateUtil.beginOfDay(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalDayDiff(firstCycleStart, now);
            } else if (cycleType == CycleTypeEnum.WEEK) {
                firstCycleStart = DateUtil.beginOfWeek(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalWeekDiff(firstCycleStart, now);
            } else if (cycleType == CycleTypeEnum.MONTH) {
                firstCycleStart = DateUtil.beginOfMonth(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalMonthDiff(firstCycleStart, now);
            } else if (cycleType == CycleTypeEnum.YEAR) {
                firstCycleStart = DateUtil.beginOfYear(startDate);
                cycleNum = DateDifferenceCalculator.getNaturalYearDiff(firstCycleStart, now);
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
                cycleNum = DateDifferenceCalculator.getNonNaturalYearDiff(startDate, now);
            }
        }
        return ++cycleNum;
    }

    public boolean isInOrBeforeCurrentCycle(Date now) {
        Date start = calCurrentCycleStartDate(now);
        Date end = calCurrentCycleEndDate(start);

        return now.before(end);
    }

    public boolean isBeforeCurrentCycle(Date now) {
        Date start = calCurrentCycleStartDate(now);
        return now.before(start);
    }


    public String genCycleKey(Date now) {
        Date start = calCurrentCycleStartDate(now);
        setCurrentCycleStartDate(start);

        Date end = calCurrentCycleEndDate(start);
        String startStr = DateUtil.formatDate(start);
        String endStr = DateUtil.formatDate(end);
        if (startStr.equals(endStr)) {
            return startStr;
        }
        return startStr + "-" + endStr;
    }

//    public String genCycleKey() {
//        Date start = calCurrentCycleStartDate();
//        Date end = calCurrentCycleEndDate(start);
//        String startStr = DateUtil.formatDate(start);
//        String endStr = DateUtil.formatDate(end);
//        if (startStr.equals(endStr)) {
//            return startStr;
//        }
//        return startStr + "-" + endStr;
//    }


    public Date calCurrentCycleStartDate(Date date) {
        if (currentCycleStartDate != null) {
            return currentCycleStartDate;
        }

        Date firstCycleStart;
        if (naturalCycle) {
            // 自然周期的起点
            if (cycleType == CycleTypeEnum.DAY) {
                firstCycleStart = DateUtil.beginOfDay(date);
            } else if (cycleType == CycleTypeEnum.WEEK) {
                firstCycleStart = DateUtil.beginOfWeek(date);
            } else if (cycleType == CycleTypeEnum.MONTH) {
                firstCycleStart = DateUtil.beginOfMonth(date);
            } else if (cycleType == CycleTypeEnum.YEAR) {
                firstCycleStart = DateUtil.beginOfYear(date);
            } else {
                throw new RuntimeException("不支持的周期类型");
            }
        } else {
            // 非自然周期的起点
//            firstCycleStart = DateUtil.beginOfDay(date);
            firstCycleStart = DateUtil.beginOfDay(startDate);
        }

        return firstCycleStart;
    }

    public Date updateCurrentCycleStartDate(Date now) {
        Date start = null;
        Date end = null;
//        this.currentCycleStartDate = DateUtil.offsetSecond(end, 1);

        while (!isInCycle(now)) {
            // 更新到下一个周期
            start = calCurrentCycleStartDate(now);
            end = calCurrentCycleEndDate(start);
            this.currentCycleStartDate = DateUtil.offsetSecond(end, 1);

            // 如果已超出结束日期，停止更新并返回 null
            if (endDate != null && start.after(endDate)) {
                return null;
            }

            // 防止过多循环
            if (now.before(start)) {
                return null;
            }
        }

        return currentCycleStartDate;
    }


    public Date calCurrentCycleEndDate(Date start) {
        Date end = null;
        if (CycleTypeEnum.DAY.equals(getCycleType())) {
            end = DateUtil.offsetDay(start, 1);
        } else if (CycleTypeEnum.WEEK.equals(getCycleType())) {
            end = DateUtil.offsetWeek(start, 1);
        } else if (CycleTypeEnum.MONTH.equals(getCycleType())) {
            end = DateUtil.offsetMonth(start, 1);
        } else if (CycleTypeEnum.YEAR.equals(getCycleType())) {
            end = DateUtil.offset(start, DateField.YEAR, 1);
        } else {
            throw new IllegalArgumentException("Unsupported cycle type: " + getCycleType());
        }
        end = DateUtil.offset(end, DateField.SECOND, -1);
//        if (end.after(endDate)) {
//            end = endDate;
//        }
        return end;
    }


    public boolean canGenCycle(Date now, Date cycleEnd) {

        Date genEndCycle = null;
        Date genStartCycle = null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(cycleEnd);
        if (getCycleType() == com.kane.practice.program.cyclefee.release3.CycleTypeEnum.YEAR) {
            cal.add(Calendar.MONTH, -1); // 倒数第二个月的最后一天
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            genEndCycle = cal.getTime();
            genStartCycle = DateUtil.beginOfMonth(genEndCycle);
        } else if (getCycleType() == com.kane.practice.program.cyclefee.release3.CycleTypeEnum.MONTH) {
            cal.add(Calendar.DAY_OF_MONTH, -1); // 倒数第二天的最后一秒
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);

            genEndCycle = cal.getTime();
            genStartCycle = DateUtil.beginOfDay(genEndCycle);
        } else if (getCycleType() == com.kane.practice.program.cyclefee.release3.CycleTypeEnum.WEEK) {
            cal.add(Calendar.DAY_OF_MONTH, -1); // 倒数第二天的最后一秒
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            genEndCycle = cal.getTime();
            genStartCycle = DateUtil.beginOfDay(genEndCycle);
        }  else if (getCycleType() == CycleTypeEnum.DAY) {
//            cal.set(Calendar.HOUR_OF_DAY, 22);
//            cal.set(Calendar.MINUTE, 1);
//            cal.set(Calendar.SECOND, 1);
            genStartCycle = DateUtil.beginOfDay(cycleEnd);
        }

        if (now.after(genStartCycle)) {
            return true;
        }
        return false;
    }

    public Integer increaseCycleNum() {
        if (currentCycleNum == null || currentCycleNum == 0) {
            currentCycleNum = 1;
            return currentCycleNum;
        }
        currentCycleNum = currentCycleNum + 1;
        return currentCycleNum;
    }
}
