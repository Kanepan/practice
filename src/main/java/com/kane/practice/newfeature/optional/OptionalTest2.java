package com.kane.practice.newfeature.optional;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalTest2 {

    public static void main(String[] args) {
        People people = null;
//        String username = Optional.ofNullable(people).ifPresent(People::getName).orElse(null);

    }


    public void test(){
        People p = null; // TODO:
        Optional<People> people = Optional.ofNullable(p);
        Map<String, String> result = new HashMap<>();
        //java 9

//        people.map(People::getName)
//                .ifPresentOrElse(e -> {
//                    result.put("status", "1");
//                    result.put("name", e);
//                }, () -> {
//                    result.put("status", "?");
//                });

        System.out.println(result);
    }

    @Data
    public static class People{
        private String name;
    }
}
