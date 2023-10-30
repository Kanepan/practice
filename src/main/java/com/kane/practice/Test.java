package com.kane.practice;

import com.alibaba.fastjson.JSON;
import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.SupplyResult;
import com.kane.practice.utils.security.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private static RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').build();

    public static void main(String[] args) {
        System.out.println("ccccc");
        //1381008024516829
        //244006563144616829
        try {
            Date date = DateUtils.parseDate("2018-11-11 00:00:00", "yy-MM-dd hh:mm:ss");
            System.out.println(date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(randomStringGenerator.generate(2));

        Map<String, String> map = new HashMap<>();
        map.put("Proxy-Authorization:Basic", Base64.encode("3389775708@qq.com:12345678aBC".getBytes()));
        SupplyResult<String> r = HttpClientUtil.get("http://api.wandoudl.com//api/whitelist/update?app_key=60ac8cfd2e34f8d39874074a5cc4e15b&ip=115.230.120.61", map, null);
        System.out.println(r);


        //读取某个文件夹下面的所有文件 使用ioutils
        File file = new File("D:\\test");
        File[] files = file.listFiles();

        for (File file2 : files) {
            System.out.println(file2.getName());
        }

        System.out.println(r);
    }

}
