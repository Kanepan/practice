package com.kane.practice.newfeature.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlatMapTest {
    public static void main(String[] args) {
        List<Sku> aList = Arrays.asList(
                new Sku(101, "矿泉水" ),
                new Sku(102, "红牛")
        );

        List<Sku> bList = Arrays.asList(
                new Sku(105, "面包" ),
                new Sku(106, "蛋糕")
        );

        List<SkuGroup> skuGroupList = Arrays.asList(
                new SkuGroup(1, aList),
                new SkuGroup(2, bList)
        );


        // 使用 flatMap 将 SkuGroup 中的 skuList 扁平化为单个 Sku 列表
        List<Sku> allSkus = skuGroupList.stream()
                .flatMap(skuGroup -> skuGroup.getSkuList().stream())
                .collect(Collectors.toList());
        // 打印所有 Sku
        allSkus.forEach(sku -> System.out.println("Sku ID: " + sku.getId() + ", Name: " + sku.getName()));
    }

    @Data
    @AllArgsConstructor
    static class Sku {
        private Integer id;
        private String name;
//        private Integer categoryId;
    }


    @Data
    @AllArgsConstructor
    static class SkuGroup {
        private Integer categoryId;
        List<Sku> skuList;
    }
}
