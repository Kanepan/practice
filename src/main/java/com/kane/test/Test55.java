package com.kane.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.List;

public class Test55 {
    public static void main(String[] args) {
        Date date = DateUtil.date();
        List<DateTime> dateList = DateUtil.rangeToList( DateUtil.offsetDay(date, -10),date, DateField.DAY_OF_MONTH);
        for (DateTime dateTime : dateList) {
            String dealDate = dateTime.toDateStr();
            System.out.println(dealDate);
        }
    }
}
