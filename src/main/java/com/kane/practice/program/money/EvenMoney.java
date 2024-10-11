package com.kane.practice.program.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kanepan
 */
public class EvenMoney {

    public static void main(String[] args) {
        //满足
        evenMoney(7, new BigDecimal("100"));
    }


    //四舍五入+多轮平均
    //第一个 100/3=33.33
    //第二个(100-33.33)/2=33.34
    //第三个(100-33.33-33.34)/1=33.33
    // 不超过100， 并且应分，尽分
    private static void evenMoney(int numberOfParts, BigDecimal totalAmount) {
        List<BigDecimal> parts = new ArrayList<>();
        BigDecimal remainingAmount = totalAmount;

        for (int i = 0; i < numberOfParts; i++) {
            int remainingParts = numberOfParts - i;
            // 计算当前份额，保留两位小数
            BigDecimal partAmount = remainingAmount.divide(new BigDecimal(remainingParts), 2, RoundingMode.HALF_UP);
            // 四舍五入到整数
            BigDecimal roundedPart = partAmount.setScale(2, RoundingMode.HALF_UP);
            parts.add(roundedPart);
            // 减去已经分配的份额
            remainingAmount = remainingAmount.subtract(partAmount);
        }

        // 打印每部分的金额
        System.out.println("每部分的金额为: " + parts);
        System.out.println("总和为: " + parts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
