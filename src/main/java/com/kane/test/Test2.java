package com.kane.test;

import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.SupplyResult;
import net.dongliu.requests.Requests;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test2 {


    public static void main(String[] args) {
        Map<String, String> header = new HashMap();
        header.put("Referer", "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 Edg/84.0.522.52");
        header.put("Referer", "https://uac.10010.com/portal/homeLoginNew");

        String url = "https://upay.10010.com/npfweb/npfcardstateweb/card_state_fill.htm";




        SupplyResult<String> r3 = HttpClientUtil.get(url,header, "12");
        System.out.println(r3);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


         url = "https://upay.10010.com/npfweb/NpfWeb/judgeLogin/isLogin.action?redirectKey=BuyCardLoginReturnUrl&time=0.8522659772902395&callback=jQuery112308308927947590301_1596898490308&_=1596898490309";
         r3 = HttpClientUtil.get(url,header, "12");
        System.out.println(r3);
    }
}
