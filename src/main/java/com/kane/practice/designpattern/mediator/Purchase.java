/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.mediator;

public class Purchase extends AbstractColeague{

    public Purchase(Mediator mediator) {
        super(mediator);
    }

    public void buyComputer(int number) {
        //下单后 访问库存
       super.mediator.buyComputer(number);
    }

    public void refuseBuyComputer() {
        System.out.println("不再采购");
    }
}
