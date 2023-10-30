package com.kane.practice.newfeature.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonNameDupilate {
        private String name;

        public PersonNameDupilate(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static void main(String[] args) {
            List<PersonNameDupilate> personList = Arrays.asList(
                    new PersonNameDupilate("John"),
                    new PersonNameDupilate("Alice"),
                    new PersonNameDupilate("John"), // Duplicate name
                    new PersonNameDupilate("Bob"),
                    new PersonNameDupilate("Alice")  // Duplicate name
            );

            // 使用stream将List转换成Map，以name为key，计数为value
            Map<String, Long> nameCountMap = personList.stream()
                    .collect(Collectors.groupingBy(PersonNameDupilate::getName, Collectors.counting()));

            // 筛选出计数大于1的name，即重复的name属性
            System.out.println("Duplicate names:");
            nameCountMap.entrySet().stream()
                    .filter(entry -> entry.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .forEach(System.out::println);
        }

}
