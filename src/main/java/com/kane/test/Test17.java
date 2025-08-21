package com.kane.test;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.util.Date;

public class Test17 {
    public static void main(String[] args) throws IOException {
        Date merchantInvalidTime = DateUtil.parseDate("2025-08-18 00:00:00");
        Date cardInvalidTime = DateUtil.parseDate("2025-07-20 00:00:00");

        Integer renewMonths = 0;
        String renewFlag = "不续费";

// 商户到期早于套餐，不续费
        if (merchantInvalidTime.before(cardInvalidTime)) {
            renewFlag = "不续费";
            renewMonths = 0;
        } else {
            // 把年月日分别提取
            int merchantYear = DateUtil.year(merchantInvalidTime);
            int merchantMonth = DateUtil.month(merchantInvalidTime); // 注意：Hutool的month是从0开始的
            int merchantDay = DateUtil.dayOfMonth(merchantInvalidTime);

            int cardYear = DateUtil.year(cardInvalidTime);
            int cardMonth = DateUtil.month(cardInvalidTime);
            int cardDay = DateUtil.dayOfMonth(cardInvalidTime);

            // 计算年月差
            int yearDiff = merchantYear - cardYear;
            int monthDiff = yearDiff * 12 + (merchantMonth - cardMonth);
            int dayDiff = merchantDay - cardDay;

            if (monthDiff == 0 && dayDiff > 0) {
                renewMonths = 1;
            } else if (monthDiff > 0 && dayDiff <= 0) {
                renewMonths = monthDiff;
            } else if (monthDiff > 0) {
                renewMonths = monthDiff + 1;
            } else {
                renewMonths = 0;
            }

            if (renewMonths > 0) {
                renewFlag = "续费";
            }
        }

// 打印结果
        System.out.println("是否续费：" + renewFlag);
        System.out.println("续费月数：" + renewMonths);
    }
}
