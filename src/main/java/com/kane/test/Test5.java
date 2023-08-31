package com.kane.test;

import java.math.BigDecimal;

public class Test5 {
    public static void main(String[] args) {
        BigDecimal b1 = BigDecimal.valueOf(0.3);

        System.out.println(b1.compareTo(BigDecimal.valueOf(9.1)));
    }
}
