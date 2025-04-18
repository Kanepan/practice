package com.kane.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.util.Date;

public class Test67 {
    public static void main(String[] args) throws IOException {
        long offsetTime = 320;
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis() - offsetTime * 60 * 1000);
        System.out.println(new Date(System.currentTimeMillis() - offsetTime * 60 * 1000));

        System.out.println(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, -3));
    }
}
