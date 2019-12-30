/*
    自旋锁中，如果不存在线程切换， 则速度飞快. 整体来说还是 ，重入锁优秀，公平和非公平，有几倍的性能差距
 */

package com.kane.practice.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class SpinLock {
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    public void lock(){

        while(!atomicBoolean.compareAndSet(false,true)){
            //do nothing
        }
    }

    public void unlock(){
        atomicBoolean.compareAndSet(true,false);
    }

//    private AtomicReference<Thread> cas = new AtomicReference<Thread>();
//    public void lock() {
//        Thread current = Thread.currentThread();
//        // 利用CAS
//        while (!cas.compareAndSet(null, current)) {
//            // DO nothing
//        }
//    }
//    public void unlock() {
//        Thread current = Thread.currentThread();
//        cas.compareAndSet(current, null);
//    }

    private static SpinLock lock = new SpinLock();

    private static ExecutorService es = Executors.newFixedThreadPool(30);

    public static void main(String[] args) {
        List<Callable<Object>> tasks = new ArrayList();
        long time = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            tasks.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    test2();
                    return null;
                }
            });
        }
        try {
            es.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("costs : "  + (System.currentTimeMillis() - time) + "ms");
        es.shutdown();
    }

    public static void test1(){
        lock.lock();
        try{

            //这里sleep 和不sleep 自旋锁的执行速度天差地别

//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }finally {
            lock.unlock();
        }
    }

    private static ReentrantLock reentrantLock = new ReentrantLock(false);

    public static void test2(){
        reentrantLock.lock();
        try{
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }finally {
            reentrantLock.unlock();
        }
    }
}
