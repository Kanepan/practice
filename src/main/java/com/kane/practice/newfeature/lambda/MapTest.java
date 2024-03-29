package com.kane.practice.newfeature.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapTest {
    public static void main(String[] args) {
        MapTest mapTest = new MapTest();
        mapTest.test7();
    }

    public void test4() {
        List<Person> list = Arrays.asList(
                new Person("zhangsan", 20),
                new Person("lisi", 20),
                new Person("dead", null)
        );

        list.forEach(Person::getAge);

        list.forEach(item -> System.out.println(item.getAge()));
    }

    public void test5() {
        List<Person> list = Arrays.asList(
                new Person("zhangsan", 20),
                new Person("lisi", 20)
        );
        //传统方式
        list.forEach(item -> System.out.println(item.getName()));
        System.out.println("-------------------");
        //使用lambda表达式进行分组

        // 剔除 zhangsan
        list.stream().filter(item -> !item.getName().equals("zhangsan")).collect(Collectors.toList()).forEach(item -> System.out.println(item.getName()));


//        Map<String, List<Person>> groupeds = list.stream().collect(Collectors.groupingBy(Person::getName));
//        groupeds.forEach((key, value) -> System.out.println(key + " -->" + value));
    }

    //test6 使用lambda表达式进行分组
    public void test6() {
        List<Person> list = Arrays.asList(
                new Person("zhangsan", 20),
                new Person("lisi", 20)
        );
        //传统方式
        list.forEach(item -> System.out.println(item.getName()));
        System.out.println("-------------------");
        //使用lambda表达式进行分组

        Map<Integer,List<Person>> map = list.stream().collect(Collectors.groupingBy(Person::getAge));

        map.forEach((age, personList) -> {
            System.out.println(age);
            personList.forEach(person -> System.out.println(person.getName()));
        });

    }

    public void test7() {
        List<Person> list = Arrays.asList(
                new Person("zhangsan", 20),
                new Person("lisi", 20)
        );
        //传统方式
//        list.forEach(item -> System.out.println(item.getName()));
        System.out.println("-------------------");
        //使用lambda表达式进行分组


    }

    public class Person {
        private String name;
        private Integer age;

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        //重写toString方法
        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
