package com.kane.practice.concurrent;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.*;

public class BloomFilterTest {
    private static int size = 10000000;

    private static Set<Integer> set = new HashSet<>();

    private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), size, 0.001);

    public static void main(String[] args) throws InterruptedException {

        bloomFilter();

        //hashMap();

        Thread.sleep(10000000L);
    }

    private static void bloomFilter() {
        for (int i = 0; i < size; i++) {
//            bloomFilter.put(i);
            bloomFilter.put(UUID.randomUUID().toString());

        }
        List<Integer> list = new ArrayList<Integer>(1000);
        // 故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        long time = System.nanoTime();

        int testCount = 1000000;
        for (int i = 0; i < testCount; i++) {
//            if (bloomFilter.mightContain(i)) {

            if (bloomFilter.mightContain(UUID.randomUUID().toString())) {
                list.add(i);
            } else {
                //肯定不存在
            }
        }
        System.out.println("检测时间;" + (System.nanoTime() - time) / 1000000 + "ms");
        System.out.println("检测数量：" + testCount);
        System.out.println("误判的数量：" + list.size());
        System.out.println("误判的率：" + Double.valueOf(list.size()) / (testCount / 100));
    }

    private static void hashMap() {
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
    }
}
