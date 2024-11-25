package com.kane.practice.program.cyclefee.release;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
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

    public boolean isNowInCycle(Date now) {
        if (now == null) {
            now = new Date();
        }
        Date start = calCurrentCycleStartDate();
        Date end = calCurrentCycleEndDate(start);
        return now.after(start) && now.before(end);
    }

    public String genCycleKey() {
        Date start = calCurrentCycleStartDate();
        Date end = calCurrentCycleEndDate(start);
        String startStr = DateUtil.formatDate(start);
        String endStr = DateUtil.formatDate(end);
        if (startStr.equals(endStr)) {
            return startStr;
        }
        return startStr + "-" + endStr;
    }


    public Date calCurrentCycleStartDate() {
        if (naturalCycle) {
            //自然周期
            if (currentCycleStartDate == null) {
                if (cycleType == CycleTypeEnum.DAY) {
                    this.currentCycleStartDate = DateUtil.beginOfDay(startDate);
                } else if (cycleType == CycleTypeEnum.WEEK) {
                    this.currentCycleStartDate = DateUtil.beginOfWeek(startDate);
                } else if (cycleType == CycleTypeEnum.MONTH) {
                    this.currentCycleStartDate = DateUtil.beginOfMonth(startDate);
                } else if (cycleType == CycleTypeEnum.YEAR) {
                    this.currentCycleStartDate = DateUtil.beginOfYear(startDate);
                } else {
                    throw new RuntimeException("不支持的周期类型");
                }
            }
            return currentCycleStartDate;
        } else {
            //非自然周期
            if (currentCycleStartDate == null) {
                this.currentCycleStartDate = DateUtil.beginOfDay(startDate);
            }
        }
        return currentCycleStartDate;
    }

    public Date updateCurrentCycleStartDate() {
        Date start = calCurrentCycleStartDate();
        Date end = calCurrentCycleEndDate(start);
        this.currentCycleStartDate = DateUtil.offsetSecond(end, 1);
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

    public Integer increaseCycleNum() {
        if (currentCycleNum == null || currentCycleNum == 0) {
            currentCycleNum = 1;
            return currentCycleNum;
        }
        currentCycleNum = currentCycleNum + 1;
        return currentCycleNum;
    }
}
