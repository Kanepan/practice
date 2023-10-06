package com.kane.practice.newfeature.jdk21;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadTest {


    //virtual thread
    //https://www.baeldung.com/java-virtual-threads
    public static void test1(){
//        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            executor.submit(() -> {
//                System.out.println("Hello from virtual thread");
//            });
//        }
    }

    public static void main(String[] args) {
        test1();
        System.out.println("Hello World!");
    }
}
