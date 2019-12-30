/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.concurrent;

public class Termination {
    private int v;
    public void runTest() throws InterruptedException   {
        Thread workerThread = new Thread( () -> {
            while(v == 0) {
                // spin
                System.out.println(1);
            }
        });
        workerThread.start();
        Thread.sleep(1);
        v = 1;
        workerThread.join();  // test might hang up here
    }
    public static void main(String[] args)  throws InterruptedException {
        System.out.println("start");
        for(int i = 0 ; i < 1000 ; i++) {
            new Termination().runTest();
        }
    }
}
