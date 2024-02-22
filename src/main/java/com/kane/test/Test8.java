package com.kane.test;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.TreeMap;

public class Test8 {

    public static void main(String[] args) {
        String key = "5SXDoQJ7LGpdYJ8XVf5xJ36T4xMc6DTA";
        JSONObject jsonObject = new JSONObject();

//        jsonObject.put("goods_refund_id", "1750079383200210945");
//        jsonObject.put("order_id", "1699725537110441985");
//        jsonObject.put("status", "2");

                jsonObject.put("goods_refund_id", "1750079383200210945");
        jsonObject.put("order_id", "1699725537110441985");
        jsonObject.put("status", "3");
        jsonObject.put("result_msg","退款中");

        checkSign(jsonObject, key);
    }

    public static boolean checkSign(JSONObject jsonObject, String privateKey) {
        String sign = jsonObject.getString("sign");

        jsonObject.keySet().remove("sign");

        TreeMap<String, String> treeMap = new TreeMap<>();
        jsonObject.keySet().forEach(key -> {
            treeMap.put(key, jsonObject.getString(key));
        });

        StringBuffer sb = new StringBuffer();
        treeMap.forEach((k, v) -> {
            if (StringUtils.isNotEmpty(v)){
                sb.append(k).append("=").append(v).append("&");
            }
        });
        // 去掉最后一个&
        sb.deleteCharAt(sb.length() - 1);
        if (privateKey != null) {
            sb.append(privateKey);
        }

        System.out.println(SecureUtil.md5(sb.toString()).toLowerCase());
//        return true;
        return SecureUtil.md5(sb.toString()).toLowerCase().equals(sign);
    }
}
