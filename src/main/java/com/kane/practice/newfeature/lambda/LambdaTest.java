/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.newfeature.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LambdaTest {


    public static void main(String[] args) {
        LambdaTest lambdaTest = new LambdaTest();

        lambdaTest.test3();

        System.out.println("-------------------");

        lambdaTest.test4();

        System.out.println("-------------------");
        lambdaTest.test5();

        System.out.println("-------------------");
        lambdaTest.test6();
    }

    //
    private static void test2() {
        MyLambdaInterface myLambdaInterface = s -> System.out.println(s);

    }

    interface MyLambdaInterface {
        void print(String s);
    }


    private static void test3() {
        enact((s) -> System.out.println(s), "test");
    }

    public static void enact(MyLambdaInterface myLambdaInterface, String s) {
        myLambdaInterface.print(s);
    }

    //---------------------------------------------

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

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    interface NameChecker {
        boolean chedkName(Person p);
    }

    interface Executor {
        void execute(Person p);
    }

    public void checkAndExecutor(List<Person> personList, NameChecker nameChecker, Executor executor) {
        for (Person p : personList) {
            if (nameChecker.chedkName(p)) {
                executor.execute(p);
            }
        }
    }

    private List<Person> personList = Arrays.asList(
            new Person("aa", 18),
            new Person("zz", 19),
            new Person("zz", 20)
    );

    public void test4() {
        checkAndExecutor(personList, p -> p.getName().startsWith("z"), p -> System.out.println(p.age));
    }

    // ------------------------------

    public void test5() {

        checkAndExecutorNew(personList, p -> p.getName().startsWith("z"), p -> System.out.println(p.getAge()));
    }

    public void checkAndExecutorNew(List<Person> personList, Predicate<Person> redicate, Consumer<Person> consumer) {
        System.out.println("filter and forEach");
        personList.stream().filter(redicate).forEach(consumer);
    }


    // ------------------------------
    public void test6() {
        checkAndExecutorNew2(personList, p -> p.getName().startsWith("z"), p -> System.out.println(p.getAge()));
    }

    public void checkAndExecutorNew2(List<Person> personList, Predicate<Person> redicate, Consumer<Person> consumer) {
        System.out.println("forEach");
        personList.forEach(p -> {
            if (redicate.test(p)) {
                consumer.accept(p);
            }
        });
    }

}

