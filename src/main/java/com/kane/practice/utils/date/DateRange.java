package com.kane.practice.utils.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

public class DateRange {
    public static void main(String[] args) {
        test1();
    }

    public static void test1(){
        cn.hutool.core.date.DateRange dateRange = DateUtil.range(
                DateUtil.parse("2023-09-25"),
                DateUtil.parse("2023-10-05"),
                DateField.DAY_OF_MONTH
        );

        dateRange.forEach(date -> {
            String formattedDate = DateUtil.format(date, "yyyy-MM-dd");
            System.out.println(formattedDate);
        });
    }
}
