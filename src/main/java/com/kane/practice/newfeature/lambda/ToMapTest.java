package com.kane.practice.newfeature.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToMapTest {
    public static void main(String[] args) {
        List<Employee> employeeList = Arrays.asList(
                new Employee(101, "张三", 18, 9999.99),
                new Employee(102, "李四", 59, 6666.66),
                new Employee(103, "王五", 28, 3333.33),
                new Employee(103, "王五1", 28, 3333.33),
                new Employee(104, "赵六", 8, 7777.77)
        );
        //在 id 重复的情况下，保留新的值, 不会抛出异常Duplicate key 王五
        Map<Integer, Double> collect4 = employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getSalary, (oldValue, newValue) -> newValue));
        System.out.println(collect4);
        /**
         * id作为Map的key,name作为value的集合
         * */
        Map<Integer, String> collect1 = employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getName));
        //System.out.println(collect1);//{101=张三, 102=李四, 103=王五, 104=赵六}

        /**
         * id作为map的集合，Employee对象作为Map的value
         */
        Map<Integer, Employee> collect2 = employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, t -> t));
        System.out.println(collect2);//{101=Employee(id=101, name=张三, age=18, salary=9999.99), 102=Employee(id=102, name=李四, age=59, salary=6666.66), 103=Employee(id=103, name=王五, age=28, salary=3333.33), 104=Employee(id=104, name=赵六, age=8, salary=7777.77)}
        /**
         * id作为map的集合，Employee对象作为Map的value
         */
        Map<Integer, Employee> collect3 = employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));
        System.out.println(collect3);//{101=Employee(id=101, name=张三, age=18, salary=9999.99), 102=Employee(id=102, name=李四, age=59, salary=6666.66), 103=Employee(id=103, name=王五, age=28, salary=3333.33), 104=Employee(id=104, name=赵六, age=8, salary=7777.77)}

        System.out.println("-----------------");//{101=Employee(id=101, name=张三, age=18, salary=9999.99), 102=Employee(id=102, name=李四, age=59, salary=6666.66), 103=Employee(id=103, name=王五, age=28, salary=3333.33), 104=Employee(id=104, name=赵六, age=8, salary=7777.77)}


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
