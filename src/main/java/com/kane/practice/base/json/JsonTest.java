package com.kane.practice.base.json;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonTest {

    public static void main(String[] args) {
        // 模拟业务对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientId", "123456");
        jsonObject.put("orderId", "order123");
        jsonObject.put("reasonImg", Arrays.asList("img1.jpg", "img2.jpg"));

        // 转换为 Map<String, String>
        Map<String, String> paramMap = new LinkedHashMap<>();

        jsonObject.forEach((k, v) -> {
            if (v != null && StringUtils.isNotBlank(v.toString())) {
                paramMap.put(k, v.toString()); // List会变为 "[img1.jpg, img2.jpg]"
            }
        });

        // 打印结果
        paramMap.forEach((k, v) -> System.out.println(k + "=" + v));

        // 拼接成 URL 参数字符串（可选）
        String paramString = paramMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        System.out.println("参数字符串：" + paramString);
    }
}
