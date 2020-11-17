package com.kane.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Test3 {
    public static void main(String[] args) throws IOException {
        test2();
    }

    private  static void test2(){
        List<String> r = null;
        try {
            r = FileUtils.readLines(new File("C:\\Users\\Kanep\\Desktop\\cc.txt"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String str : r) {
            System.out.println(str.split("\\s+")[1]);
        }
    }

    private static  void test1(){
        List<String> r = null;
        try {
            r = FileUtils.readLines(new File("C:\\Users\\Kanep\\Desktop\\bug.txt"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


        String sss = "select item_uid,card_no,card_pwd from supply_order where gmt_create > '2020-08-17' and supply_trader_id = '436' and user_id = '337' and item_id in (";
        String s2 = "";
        for (String str : r) {
            s2 = s2 + str +",";
        }
        sss = sss + s2 + ")";
        System.out.println(sss);
        System.out.println(r.size());
    }
}
