package com.kane.practice.newfeature.lambda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByValue {

    public static void main(String[] args) {
        Map<Long, Long> map = new HashMap<>();
        map.put(1L, 1L);
        map.put(2L, 1L);
        map.put(3L, 2L);
        map.put(4L, 2L);
        map.put(5L, 3L);
        map.put(6L, 3L);
        Map<Long, List<Long>> collect = map.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        System.out.println(collect);
    }
}
