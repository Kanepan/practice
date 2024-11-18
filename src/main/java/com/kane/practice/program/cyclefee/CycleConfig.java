package com.kane.practice.program.cyclefee;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CycleConfig {
    private BigDecimal amount; // 周期金额
    private String cycleType; // 周期类型：DAY, WEEK, MONTH, YEAR
    private Date startDate;   // 周期起始日期
    private Date endDate;
    private String currentCycle;// 周期结束日期
}
