/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.temp;

public class ElementDict {
    private String term;
    private int freq;

    public ElementDict(String term, int freq) {
        this.term = term;
        this.freq = freq;
    }


    public void setFreq (int freq) {
        this.freq = freq;
    }


    public String getTerm() {
        return term;
    }


    public int getFreq() {
        return freq;
    }

}
