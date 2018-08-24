package com.kane.practice.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public class InternersTest {
    private static Interner<String> pool = Interners.newWeakInterner();
    private static ExecutorService es = Executors.newFixedThreadPool(32);

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Set<Callable<Integer>> set = new HashSet<Callable<Integer>>();
        for (int i = 0; i < 10; i++) {

            //final String count  = i +"";
            set.add(new Callable<Integer>() {
                public Integer call() throws Exception {
//					deal(System.currentTimeMillis() + count);
                    deal(System.currentTimeMillis()+"");
                    return null;
                }
            });
        }

        try {
            es.invokeAll(set);
        } catch (InterruptedException e1) {
        }

        System.out.println("cost : " + (System.currentTimeMillis() - time) / 1000 + "ms");
    }

    public static void deal(String bizId) {
        synchronized (pool.intern(bizId)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("完成任务，释放锁");
        }
    }
}