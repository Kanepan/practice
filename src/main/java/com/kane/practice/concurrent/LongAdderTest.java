/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.concurrent;

import java.util.concurrent.atomic.LongAdder;

/**
 * 把并发分担到多个原子变量上，让多个线程并发的对不同的原子变量进行操作，然后获取计数时候在把所有原子变量的计数和累加。
 */
public class LongAdderTest {
    private final static LongAdder longAdder = new LongAdder();

    public static void main(String[] args) {
        longAdder.increment();
        System.out.println(longAdder.intValue());
    }

}
