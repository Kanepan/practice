package com.kane.practice.base;

import java.util.concurrent.CountDownLatch;

/**
 * @author kanepan
 */
public class CustomRandom {
    private long seed;
    private static final long a = 25214903917L;
    private static final long c = 11;
    private static final long m = (1L << 48);

    public CustomRandom(long seed) {
        this.seed = seed;
    }

    public int nextInt(int bound) {
        seed = (a * seed + c) % m;
        return (int)(Math.abs(seed) % bound);
    }

    public static void main(String[] args) {
        long nanoTime = System.nanoTime();
        CustomRandom random1 = new CustomRandom(nanoTime);
//        CustomRandom random2 = new CustomRandom(nanoTime + 1); // Slightly different seed

//        for (int i = 0; i < 10; i++) {
//            System.out.println("Random1: " + random1.nextInt(100));
////            System.out.println("Random2: " + random2.nextInt(100));
//        }
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                System.out.println("Random1: " + random1.nextInt(100));
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
