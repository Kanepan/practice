/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.mediator;

public class Stock extends AbstractColeague{
    private static int quantity = 100;

    public Stock(Mediator mediator) {
        super(mediator);
    }

    public synchronized void inc(int number) {
        quantity = quantity + number;
        System.out.println(" 库存 数量 为：" + quantity);

    }

    public synchronized void dec(int number) {
        quantity = quantity - number;
        System.out.println(" 库存 数量 为：" + quantity);
    }

    public int getStockNumber() {
        System.out.println(" 库存 数量 为：" + quantity);
        return quantity;
    }

    public synchronized void clearStock() {
        super.mediator.clearStock();
    }

}
