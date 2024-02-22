package com.kane.test;

import java.util.ArrayList;
import java.util.List;

public class Test6 {

    public static void main(String[] args) {
        List<Long> skuIdNotInGroupList = new ArrayList<>();
        skuIdNotInGroupList.add(1L);
        skuIdNotInGroupList.add(2L);
        System.out.println(String.format("商品列表不在该货柜的商品库中%s", skuIdNotInGroupList));
    }
}
