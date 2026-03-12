package com.kane.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.util.Date;

public class Test67 {
    public static void main(String[] args) throws IOException {
//        long offsetTime = 320;
//        System.out.println(System.currentTimeMillis());
//        System.out.println(System.currentTimeMillis() - offsetTime * 60 * 1000);
//        System.out.println(new Date(System.currentTimeMillis() - offsetTime * 60 * 1000));
//
//        System.out.println(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, -3));


        Date now = new Date();

        Date offsetDate = DateUtil.offsetMonth(now, 2);
        System.out.println("时间: " + offsetDate);
        offsetDate = DateUtil.endOfMonth(offsetDate);
        offsetDate = DateUtil.endOfDay(offsetDate);
        System.out.println("时间: " + offsetDate);


        Date monthStart = DateUtil.beginOfMonth(now);

        Date payDeadline = DateUtil.offsetDay(monthStart, 18); // 20号
        payDeadline = DateUtil.endOfDay(payDeadline); // 设置为当天的23:59:59

        System.out.println("支付截止时间: " + payDeadline);
    }
}
