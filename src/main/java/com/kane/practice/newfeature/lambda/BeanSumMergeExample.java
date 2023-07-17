package com.kane.practice.newfeature.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanSumMergeExample {
    public static void main(String[] args) {
        List<Bean2> beanList = new ArrayList<>();
        beanList.add(new Bean2(10, "A", 30));
        beanList.add(new Bean2(40, "A", 60));
        beanList.add(new Bean2(70, "B", 90));
        beanList.add(new Bean2(80, "A", 100));

        // 按字段2进行合并并求和
        Map<String, Bean2> mergedMap = beanList.stream()
                .collect(Collectors.toMap(Bean2::getField2, bean -> bean, Bean2::merge));

        // map to list
        List<Bean2> mergedList = new ArrayList<>(mergedMap.values());
        System.out.println(mergedList);

        // 打印结果
        mergedMap.forEach((field2, mergedBean) -> System.out.println("Field2: " + field2 + ", Sum of Field1: " + mergedBean.getField1() + ", Sum of Field3: " + mergedBean.getField3()));
    }
}

// Bean类
class Bean2 {
    private int field1;
    private String field2;
    private int field3;

    public Bean2(int field1, String field2, int field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public int getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public int getField3() {
        return field3;
    }

    // 合并方法
    public Bean2 merge(Bean2 other) {
        return new Bean2(this.field1 + other.field1, this.field2, this.field3 + other.field3);
    }
}
