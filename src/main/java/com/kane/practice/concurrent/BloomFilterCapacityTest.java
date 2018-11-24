package com.kane.practice.concurrent;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.*;

public class BloomFilterCapacityTest {
    private static int size = 10000000;

    private static Set<String> set = new HashSet<>();

    private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), size, 0.00001);

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();
        bloomFilter();

        //hashMap();
        System.out.println("操作完成" + (System.currentTimeMillis() - time)/1000 + "s");
        Thread.sleep(10000000L);

        //结果: bloom算法 1千万 uuid fpp 0.01的只需要 50M  -- fpp 0.2 需要16M  0.00001 占用 81M 错误率越大，占用空间小
        //  hashSet 存储需要 1500M
        //是 hash算法 的 13 %

    }

    private static void bloomFilter(){

        for (int i = 0; i < size; i++) {
            bloomFilter.put(UUID.randomUUID().toString());
        }
    }

    private static void hashMap(){
        for (int i = 0; i < size; i++) {
            set.add(UUID.randomUUID().toString());
        }
    }
}
