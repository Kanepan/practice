package com.kane.practice.concurrent.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 只有在创建线程的时候，才会继承，所以 jdk的用处不大
 */
public class InheritableThreadLocalTest {


    private static final ThreadLocal<Person> THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);
//    private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {
        InheritableThreadLocalTest testThreadLocal = new InheritableThreadLocalTest();
        testThreadLocal.fun1();
    }

    public void fun1() throws InterruptedException {
        THREAD_LOCAL.set(new Person());

        THREAD_POOL.execute(() -> getAndPrintData());
        TimeUnit.SECONDS.sleep(2);
        Person newPerson = new Person();
        newPerson.setAge(100);
        THREAD_LOCAL.set(newPerson); // 给线程重新绑定值


        THREAD_POOL.execute(() -> getAndPrintData());
        TimeUnit.SECONDS.sleep(2);

        Person newPerson3 = new Person();
        newPerson3.setAge(66);
        THREAD_LOCAL.set(newPerson3); // 给线程重新绑定值
        THREAD_POOL.execute(() -> getAndPrintData());
    }


    private void setData(Person person) {
        System.out.println("set数据，线程名：" + Thread.currentThread().getName());
        THREAD_LOCAL.set(person);
    }

    private Person getAndPrintData() {
        Person person = THREAD_LOCAL.get();
        System.out.println("get数据，线程名：" + Thread.currentThread().getName() + "，数据为：" + person + " age:" + person.getAge());
        return person;
    }


    public static class Person {
        private Integer age = 18;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

}
