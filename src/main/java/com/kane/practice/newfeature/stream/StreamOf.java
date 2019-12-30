/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.newfeature.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class StreamOf {
    public static void main(String[] args) {
        test4();
    }


    public static void test4() {
        DoubleStream.of(1.0, 2.0, 3.0)
                .boxed()
                .collect(Collectors.toList()).forEach(System.out::println);

        Stream.of(1.0, 2.0, 3.0).collect(Collectors.toList()).forEach(System.out::println);
    }

    public static void test3() {
        System.out.println("--------------------------");
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        Stream.generate(System::nanoTime).limit(10).forEach(System.out::println);

    }

    public static void iterate() {
        Stream.iterate(10, n -> n + 1)
                .limit(5)
                .collect(Collectors.toList())
                .forEach(System.out::println);

        System.out.println("--------------------------");
        Stream.iterate(0, i -> i + 1).limit(5).collect(Collectors.toList())
                .forEach(System.out::println);
    }

    public static void test1() {
        Stream<String> stream = Stream.of("a", "b", "c");
        stream.forEach(System.out::println);
    }

    public static void test2() {
        Stream<String> stream = Stream.of("a", "b", "c");
        StringBuffer sb = new StringBuffer();
        stream.forEach(sb::append);
        System.out.println(sb);
    }
}
