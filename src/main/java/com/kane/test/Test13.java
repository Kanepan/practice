package com.kane.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Test13 {

    public static void main(String[] args) {
        String date = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd")   ;
        System.out.println(date);



        System.out.println (DateUtil.parse(date, "yyyy-MM-dd"));

        long no = 8;

        String noStr = String.format("%05d", no);
        System.out.println(noStr);


        String billingDate = "2022-01-30";
        System.out.println(new Test13().isEndOfMonth(billingDate));


        System.out.println(billingDate.replace("-",""));


        String tradeExtInfo = "/612";
        if(StringUtils.isBlank(tradeExtInfo) || !tradeExtInfo.contains("/")){
             System.out.println("err");
             return;
        }
        String curPeriodNumStr = tradeExtInfo.substring(0, tradeExtInfo.indexOf("/"));

            System.out.println(NumberUtil.isNumber(curPeriodNumStr));

            String a = "2026-03-08 - 2027-03-08";

            System.out.println(a.substring(0, a.indexOf(" - ")));

        String b = "202603";

        // 去掉前两位
        System.out.println(b.substring(2));


    }

    private boolean isEndOfMonth(String billingDate){
        Date date = DateUtil.parse(billingDate, "yyyy-MM");
        DateTime endOfMonth = DateUtil.endOfMonth(date);
        return DateUtil.isSameDay(DateUtil.parse(billingDate, "yyyy-MM-dd"), endOfMonth);
    }
}
