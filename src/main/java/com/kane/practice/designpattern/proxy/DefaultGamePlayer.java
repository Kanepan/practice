/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.proxy;

public class DefaultGamePlayer implements GamePlayer {
    private String name;

    public DefaultGamePlayer(String name){
        this.name = name;
    }

    @Override
    public void login(String name, String password) {
        System.out.println("登陆");
    }

    @Override
    public void killBoss() {
        System.out.println("杀BOSS");
    }

    @Override
    public void upgrade() {
        System.out.println("升级");
    }
}
