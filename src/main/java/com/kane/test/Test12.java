package com.kane.test;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class Test12 {

    public static void main(String[] args) {
//        BigDecimal amount = NumberUtil.round(new BigDecimal("0.02").multiply(new BigDecimal("0.01")), 2, RoundingMode.HALF_UP);
//        System.out.println(amount);
//        System.out.println(amount.equals(new BigDecimal("0.00")));
//        System.out.println(amount.equals(new BigDecimal("0")));
//        System.out.println(amount.compareTo(BigDecimal.ZERO));
//
//        String str = "as阿斯蒂芬-应分:\\\" + shouldDivisionAmount + \\\"元-实分\\\" + amount + \\\"元\"";
//
//        String[] split = str.split("-");
//        System.out.println(split[0]);
//
//        BigDecimal amount = new BigDecimal("0.02").multiply(new BigDecimal("100"));
//        System.out.println(amount.longValue());

        System.out.println(new BigDecimal("0.00").compareTo(BigDecimal.ZERO) <=0);

        Date date = new Date();
        System.out.println(date);
        System.out.println(convertDate2(date));


        String startTime  = "2025-01-21 11:00:00";
        String endTime = "2025-01-19 10:00:00";

        // 解析字符串为日期对象
        Date startDate = DateUtil.parse(startTime);
        Date endDate = DateUtil.parse(endTime);
        endDate =  DateUtil.endOfDay(endDate);

        // 按毫秒计算差值，结果可为正或负
        long diffInMs = endDate.getTime() - startDate.getTime();

        // 转换为天，保留正负值
        long diffInDays = diffInMs / DateUnit.DAY.getMillis();

        System.out.println("两个时间相差的天数: " + diffInDays);
    }

    private static Date convertDate(Date date) {
        return new Date(date.getTime() - date.getTime() % (24 * 60 * 60 * 1000));
    }

    private static Date convertDate2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
