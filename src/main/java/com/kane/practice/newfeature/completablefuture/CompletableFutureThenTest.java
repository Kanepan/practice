package com.kane.practice.newfeature.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureThenTest {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        thenCombine1();
//        thenCombineAsync();
//
//        thenApply();
    }

    private static void thenCombine1() {
        System.out.println("-----------------------------------");

        CompletableFuture<Boolean> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println("request interface 1");
            System.out.println("supplyAsync=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());

            return true;
        }, es);


        CompletableFuture<Boolean> cf2 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (Exception e) {
//            }
//            System.out.println("request interface 2");
            System.out.println("supplyAsync=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());
            return true;
        }, es);

        CompletableFuture<Boolean> result = cf1.thenCombine(
                cf2, (r1, r2) -> {
                    System.out.println(r1 && r2);
                    System.out.println("thenCombine=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());

                    return r1 && r2;
                });
        System.out.println("等待。。。");
        result.join();
    }

    private static void thenCombine2() {
        CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> 100d);
        CompletableFuture<Double> futureDiscount = CompletableFuture.supplyAsync(() -> 0.8);
        CompletableFuture<Double> futureResult = futurePrice.thenCombine(futureDiscount, (price, discount) -> price * discount);
        System.out.println("最终价格为:" + futureResult.join()); //最终价格为:80.0
    }

    private static void thenCombineAsync() {
        System.out.println("-----------------------------------");
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            try {
                // 这里模拟微服务A的查询接口
                System.out.println("A======" + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return System.currentTimeMillis();
        }).thenCombineAsync(
                CompletableFuture.supplyAsync(() -> {
                    try {
                        // 这里模拟微服务B的查询接口
                        System.out.println("completablefutureB======" + Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return System.currentTimeMillis();
                })
                , (r1, r2) -> {
                    // 合并两个查询结果
                    System.out.println(r1 + " = " + r2);
                    return r1 + " = " + r2;
                });

    }


    private static void thenApply() {
        System.out.println("-----------------------------------");
        CompletableFuture<Boolean> cf1 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("supplyAsync=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());

            System.out.println("step1 :" + true);
            return true;
        });


        CompletableFuture<Boolean> cf2 = cf1.thenApply((s) -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thenApply=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());

            System.out.println("step2 :" + s);
            return s || false;
        });
        System.out.println("等待");
        try {
            System.out.println("final: " + cf2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
