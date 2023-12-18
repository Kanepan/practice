package com.kane.practice.newfeature.lambda;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PeekTest {
    public static void main(String[] args) {
//        Stream.of("one", "two", "three", "four")
//                .filter(e -> e.length() > 3)
//                .peek(e -> System.out.println("Filtered value: " + e))
//                .map(String::toUpperCase)
//                .peek(e -> System.out.println("Mapped value: " + e))
//                .collect(Collectors.toList()).forEach(System.out::println);

//        System.out.println("-------------");
//
//        Stream.of("one", "two", "three", "four")
//                .filter(e -> e.length() > 3)
//                .peek(String::toUpperCase)
//                .forEach(System.out::println);


        Stream.of("one", "two", "three", "four").filter(e -> e.length() > 3).forEach(System.out::println);
    }
}
