package com.kane.practice.newfeature.completablefuture;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureAllofTest {
//    private static ExecutorService es = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {
        test1();
        test2();

        test4();
    }

    private static void test1() {
        System.out.println("-----------------------------------");

        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "Beautiful");

        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture.allOf(future1, future2, future3)


                .thenApply((v) -> Stream.of(future1, future2, future3)
                        .map(CompletableFuture::join)
                        .collect(Collectors.joining(" ")))
                .thenAccept(System.out::println);
    }


    private static void test2() {
        System.out.println("-----------------------------------");

        CompletableFuture<Boolean> future1
                = CompletableFuture.supplyAsync(() -> {
            //这里如果有sleep 就需要 在最后join
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtils.nextBoolean();
        });

        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "choose1");

        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "choose2");

        CompletableFuture.allOf(future1, future2, future3)
                .thenApply((v) -> {
                    if (future1.getNow(false)) {
                        return future2.getNow(null);
                    } else {
                        return future3.getNow(null);
                    }
                })
                .thenAccept(System.out::println).join();
        System.out.println("同步等待？");
    }


    private static void test3() {
        System.out.println("-----------------------------------");

        CompletableFuture<Boolean> future1
                = CompletableFuture.supplyAsync(() -> {
            //这里如果有sleep,thenApply就变成异步了
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return RandomUtils.nextBoolean();
        });

        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "choose1");

        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "choose2");

        CompletableFuture.allOf(future1, future2, future3)
                .thenApply((v) -> {
                    System.out.println("thenApply=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());
                    if (future1.getNow(false)) {
                        return future2.getNow(null);
                    } else {
                        return future3.getNow(null);
                    }
                })
                .thenAccept(System.out::println);

    }

    private static void test4() {
        System.out.println("-----------------------------------");

        CompletableFuture<Boolean> future1
                = CompletableFuture.supplyAsync(() -> {
            //这里如果有sleep,thenApply就变成异步了
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            return RandomUtils.nextBoolean();
            return true;
        });

        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "choose1");

        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "choose2");

        CompletableFuture.allOf(future1, future2, future3)
                .thenApplyAsync((v) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thenApply=" + Thread.currentThread().getName() + "|" + Thread.currentThread().isDaemon());
                    if (future1.getNow(false)) {
                        return future2.getNow(null);
                    } else {
                        return future3.getNow(null);
                    }
                })
                .thenAccept(System.out::println).join();

        System.out.println("同步等待？");
    }
}
