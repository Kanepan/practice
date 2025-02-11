package com.kane.practice.newfeature.lambda;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

class Transaction {
    String category;
    int amount;
    int tax;

    public Transaction(String category, int amount, int tax) {
        this.category = category;
        this.amount = amount;
        this.tax = tax;
    }

    public String groupByCategoryAndTax() {
        return category + tax;
    }

    public String groupByCategory() {
        return category ;
    }

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public int getTax() {
        return tax;
    }
}

@Data
class Summary {
    int totalAmount;
    int totalTax;
    String category;


}

public class BeanSumNew {
    public static void main(String[] args) {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("Food", 100, 10),
                new Transaction("Food", 200, 20),
                new Transaction("Transport", 50, 5),
                new Transaction("Food", 150, 10),
                new Transaction("Transport", 100, 10)
        );

        // 按照 category 分组并对 amount 和 tax 字段求和
        Map<String, Summary> sumByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::groupByCategoryAndTax,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    int totalAmount = list.stream().mapToInt(Transaction::getAmount).sum();
                                    int totalTax = list.stream().mapToInt(Transaction::getTax).sum();
                                    Summary summary =  new Summary();
                                    summary.totalAmount = totalAmount;
                                    summary.totalTax = totalTax;
                                    summary.category = list.get(0).getCategory();
                                    return summary;
                                }
                        )
                ));



        // 输出结果
        sumByCategory.forEach((category, summary) ->
                System.out.println(category + ": " + summary)
        );
    }
}
