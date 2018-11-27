/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.mediator;

public class Mediator {
    private Stock stock = null;
    private Purchase purchase = null;
    private Sale sale = null;

    public Mediator() {
        purchase = new Purchase( this);
        sale = new Sale( this);
        stock = new Stock( this);
    }

    public void sellIBMComputer(int number) {
        if (stock.getStockNumber() < number) {
            //库存 数量 不够 销售
            purchase.buyComputer(number);
        }
        System.out.println(" 销售 IBM 电脑" + number + " 台");
        stock.dec(number);

    }

    public synchronized void clearStock() {
        System.out.println(" 清理 存货 数量 为：" + stock.getStockNumber());
        sale.offSale();
        purchase.refuseBuyComputer();

    }

    public void buyComputer(int number) {
        //下单后 访问库存
        //电脑 的 销售 情况
        int saleStatus = sale.getSaleStatus();
        if (saleStatus > 80) {

            //销售 情况 良好 System. out. println(" 采购 IBM 电脑:"+ number + "台");
            stock.inc(number);
        } else {
            System.out.println("销售低于预期");
            //销售 情况 不好
            int buyNumber = number / 2;
            //折半 采购
            System.out.println(" 采购 IBM 电脑：" + buyNumber + "台");
            stock.inc(buyNumber);
        }
    }


}
