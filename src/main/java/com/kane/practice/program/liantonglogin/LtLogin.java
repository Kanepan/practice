package com.kane.practice.program.liantonglogin;

import com.alibaba.fastjson.JSONObject;
import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.SupplyResult;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LtLogin {
    public static void main(String[] args) {
        //login();

        getTidAndC();
    }


    private static String loginUrl = "https://api.weibo.com/oauth2/authorize?client_id=2766276024&redirect_uri=http://uac.10010.com/portal/SinaWeiboLogin&response_type=code";

    private static void login() {
        Map<String, String> loginHeader = new HashMap<>();
        loginHeader.put("Accept", "text/plain, */*; q=0.01");
        loginHeader.put("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
//        loginHeader.put("Host", "upay.10010.com");
        loginHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
//        loginHeader.put("Referer", "https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
        loginHeader.put("Accept-Encoding", "br, gzip, deflate");
        RawResponse needCodeSend = Requests.get(loginUrl).headers(loginHeader).socksTimeout(15000).send();
        String s = needCodeSend.readToText();
        System.out.println("返回：" + s);
        String str = needCodeSend.getHeader("Set-Cookie");
        System.out.println(str);
    }


    private static JSONObject getTidAndC() {
        String url = "https://passport.weibo.com/visitor/genvisitor";
        SupplyResult<String> result = HttpClientUtil.postJson(url, "", "1");
        if (result.isSuccess() || result.getModule() == null) {
            System.out.println(result);
            return null;
        }
        String body = result.getModule();
        body = body.replaceAll("window.gen_callback && gen_callback\\(", "");
        body = body.replaceAll("\\);", "");
        JSONObject json = JSONObject.parseObject(body).getJSONObject("data");
        System.out.println(body);
        return json;
    }


}
