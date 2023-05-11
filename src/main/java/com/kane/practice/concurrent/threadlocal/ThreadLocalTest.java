package com.kane.practice.concurrent.threadlocal;

/**
 * @program: practice-demos
 * @description: ThreadLocalTest
 * @url: https://www.cnblogs.com/dolphin0520/p/3920407.html
 * @create: 2021-07-27 22:00
 */
public class ThreadLocalTest {
    private static ThreadLocal<String> localVar = new ThreadLocal<String>();

    static void print(String str) {
        //打印当前线程中本地内存中本地变量的值
        System.out.println(str + " :" + localVar.get());
        //清除本地内存中的本地变量
        localVar.remove();
    }
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            ThreadLocalTest.localVar.set("local_A");
            print("A");
            //打印本地变量
            System.out.println("after remove : " + localVar.get());

        },"A").start();

        new Thread(() -> {
            ThreadLocalTest.localVar.set("local_B");
            print("B");
            System.out.println("after remove : " + localVar.get());

        },"B").start();
    }

}
