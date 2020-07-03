package com.kane.test;

import net.dongliu.requests.Requests;

import java.util.concurrent.CountDownLatch;

public class Test2 {

    //

    final static CountDownLatch order = new CountDownLatch(1);

    public static void main(String[] args) {
        final String url = "http://localhost:8080/callbackTest.do?id=601459&status=1";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    order.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Requests.get(url).send().body());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    order.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Requests.get(url).send().body());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    order.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Requests.get(url).send().body());
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    order.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Requests.get(url).send().body());
//            }
//        }).start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("准备发射");
        order.countDown();


    }
}
