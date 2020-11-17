package com.kane.practice.temp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test2 {
    private static final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (1 != 1) {
                    System.out.println("working");
                } else {
                    System.out.println("throw");
                    throw new RuntimeException("xxx");
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
