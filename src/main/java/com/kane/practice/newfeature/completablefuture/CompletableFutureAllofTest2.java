package com.kane.practice.newfeature.completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletableFutureAllofTest2 {

    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        CompletableFuture completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("done 1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, es);


        CompletableFuture completableFuture2 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    System.out.println("done 2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, es);

        CompletableFuture completableFuture3 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    System.out.println("done 3");
                    throw new RuntimeException("test3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, es);

        CompletableFuture.allOf(completableFuture, completableFuture2, completableFuture3).whenComplete((r, e) -> {
            System.out.println("over");
            if(e!=null){
                e.printStackTrace();
            }
            es.shutdown();
        });

    }


}
