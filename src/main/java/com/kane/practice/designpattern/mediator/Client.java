/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.mediator;

public class Client {

    public static void main(String[] args) {
        Mediator mediator = new Mediator();
        Purchase purchase = new Purchase(mediator);
        Sale sale = new Sale(mediator);
        Stock stock = new Stock(mediator);

        //采购 人员 采购 电脑
        System.out.println("------ 采购 人员 采购 电脑--------");
        purchase.buyComputer(100);
        //销售 人员 销售 电脑
        System.out.println("------ 销售 人员 销售 电脑--------");

        sale.sellIBMComputer(1);
        //库房 管理 人员 管理 库存
        System.out.println("------ 库房 管理 人员 清库 处理--------");

        stock.clearStock();
    }
}
