package com.kane.practice.concurrent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class OrderCounter {
    private final static Cache<Long, AtomicInteger> cache = CacheBuilder.newBuilder().expireAfterWrite(11, TimeUnit.SECONDS).build();
    private final static ConcurrentMap<Long, AtomicInteger> map = cache.asMap();

    private final static AtomicInteger a = new AtomicInteger();

    private Long time = timeGen();

    public void inc() {
        long now = timeGen();
        AtomicInteger count = map.get(now);
        if (count == null) {
            count = new AtomicInteger(0);
            AtomicInteger temp = map.putIfAbsent(now, count);
            if (temp != null) {
                count = temp;
            }
        }
        count.incrementAndGet();
    }

    public void inc2() {
        long now = timeGen();
        AtomicInteger count = map.get(now);
        if (count == null) {
            AtomicInteger temp = map.computeIfAbsent(now, k -> new AtomicInteger(0));
            if (temp != null) {
                count = temp;
            }
        }
        count.incrementAndGet();
    }

    public void inc3() {
        a.incrementAndGet();
    }

    public Long getRate2() {
        return a.get() / (timeGen() - time);
    }

    public Double getRate() {
        int secondTime = 10;
        long now = timeGen();
        int count = 0;
        for (int i = 0; i < secondTime; i++) {
            AtomicInteger atomicInteger = map.get(now - i);
            if (atomicInteger == null) {
                continue;
            }
            count = count + atomicInteger.get();
        }
        return Double.parseDouble(count + "") / secondTime;
    }

    protected long timeGen() {
        return System.currentTimeMillis() / 1000;
    }


    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    private static final OrderCounter orderCounter = new OrderCounter();
    private static final ExecutorService es = Executors.newFixedThreadPool(10);
    private static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);

    private static final RateLimiter r = RateLimiter.create(100);

    public static void main(String[] args) {

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("当前rate: " + orderCounter.getRate());
                System.out.println("当前map size: " + map.size());
            }
        }, 5, 5, TimeUnit.SECONDS);

        for (int i = 0; i < 1000000000; i++) {
            r.acquire();
            es.submit(new Runnable() {
                @Override
                public void run() {
                    orderCounter.inc();
                }
            });
        }

    }

}
