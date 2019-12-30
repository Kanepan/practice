/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.arithmetic.btree;

public interface B {
    public Object get(Comparable key);   //查询
    public void remove(Comparable key);    //移除
    public void insertOrUpdate(Comparable key, Object obj); //插入或者更新，如果已经存在，就更新，否则插入
}
