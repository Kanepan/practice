package com.kane.practice.program.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PwdValidate {
    public static void main(String[] args) {
        String password = "23432AAAaaa";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }

         password = "3434324!!a";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }


         password = "AAAAA!!a!";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }

        password = "AAAAA!!aaa";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }

         password = "&&&111AAA";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }

        password = "&&&111BBBaaa";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }


         password = "23432aaaaaa";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }

        password = "23432!!@#!@#@";

        if (validatePassword(password)) {
            System.out.println("密码验证通过");
        } else {
            System.err.println("密码不符合规则");
        }
    }
//     static final String regex = "^(?![A-Za-z0-9]+$)(?![A-Za-z!@#$%^&*]+$)(?![0-9!@#$%^&*]+$)[A-Za-z0-9!@#$%^&*]{8,}$";


    // 正则判断密码大写字母、 小写字母、数字、半角特殊符号(!@#$%^&* )中三种或三种以上组成，最小长度为8位 ,不包含空格
    static final String regex = "^(?![A-Za-z0-9]+$)(?![A-Za-z!@#$%^&*]+$)(?![0-9a-z!@#$%^&*]+$)[A-Za-z0-9!@#$%^&*]{8,}$";
    public static boolean validatePassword(String password) {
        String pattern = "^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])|(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])|(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]))[A-Za-z\\d!@#$%^&*]{8,}$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
