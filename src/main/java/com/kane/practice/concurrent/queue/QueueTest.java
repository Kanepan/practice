package com.kane.practice.concurrent.queue;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {
    public static void main(String[] args) {
        linkedBlockingQueueTest();
    }

    private static void  linkedBlockingQueueTest(){
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(10);

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(linkedBlockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        for (int i = 0; i < 10; i++) {

            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(RandomUtils.nextInt(5000, 10000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                linkedBlockingQueue.offer(finalI);

            }).start();
        }

    }
}
