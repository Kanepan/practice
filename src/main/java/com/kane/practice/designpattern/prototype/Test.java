/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.designpattern.prototype;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        MyObject myObject = new MyObject("Kane",list);


        System.out.println(myObject);
        MyObject cloneMyObject = myObject.clone();
        System.out.println(cloneMyObject);
        list.add(3);
        System.out.println(myObject);
        System.out.println(cloneMyObject);

        System.out.println("--------------------------deepclone------------");
        MyObject deepCloneMyObject = myObject.deepClone();
        System.out.println(deepCloneMyObject);
        list.add(4);
        System.out.println(myObject);
        System.out.println(deepCloneMyObject);
    }

    static class MyObject implements Cloneable{
        private String name ;
        private List<Integer> list = new ArrayList();

        public MyObject(String name,List<Integer> list ){
            this.name = name;
            this.list = list;
        }


        @Override
        public String toString() {
            return "MyObject{" +
                    "name='" + name + '\'' +
                    ", list=" + list +
                    '}';
        }

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MyObject deepClone(){
            MyObject myObject = null;
            try {
                myObject =  (MyObject)super.clone();
                ArrayList list =  (ArrayList)((ArrayList)this.getList()).clone();
                myObject.setList(list);

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return myObject;
        }

        public MyObject clone(){
            try {
                return (MyObject)super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
