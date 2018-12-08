/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.base;

import java.util.concurrent.*;

public class Test {
    private static final ExecutorService es = new ThreadPoolExecutor(10, 10,
            10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) {
        es.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread over!!!" );

            }
        });

        for(;;){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadPoolExecutor downCallbackPool = (ThreadPoolExecutor) es;
            System.out.println("actCount" + downCallbackPool.getActiveCount());
            System.out.println("poolSize" + downCallbackPool.getPoolSize());
            System.out.println("queueSize" + downCallbackPool.getQueue().size());
        }


    }

}
