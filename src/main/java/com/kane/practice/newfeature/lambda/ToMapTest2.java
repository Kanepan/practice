package com.kane.practice.newfeature.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToMapTest2 {
    public static void main(String[] args) {
        List<Employee> employeeList = Arrays.asList(
                new Employee(101, "张三", 18, 9999.99),
                new Employee(102, "李四", 59, 6666.66),
                new Employee(103, "王五", 28, 3333.33),
                new Employee(103, "王五1", 28, 3333.33),
                new Employee(104, "赵六", 8, 7777.77)
        );
        //在 id 重复的情况下，保留新的值, 不会抛出异常Duplicate key 王五
        Map<Integer, String> collect = employeeList.stream()
                .collect(Collectors.toMap(Employee::getId,Employee::getName, (oldValue, newValue) -> newValue));
        System.out.println(collect);


        String a = "{\"machineCode\": \"abc\"}";
    }

    @Data
    @AllArgsConstructor
    static class Employee {
        private Integer id;
        private String name;
        private Integer age;
        private Double salary;
    }
}
