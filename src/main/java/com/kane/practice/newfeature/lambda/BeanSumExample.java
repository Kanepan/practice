package com.kane.practice.newfeature.lambda;


import java.util.ArrayList;
import java.util.List;

public class BeanSumExample {
    public static void main(String[] args) {
        List<Bean> beanList = new ArrayList<>();
        beanList.add(new Bean(10, 20, 30));
        beanList.add(new Bean(40, 50, 60));
        beanList.add(new Bean(70, 80, 90));


        int sumField1 = beanList.stream().mapToInt(Bean::getField1).sum();
        int sumField2 = beanList.stream().mapToInt(Bean::getField2).sum();
        int sumField3 = beanList.stream().mapToInt(Bean::getField3).sum();

        // 打印结果
        System.out.println("Sum of Field1: " + sumField1);
        System.out.println("Sum of Field2: " + sumField2);
        System.out.println("Sum of Field3: " + sumField3);
        System.out.println("-------------------------------");

        // 使用一个Stream对Bean对象的多个字段求和
        SumResult sumResult = beanList.stream()
                .reduce(new SumResult(), SumResult::accumulate, SumResult::combine);

        // 打印结果
        System.out.println("Sum of Field1: " + sumResult.getField1Sum());
        System.out.println("Sum of Field2: " + sumResult.getField2Sum());
        System.out.println("Sum of Field3: " + sumResult.getField3Sum());


    }
}

// Bean类
class Bean {
    private int field1;
    private int field2;
    private int field3;

    public Bean(int field1, int field2, int field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    public int getField3() {
        return field3;
    }
}

// 求和结果类
class SumResult {
    private int field1Sum;
    private int field2Sum;
    private int field3Sum;

    public SumResult() {
        this.field1Sum = 0;
        this.field2Sum = 0;
        this.field3Sum = 0;
    }

    public SumResult accumulate(Bean bean) {
        this.field1Sum += bean.getField1();
        this.field2Sum += bean.getField2();
        this.field3Sum += bean.getField3();
        return this;
    }

    public SumResult combine(SumResult other) {
        this.field1Sum += other.getField1Sum();
        this.field2Sum += other.getField2Sum();
        this.field3Sum += other.getField3Sum();
        return this;
    }

    public int getField1Sum() {
        return field1Sum;
    }

    public int getField2Sum() {
        return field2Sum;
    }

    public int getField3Sum() {
        return field3Sum;
    }
}
