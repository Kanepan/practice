package com.kane.practice.temp;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String uid = "16334652345";
        if (!Pattern.matches("^((13[0-9])|(14[5-7,9])|(15[0-3,5-9])|(17[0-8])|(18[0-9])|(16[5-7,2])|198|195|191|199|(147))\\d{8}$",
                uid)) {
            System.out.println(false);
            return;
        }
        System.out.println(true);
    }
}
