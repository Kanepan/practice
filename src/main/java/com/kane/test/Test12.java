package com.kane.test;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test12 {

    public static void main(String[] args) {
//        BigDecimal amount = NumberUtil.round(new BigDecimal("0.02").multiply(new BigDecimal("0.01")), 2, RoundingMode.HALF_UP);
//        System.out.println(amount);
//        System.out.println(amount.equals(new BigDecimal("0.00")));
//        System.out.println(amount.equals(new BigDecimal("0")));
//        System.out.println(amount.compareTo(BigDecimal.ZERO));

        String str = "as阿斯蒂芬-应分:\\\" + shouldDivisionAmount + \\\"元-实分\\\" + amount + \\\"元\"";

        String[] split = str.split("-");
        System.out.println(split[0]);
    }
}
