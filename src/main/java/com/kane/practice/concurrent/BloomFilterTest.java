package com.kane.practice.concurrent;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BloomFilterTest {
    private static int size = 10000000;

    private static Set<Integer> set = new HashSet<>();

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, 0.01);

    public static void main(String[] args) throws InterruptedException {

        bloomFilter();

        //hashMap();

        Thread.sleep(10000000L);
    }

    private static void bloomFilter(){
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }
        List<Integer> list = new ArrayList<Integer>(1000);
        // 故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里

        long time = System.nanoTime();
        for (int i = size + 1000000; i < size + 2000000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("检测时间;" + (System.nanoTime() - time)/1000000 + "ms");
        System.out.println("误判的数量：" + list.size());
        System.out.println("误判的率：" + Double.valueOf(list.size()) / 10000);
    }

    private static void hashMap(){
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
    }
}
