package com.kane.practice.base.date;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DateRange {

    public static void main(String[] args) {
        List<String> monthList = getMonthsFromStartToNow("2024-02");
        System.out.println(monthList);
    }

    public static List<String> getMonthsFromStartToNow(String startMonth) {
        List<String> months = new ArrayList<>();
        YearMonth start = YearMonth.parse(startMonth);
        YearMonth now = YearMonth.now();

        while (!start.isAfter(now)) {
            months.add(start.toString()); // 格式为 yyyy-MM
            start = start.plusMonths(1); // 下一个月
        }
        return months;
    }
}
