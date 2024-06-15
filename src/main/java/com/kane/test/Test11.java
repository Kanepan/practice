package com.kane.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test11 {

    public static void main(String[] args) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        JSONArray ja = list.stream().map(item-> {
            JSONObject jo = new JSONObject();
            jo.put("machineCode", item);
            return jo;
        }).collect(Collectors.toCollection(JSONArray::new));



        System.out.println(ja.toJSONString());
    }
}
