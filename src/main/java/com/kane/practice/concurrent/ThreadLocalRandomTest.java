/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.concurrent;

import com.kane.practice.utils.TimeCostUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 结论:在一个线程中多次执行random操作, ThreadLocalRandom优势非常大,因为减少了原子操作
 */
public class ThreadLocalRandomTest {


    private static final Random random = new Random();
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    private static ExecutorService es = Executors.newFixedThreadPool(20);
    private static int count = 1000000;

    public static void main(String[] args) {
        TimeCostUtils timeCostUtils = new TimeCostUtils();

        List<Callable<Boolean>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    for (int i = 0; i < 100; i++) {
//                        doRandom();
                        doThreadLocalRandom();
                    }

                    return null;
                }
            });
        }
        try {
            es.invokeAll(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("time cost : " + timeCostUtils.cost() + "ms");
    }


    private static void doRandom() {
        random.nextInt(100);
    }

    private static void doThreadLocalRandom() {
        threadLocalRandom.nextInt(100);
    }
}
