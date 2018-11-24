package com.kane.practice;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        System.out.println("ccccc");
        //1381008024516829
        //244006563144616829
        try {
            Date date = DateUtils.parseDate("2018-11-11 00:00:00","yy-MM-dd hh:mm:ss");
            System.out.println(date.getTime()/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
