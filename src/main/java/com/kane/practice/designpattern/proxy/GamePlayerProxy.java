/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.proxy;

public class GamePlayerProxy implements  GamePlayer,Proxy{
    private GamePlayer gamePlayer;

    public GamePlayerProxy(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void login(String name, String password) {
        gamePlayer.login(name,password);
    }

    @Override
    public void killBoss() {
        gamePlayer.killBoss();
    }

    @Override
    public void upgrade() {
        gamePlayer.upgrade();
    }

    @Override
    public void billing() {
       System.out.println("代理计费100元");
    }
}
