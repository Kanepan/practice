package com.kane.practice.program.locktool;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalLock {

    private final Cache<String, Lock> lockCache;

    public LocalLock() {
        lockCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    public void executeWithLock(String key, long timeout, TimeUnit unit, Runnable task) throws InterruptedException {
        Lock lock = lockCache.getIfPresent(key);
        if (lock == null) {
            synchronized (this) {
                lock = lockCache.getIfPresent(key);
                if (lock == null) {
                    lock = new ReentrantLock();
                    lockCache.put(key, lock);
                }
            }
        }

        boolean locked = lock.tryLock(timeout, unit);
        if (!locked) {
            System.out.println("Failed to acquire lock for key: " + key);
            return;
        }

        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace(); // or use logger
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        LocalLock lock = new LocalLock();

        a(lock);
        a(lock);
    }

    private static void a(LocalLock lock) {
        new Thread(() -> {
            try {
                lock.executeWithLock("aaa",1, TimeUnit.MINUTES,()->{
                    System.out.println("Executing task with lock");
                    try {
                        Thread.sleep(5000); // Simulate some work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}

