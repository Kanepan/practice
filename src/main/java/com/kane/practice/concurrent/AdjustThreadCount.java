package com.kane.practice.concurrent;


import java.util.concurrent.*;

public class AdjustThreadCount {

    private static final ConcurrentMap<Integer, DealRunnable> threadMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    private int threadCount = 10;
    private int testInt = 0;

    public static void main(String[] args) {
        new AdjustThreadCount().init();
    }

    public void init() {
        adjustThread();
        ses.scheduleWithFixedDelay(() -> {
            adjustThread();
        }, 30, 30, TimeUnit.SECONDS);
    }

    class DealRunnable implements Runnable {
        private volatile boolean runSwitch = true;

        @Override
        public void run() {
            while (runSwitch) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                deal();
            }
        }

        public void stop() {
            runSwitch = false;
        }
    }

    private void adjustThread() {
        int currentCount = threadMap.size();
        int threadCount = threadCount();


        if (threadCount == currentCount) {
            return;
        }

        if (threadCount > currentCount) {
            //新增
            for (int i = currentCount + 1; i < threadCount + 1; i++) {
                int threadIndex = i;
                DealRunnable dealRunnable = new DealRunnable();
                Thread thread = new Thread(dealRunnable, "OrderPull-" + threadIndex);
                threadMap.put(threadIndex, dealRunnable);
                thread.start();
            }
        } else {
            //减少
            for (int i = currentCount; i > threadCount; i--) {
                int threadIndex = i;
                DealRunnable dealRunnable = threadMap.remove(threadIndex);
                if (dealRunnable != null) {
                    dealRunnable.stop();
                }
            }
        }
    }

    private int threadCount() {
        threadCount = testInt++ % 2 == 0 ? 10 : 20;
        return threadCount;
    }

    private void setThreadCount(int count) {
        this.threadCount = count;
    }


    public void deal() {

    }
}
