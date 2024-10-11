package com.kane.practice.newfeature.stream;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByTest {


    public static void main(String[] args) {
        List<Receiver> list = new ArrayList<>();
//        list.add( new Receiver("a", new BigDecimal("1.5")));
        list.add( new Receiver("a", new BigDecimal("1.8")));
        list.add( new Receiver("b", new BigDecimal("5.8")));

        Map<String, Receiver> mergedReceivers = list.stream()
                .collect(Collectors.toMap(
                        Receiver::getName,
                        receiver -> receiver,
                        (existing, replacement) -> {
                            existing.setAmount(existing.getAmount().add(replacement.getAmount()));
                            return existing;
                        }
                ));

        List<Receiver> result = new ArrayList<>(mergedReceivers.values());

        System.out.println("Merged Receivers:");
        result.forEach(System.out::println);
    }

    @Data
    public static class Receiver{
        private String name;
        private BigDecimal amount;

        public Receiver(String name, BigDecimal amount) {
            this.name = name;
            this.amount = amount;
        }

    }
}
