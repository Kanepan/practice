package com.kane.practice.concurrent.thread;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledExecutorServiceTest {


    static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            scheduledExecutorService.schedule(() -> {
                System.out.println("Hello World" + Thread.currentThread().getName() + " " + finalI);
                atomicInteger.incrementAndGet();

            }, RandomUtils.nextInt(1, 5), TimeUnit.SECONDS);
        }

        try {
            scheduledExecutorService.shutdown();
            boolean flag = scheduledExecutorService.awaitTermination(3, TimeUnit.SECONDS);
            System.out.println(flag);
            System.out.println(atomicInteger.get());
            if(!flag){
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
