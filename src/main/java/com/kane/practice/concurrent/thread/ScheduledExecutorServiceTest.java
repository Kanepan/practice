package com.kane.practice.concurrent.thread;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledExecutorServiceTest {


    static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            submit(scheduledExecutorService, finalI);
        }

        scheduledExecutorService.shutdown();

//        submit(scheduledExecutorService, 10001);

        try {
            boolean flag = scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(flag);
            System.out.println(atomicInteger.get());
            if(!flag){
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void submit(ScheduledExecutorService scheduledExecutorService, int finalI) {
        scheduledExecutorService.schedule(() -> {
            System.out.println("Hello World" + Thread.currentThread().getName() + " " + finalI);
            atomicInteger.incrementAndGet();

        }, RandomUtils.nextInt(1, 5), TimeUnit.SECONDS);
    }
}
