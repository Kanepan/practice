package com.kane.practice.newfeature.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CFSupplyAsyncTest {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        CompletableFuture<Boolean> c1 = CompletableFuture.supplyAsync(()-> {
           try {
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           return true;
       },es);

        CompletableFuture<Boolean> c2 = CompletableFuture.supplyAsync(()-> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        },es);

        CompletableFuture.allOf(c1,c2).whenComplete((r,e)->{
            System.out.println(r);
        });
    }
}
