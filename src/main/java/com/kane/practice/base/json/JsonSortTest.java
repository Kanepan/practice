package com.kane.practice.base.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

public class JsonSortTest {

    public static void main(String[] args) {
        RequestBO  requestBO = new RequestBO();
        requestBO.a = "a";
        requestBO.c = "c";
        requestBO.ab = "ab";
        requestBO.ac = "ac";
        requestBO.da = "da";
        requestBO.db = "db";

        String jsonStr = JSON.toJSONString(requestBO, SerializerFeature.SortField);
        System.out.println(jsonStr);

        String jsonStr2 = JSON.toJSONString(requestBO);
        System.out.println(jsonStr2);
    }

    @Data
    static class RequestBO{
        private String a;
        private String ac;
        private String ab;
        private String db;
        private String da;
        private String c;

    }
}
