package com.kane.test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test20 {
    public static void main(String[] args) {

        Long year = 11000L;

        BigDecimal avg = new BigDecimal(year)
                .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP) // 先除以365
                .multiply(BigDecimal.valueOf(100)) // *100
                .setScale(0, RoundingMode.CEILING)// 向上取整
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UNNECESSARY);

        System.out.println(avg);

    }
}
