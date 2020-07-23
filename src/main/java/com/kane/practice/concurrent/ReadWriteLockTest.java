package com.kane.practice.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static ReentrantLock lock  = new ReentrantLock();

    public static void main(String[] args) {


        for (int i = 0; i < 1000; i++) {
            if (i % 10 == 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        System.out.println(writeAndRead());
                        System.out.println(test2());
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        System.out.println(read());
                        System.out.println(test1());
                    }
                }).start();
            }
        }
    }

    private static Object source = new String();

    private static int read() {
        readWriteLock.readLock().lock();
        try {
            return source.hashCode();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private static int writeAndRead() {
        readWriteLock.writeLock().lock();
        try {
            source = new Object();
//            return read();
            return source.hashCode();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private static int test1() {
        lock.lock();
        try {
            return source.hashCode();
        } finally {
            lock.unlock();
        }
    }


    private static int test2() {
        lock.lock();
        try {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            source = new Object();
            return test1();
//            return source.hashCode();
        } finally {
            lock.unlock();
        }
    }

}
