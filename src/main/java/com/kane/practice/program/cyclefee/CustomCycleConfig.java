package com.kane.practice.program.cyclefee;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomCycleConfig {
    private BigDecimal amount; // 周期款金额
    private String cycleType;  // 支持 MONTH / WEEK / DAY 等
    private Date customStartDate; // 自定义周期起点（如15日开始）
    private Date cycleNum;
    private Date currentStartDate;

}
