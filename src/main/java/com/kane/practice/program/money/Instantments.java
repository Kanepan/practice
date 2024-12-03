package com.kane.practice.program.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Instantments {
    /**
     * 计算每期还款金额
     *
     * @param totalAmount  单价金额（总金额）
     * @param downAmount   首付款
     * @param interestRate 利率（年利率，如 0.05 表示 5%）
     * @param periodNum    分期期数
     * @return 每期还款金额列表，最后一期包含余数
     */
    public static List<BigDecimal> calculateRepayment(BigDecimal totalAmount, BigDecimal downAmount, BigDecimal interestRate, Integer periodNum) {
        // 确保参数有效
        if (totalAmount == null || downAmount == null || interestRate == null || periodNum == null || periodNum <= 0) {
            throw new IllegalArgumentException("参数不能为null且期数必须大于0");
        }

        // 计算应还总金额
        BigDecimal decimalInterestRate = interestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal loanAmount = totalAmount.subtract(downAmount); // 剩余贷款金额
        BigDecimal totalRepayment = loanAmount.multiply(BigDecimal.ONE.add(decimalInterestRate)); // 应还总金额（包含利率）

        // 每期还款金额（先按平均分配）
        BigDecimal basicRepayment = totalRepayment.divide(BigDecimal.valueOf(periodNum), 2, RoundingMode.DOWN);
        List<BigDecimal> repayments = new ArrayList<>();

        // 填充每期还款金额
        for (int i = 0; i < periodNum; i++) {
            repayments.add(basicRepayment);
        }

        // 计算余数并加到最后一期
        BigDecimal totalBasicRepayment = basicRepayment.multiply(BigDecimal.valueOf(periodNum));
        BigDecimal remainder = totalRepayment.subtract(totalBasicRepayment); // 余数
        // 保留两位小数
        remainder = remainder.setScale(2, RoundingMode.DOWN);
        repayments.set(periodNum - 1, repayments.get(periodNum - 1).add(remainder)); // 最后一期加余数

        return repayments;
    }

    public static void main(String[] args) {
        // 示例数据
        BigDecimal totalAmount = new BigDecimal("10000"); // 总金额
        BigDecimal downAmount = new BigDecimal("0"); // 首付款
        BigDecimal interestRate = new BigDecimal("0"); // 利率（5%）
        Integer periodNum = 12; // 分期期数

         //计算每期还款金额
        List<BigDecimal> repayments = calculateRepayment(totalAmount, downAmount, interestRate, periodNum);

        // 输出结果
        for (int i = 0; i < repayments.size(); i++) {
            System.out.println("第 " + (i + 1) + " 期还款金额: " + repayments.get(i));
        }

        System.out.println("=====================================");

        for (int i = 0; i < periodNum; i++) {
            System.out.println("第 " + (i + 1) + " 期还款金额: " + calculateItemAmout(totalAmount, i, 2, periodNum));
        }
    }

    private static BigDecimal calculateItemAmout(BigDecimal amount, int i, int installmentsRoundedType, Integer periodNum) {

        BigDecimal periodNumBig = new BigDecimal(periodNum);
        //保留两位小数, 直接截断
        BigDecimal itemAmout = amount.divide(periodNumBig, 2, BigDecimal.ROUND_DOWN);
        if (installmentsRoundedType == 1) {
            //差额计入首周期
            if (i == 0) {
                //分期款第一个周期
                BigDecimal afterAmount = itemAmout.multiply(periodNumBig);
                //分期款加一起后可能比总金额少,  差值放在第一个周期收
                itemAmout = itemAmout.add(amount.subtract(afterAmount));
            }
        } else {
            //差额计入末周期
            if (i + 1 == periodNum) {
                //分期款最后一个周期
                BigDecimal afterAmount = itemAmout.multiply(periodNumBig);
                //分期款加一起后可能比总金额少,  差值放在最后周期收
                itemAmout = itemAmout.add(amount.subtract(afterAmount));
            }
        }
        return itemAmout;
    }
}
