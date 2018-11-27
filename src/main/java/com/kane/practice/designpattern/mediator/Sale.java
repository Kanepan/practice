/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.mediator;

import java.util.Random;

public class Sale extends AbstractColeague{

    public Sale(Mediator mediator) {
        super(mediator);
    }

    public void sellIBMComputer(int number) {
        super.mediator.sellIBMComputer(number);
    }

    public int getSaleStatus() {
        Random rand = new Random(System.currentTimeMillis());
        int saleStatus = rand.nextInt(100);
        System.out.println(" IBM 电脑 的 销售 情况 为：" + saleStatus);
        return saleStatus;
    }

    public void offSale() {
        System.out.println(" 折价 销售 IBM 电脑");
    }

}
