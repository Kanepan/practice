package com.kane.test;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.util.Date;

public class Test68 {
    public static void main(String[] args) throws IOException {
        int baseDayNum = 365;


        String year = "2024";
        Date yearDate = DateUtil.parseDate(year + "-01-01");
        Date machineStartTime = DateUtil.parseDate("2025-11-30");
        String startDay = null;
        if (yearDate.after(machineStartTime)) {
            startDay = year + "-01-01";
        } else {
            startDay = DateUtil.formatDate(machineStartTime);
            if (DateUtil.year(machineStartTime) != Integer.parseInt(year)) {
                baseDayNum = 0;
            } else {
                baseDayNum = 365 - DateUtil.dayOfYear(machineStartTime) + 1;
            }
        }

        System.out.println(baseDayNum);
    }
}
