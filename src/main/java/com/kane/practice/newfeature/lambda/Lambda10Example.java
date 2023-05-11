package com.kane.practice.newfeature.lambda;

import java.util.*;
import java.util.stream.Collectors;

public class Lambda10Example {

    public static void main(String[] args) {
        Lambda10Example lambda10Example = new Lambda10Example();
//        lambda10Example.test1();
//        //输出分割线
//        System.out.println("-------------------");
//        lambda10Example.test2();
        System.out.println("-------------------");
        lambda10Example.test9();
    }

    // 方法test1 使用lambda表达式实现集合遍历    1.8之前
    public void test1() {
        List<String> list = Arrays.asList("hello", "world", "hello world");
        list.forEach(item -> System.out.println(item.toUpperCase()));
    }


    //使用lambda表达式进行排序
    public void test2() {
        List<String> list = Arrays.asList("apple", "banna", "orange");

        //传统方式
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        list.forEach(item -> System.out.println(item));
        System.out.println("-------------------");
        //使用lambda表达式
        list.sort((item1, item2) -> item2.compareTo(item1));
        list.forEach(item -> System.out.println(item));
    }

    //使用lambda表达式进行过滤
    public void test3() {
        List<String> list = Arrays.asList("hello", "world", "hello world");
        //传统方式
        list.forEach(item -> {
            if (item.length() > 5) {
                System.out.println(item);
            }
        });
        System.out.println("-------------------");
        //lambda表达式

        //直接过滤
        list.stream().filter(item -> item.length() > 5).forEach(item -> System.out.println(item));

        //过滤后收集
        List<String> filterList = list.stream().filter(item -> item.length() > 5).collect(Collectors.toList());
        System.out.println(filterList.size());
    }

    // 映射
    public void test4() {
        List<String> list = Arrays.asList("hello", "world", "hello world");
        //使用lambda表达式进行映射
        List<Integer> lengths = list.stream().map(item -> item.length()).collect(Collectors.toList());
        lengths.forEach(item -> System.out.println(item));
    }


    //lambda表达式进行归约
    public void test5() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        //传统方式
        Integer sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        System.out.println(sum);

        //使用lambda表达式进行归约
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);

        Integer sum2 = list2.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum2);
    }

    //test6 使用lambda表达式进行分组
    public void test6() {
        List<Person> list = Arrays.asList(
                new Person("zhangsan", 20),
                new Person("lisi", 30),
                new Person("wangwu", 40),
                new Person("zhangsan", 50)
        );
        //传统方式
        list.forEach(item -> System.out.println(item.getName()));
        System.out.println("-------------------");
        //使用lambda表达式进行分组

        list.stream().collect(Collectors.groupingBy(Person::getName)).forEach((name, personList) -> {
            System.out.println(name);
            personList.forEach(person -> System.out.println(person.getAge()));
        });

        Map<String, List<Person>> groupeds = list.stream().collect(Collectors.groupingBy(Person::getName));
        groupeds.forEach((key, value) -> System.out.println(key + " -->" + value));
    }

    // bean Person with name and age
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


    //test7  使用lambda表达式进行线程的创建
    public void test7() {
        //传统方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        }).start();

        //使用lambda表达式进行线程的创建
        new Thread(() -> System.out.println("hello world")).start();
    }

    // 使用lambda表达式进行optional的操作
    public void test8() {
        //传统方式
        String str = "hello world";
        if (str != null) {
            System.out.println(str);
        }

        str = null;
        //使用lambda表达式进行optional的操作
        Optional.ofNullable(str).ifPresent(item -> System.out.println(item));


        str = "hello world";
        Optional<String> strs = Optional.ofNullable(str);
        strs.map(String::toUpperCase).ifPresent(System.out::println);
    }

    // 使用lambda表达式进行stream流水线操作
    public void test9() {
        List<String> list = Arrays.asList("hello", "world", "hello world", "aa", "c", "bb");
        //传统方式
        List<String> list2 = new ArrayList<>();
        for (String item : list) {
            if (item.length() > 5) {
                list2.add(item);
            }
        }
        Collections.sort(list2);
        list2.forEach(item -> System.out.println(item));

        //使用lambda表达式进行stream流水线操作
        list.stream().filter(item -> item.length() > 5).sorted(Comparator.comparingInt(String::length)).forEach(item -> System.out.println(item));

        //输出分割线
        System.out.println("-------------------");
        List<String> list3 = list.stream().filter(item -> item.length() > 1).map(String::toUpperCase).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        list3.forEach(item -> System.out.println(item));

    }


}
