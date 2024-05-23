package com.kane.practice.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Money {
    private BigDecimal amount;
    private long cent;

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.cent = amount.multiply(new BigDecimal("100")).longValue();
    }

    public Money add(Money other) {
        BigDecimal result = amount.add(other.amount);
        return new Money(result);
    }

    public Money subtract(Money other) {
        BigDecimal result = amount.subtract(other.amount);
        return new Money(result);
    }

    public Money multiply(BigDecimal multiplier) {
        BigDecimal result = amount.multiply(multiplier).setScale(2, RoundingMode.HALF_EVEN);
        return new Money(result);
    }

    public Money divide(BigDecimal divisor) {
        BigDecimal result = amount.divide(divisor, 2, RoundingMode.HALF_EVEN);
        return new Money(result);
    }

    public int hashCode() {
        return (int) (this.cent ^ (this.cent >>> 32));
    }


    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(amount);
    }

    public boolean equals(Money other) {
        return (this.cent == other.cent);
    }


    // 示例用法
    public static void main(String[] args) {
        Money money1 = new Money(new BigDecimal("100.50"));
        Money money2 = new Money(new BigDecimal("50.75"));

        Money sum = money1.add(money2);
        Money difference = money1.subtract(money2);
        Money product = money1.multiply(new BigDecimal("2.5"));
        Money quotient = money1.divide(new BigDecimal("3"));

        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + difference);
        System.out.println("Product: " + product);
        System.out.println("Quotient: " + quotient);

        System.out.println("____________________________");
        System.out.println(new BigDecimal("10.5").equals(new BigDecimal("10.50")));
        //compare Money
        Money money3 = new Money(new BigDecimal("10.50"));
        Money money4 = new Money(new BigDecimal("10.50"));
        System.out.println(money3.equals(money4));//true

    }
}
