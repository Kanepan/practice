package com.kane.practice.utils.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.text.DateFormat;
import java.util.List;

public class DateTest {

    public static void main(String[] args) {
        List<DateTime> list = DateUtil.rangeToList(DateUtil.parse("2022-11", "yyyy-MM"), DateUtil.parse("2023-05", "yyyy-MM"), DateField.MONTH);

        for (DateTime dateTime : list) {
            System.out.println(DateUtil.format(dateTime, "yyyy-MM"));
            System.out.println(dateTime.toString());
        }
    }
}
