package com.kane.practice.concurrent.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AliThreadLocalTest {
    TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    // private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
    private static final ExecutorService THREAD_POOL = TtlExecutors.getTtlExecutorService(Executors.newSingleThreadExecutor());

    public static void main(String[] args) throws InterruptedException {

        new AliThreadLocalTest().test2();
    }

    //test1
    public void test1() {
        context.set("value-set-in-parent");
        // 在子线程中可以读取，值是"value-set-in-parent"
        new Thread(() -> {
            getAndPrint();
        }).start();
    }

    String value = "value-set-in-parent";

    public void test2() throws InterruptedException {
        context.set(value);

        THREAD_POOL.execute(() -> {
            getAndPrint();
        });

        Thread.sleep(200);
        System.out.println("update");
        context.set("update value in parent");

        THREAD_POOL.execute(() -> {
            getAndPrint();
        });
    }

    public void getAndPrint() {
        String value = context.get();
        System.out.println("value:" + value);
    }
}
