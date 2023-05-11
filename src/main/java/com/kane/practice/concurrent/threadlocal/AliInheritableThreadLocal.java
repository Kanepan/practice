package com.kane.practice.concurrent.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AliInheritableThreadLocal {

    // 实现类使用TTL的实现
    private static final ThreadLocal<InheritableThreadLocalTest.Person> THREAD_LOCAL = new TransmittableThreadLocal<>();
    // 线程池使用TTL包装一把
    private static final ExecutorService THREAD_POOL = TtlExecutors.getTtlExecutorService(Executors.newSingleThreadExecutor());

    public static void main(String[] args) throws Exception {
        THREAD_LOCAL.set(new InheritableThreadLocalTest.Person());

        THREAD_POOL.execute(() -> getAndPrintData());
        TimeUnit.SECONDS.sleep(2);
        InheritableThreadLocalTest.Person newPerson = new InheritableThreadLocalTest.Person();
        newPerson.setAge(100);
        THREAD_LOCAL.set(newPerson); // 给线程重新绑定值

        THREAD_POOL.execute(() -> getAndPrintData());
        TimeUnit.SECONDS.sleep(2);

        InheritableThreadLocalTest.Person newPerson3 = new InheritableThreadLocalTest.Person();
        newPerson3.setAge(66);
        THREAD_LOCAL.set(newPerson3); // 给线程重新绑定值
        THREAD_POOL.execute(() -> getAndPrintData());
    }

    private static InheritableThreadLocalTest.Person getAndPrintData() {
        InheritableThreadLocalTest.Person person = THREAD_LOCAL.get();
        System.out.println("get数据，线程名：" + Thread.currentThread().getName() + "，数据为：" + person + " age:" + person.getAge());
        return person;
    }
}


