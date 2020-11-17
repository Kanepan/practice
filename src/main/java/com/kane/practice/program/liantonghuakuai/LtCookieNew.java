package com.kane.practice.program.liantonghuakuai;

import com.alibaba.fastjson.JSONObject;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class LtCookieNew {
    private static final Logger logger = LoggerFactory.getLogger(LtCookieNew.class);

    private static final String SEND_URL = "http://localhost:8881/ltcard/session";
    private static final int SLEEP_TIME = 20;//队列满时休眠秒数

    private static String driverAdress;
    private static int proxyNum = 0;
    private static int cookieNum = 0;

    public static void main(String[] args) {
        System.out.println("start.........");


        //启动参数传入cpu核数 或者 cpu核数和驱动地址
        int cpuSize;
        if (args.length == 0) {//本地环境
            cpuSize = 1;
            driverAdress = "/Users/xiaobu/Documents/software/chromedriver";
            driverAdress = "C:\\Users\\Kanep\\Desktop\\chromedriver.exe";

        } else if (args.length == 1) {
            cpuSize = Integer.parseInt(args[0]);
            String osName = System.getProperty("os.name");
            if (osName.contains("Window")) {//没传默认和程序同目录
                driverAdress = "chromedriver.exe";
            } else {
                driverAdress = "chromedriver";
            }
        } else {
            cpuSize = Integer.parseInt(args[0]);
            driverAdress = args[1];
        }
        long l = System.currentTimeMillis();
        logger.warn("初始化数据：cpuSize:{}，driverAdress:{}", cpuSize, driverAdress);

        System.setProperty("webdriver.chrome.driver", driverAdress);

        for (int i = 0; i < cpuSize; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                Object[] proxyInfo = new Object[]{true, null, null};//refresh、host、port
                while (true) {
                    try {
                        if ((boolean) proxyInfo[0]) {
                            //刷新代理
                            boolean b = refreshProxyInfo(proxyInfo);
                            if (!b) {
                                Thread.sleep(3000);
                                continue;
                            }
                            synchronized (LtCookieNew.class) {
                                proxyNum++;
                            }
                        }
                        String phone = "176" + RandomUtils.nextLong(10000000, 99999999);
                        String cardPwd = "981" + RandomUtils.nextLong(10000000, 99999999) + RandomUtils.nextLong(10000000, 99999999);
//                        cardPwd = "9811234567812345678";
                        logger.warn("随机号码：{}，随机卡密：{}", phone, cardPwd);

                        String cookie = getCookie(phone, cardPwd, proxyInfo);
                        if (cookie == null) {
                            continue;
                        }
                        synchronized (LtCookieNew.class) {
                            cookieNum++;
                        }
                        boolean isSleep = sendCookie(cookie);
                        if (isSleep) {
                            logger.warn("队列已满或发送异常，休眠{}秒", SLEEP_TIME);
                            Thread.sleep(SLEEP_TIME * 1000);
                        }
                    } catch (Exception e) {
                        logger.error("线程未知异常，终止生产", e);
                        return;
                    } finally {
                        logger.warn("消耗代理数量：{}，获取到cookie数量：{}，耗时：{}秒", proxyNum, cookieNum, (System.currentTimeMillis() - l) / 1000);
                    }
                }
            }).start();

        }
    }

    public synchronized static boolean refreshProxyInfo(Object[] proxyInfo) {
        try {
//            String proxyUrl = "http://api.wandoudl.com/api/ip?app_key=323ac7c5aab3abef46ed8330297bd15b&pack=209656&num=1&xy=1&type=2&mr=1&";
//            RawResponse send = Requests.get(proxyUrl).send();
//            String readToText = send.readToText();
//            logger.warn("获取代理响应：{} ", readToText);
//            JSONObject jsonObject = JSONObject.parseObject(readToText);
//            int code = jsonObject.getIntValue("code");
//            String msg = jsonObject.getString("msg");
//            if (code != 200 || StringUtils.equals(msg, "cached")) {
//                return false;
//            }
//            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
//            String proxyHost = data.getString("ip");
//            Integer proxyPort = data.getInteger("port");

            String zhiMaUrl = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=330000&city=0&yys=0&port=11&time=1&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=";
            RawResponse send = Requests.get(zhiMaUrl).send();
            String readToText = send.readToText();
            logger.warn("获取代理响应：{}", readToText);
            if (StringUtils.contains(readToText, "code")) {
                return false;
            }
            String[] split = StringUtils.split(readToText, ":");
            String proxyHost = split[0];
            String proxyPort = split[1];
            proxyInfo[0] = false;
            proxyInfo[1] = proxyHost;
            proxyInfo[2] = proxyPort;
            return true;
        } catch (Exception e) {
            logger.error("获取代理异常", e);
            return false;
        }
    }


    //发送cookie，返回是否需要休眠，网络异常或响应失败都当成需要休眠
    public static boolean sendCookie(String cookie) {
        try {
            logger.warn("开始发送cookie：{}", cookie);
//            if (1 == 1) {
//                return false;
//            }
            Map<String, String> map = new HashMap<>();
            map.put("cookie", cookie);
            RawResponse send = Requests.post(SEND_URL).forms(map).timeout(10000, 30000).send();
            if (send.getStatusCode() != 200) {
                logger.error("请求失败，httpCode：{}", send.getStatusCode());
                return true;
            }
            String readToText = send.readToText();
            logger.warn("发送cookie响应：{}", readToText);
            JSONObject jsonObject = JSONObject.parseObject(readToText);
            if (!jsonObject.getBooleanValue("success")) {
                return true;
            }
            return jsonObject.getBooleanValue("module");
        } catch (Exception e) {
            logger.error("发送cookie请求异常", e);
            return true;
        }
    }

    private static WebDriver create(Object[] proxyInfo) {
        ChromeOptions options = new ChromeOptions();
        if (proxyInfo[1] != null) {
            options.addArguments("--proxy-server=http://" + proxyInfo[1] + ":" + proxyInfo[2]);
        }
        //无头浏览器，开发时可注释掉方便查看运行情况
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    //获取cookie
    public static String getCookie(String phone, String cardPwd, Object[] proxyInfo) {
        try {
            //Selenide配置的代理有点毛病，改用手动传入驱动
//            Configuration.browser = "chrome";
//            Configuration.proxyEnabled = true;
//            Configuration.proxyHost = proxyHost;
//            Configuration.proxyPort = proxyPort;
//            Configuration.headless = true;//无头浏览器
//            System.out.println("getcookie:" + phone + "-" + cardPwd + "-" + proxyInfo);


            ChromeOptions options = new ChromeOptions();
            options.addArguments("--proxy-server=http://" + proxyInfo[1] + ":" + proxyInfo[2]);
            //无头浏览器，开发时可注释掉方便查看运行情况
//            options.addArguments("--headless");
            WebDriverRunner.setWebDriver(new ChromeDriver(options));

            //访问充值页
            open("https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
            executeJavaScript("Object.defineProperties(navigator,{webdriver:{get:()=>false}})");
            //切换到充值卡
            $(By.className("cardPayfee")).click();
            //输入账号
            SelenideElement number = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed payNum']/dd[@class='dd']/div[@class='selectNumCond fixed']/div[@class='myNumber']/input[@id='number']"));
            number.setValue(phone);
            //输入卡密
            SelenideElement password = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/div[@id='rechargeModetwo']/dl[@class='dl fixed rechargePassword']/dd[@class='dd ']/div[@class='password relative']/input[@id='passwordInput']"));
            password.setValue(cardPwd);
            //滑块
            SelenideElement verifyCode = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed verifiCode']"));
            if (!verifyCode.isDisplayed()) {
                //若没滑块，先点下一步
                SelenideElement nextBtn02 = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/a[@class='nextBtn nextBtn02']"));
                nextBtn02.click();
                //等待加载状态消失
                $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/span[@class='loading']")).shouldNotBe(Condition.visible);
            }
            if (verifyCode.isDisplayed()) {
                SelenideElement moveBtn = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed verifiCode']/dd[@id='verifyCode']/div[@id='nc_1_wrapper']/div[@id='nc_1_n1t']/span[@id='nc_1_n1z']"));
                //计算移动轨迹并移动
                List<Integer> moveLocus = getMoveLocus(260);
                Actions actions = new Actions(getWebDriver());
                actions.clickAndHold(moveBtn).perform();
                for (Integer locus : moveLocus) {
                    actions.moveByOffset(locus, 0).perform();
                }
                SelenideElement element = $(By.id("verifyCode")).shouldNotHave(Condition.text("加载中"));
                String text = element.getText();
                logger.warn("滑块验证结果：{}", text);
                if (!text.contains("验证通过")) {
                    return null;
                }
//                //点击下一步
                SelenideElement nextBtn02 = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/a[@class='nextBtn nextBtn02']"));
                nextBtn02.click();
                //等待加载状态消失
                $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/span[@class='loading']")).shouldNotBe(Condition.visible);
            }

            Thread.sleep(500);

            //判断提交情况
            String errorText = $(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/div[@class='error']")).getText();
            if (StringUtils.isNotBlank(errorText)) {
                logger.warn("提交失败：{}", errorText);
                if (StringUtils.equals(errorText, "您的操作太频繁，请稍后再试！")) {
                    proxyInfo[0] = true;//刷新代理
                    return null;
                }
                //号码、卡密等问题失败的，cookie依旧有效，继续往下检测
//                return null;
            }
            //校验cookie是否可用
            open("https://upay.10010.com/npfweb/NpfWeb/needCode?pageType=01");
            String needCode = $("body").getText();
            if (StringUtils.equals(needCode, "no")) {
                logger.warn("cookie检测结果：可用");
                Cookie upayUser = getWebDriver().manage().getCookieNamed("upay_user");
                return upayUser.getName() + "=" + upayUser.getValue();
            } else {
                logger.warn("cookie检测结果：不可用");
            }
        } catch (Throwable e) {
            logger.error("获取cookie异常", e);
            proxyInfo[0] = true;//刷新代理
        } finally {
            closeWebDriver();
//            Selenide.clearBrowserCookies();
        }
        return null;
    }

    //移动算法
    private static List<Integer> getMoveLocus(int distance) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < distance; ) {
            int nextInt = RandomUtils.nextInt(100, 150);
            if (i + nextInt > distance) {
                nextInt = distance - i;
            }
            i += nextInt;
            list.add(nextInt);
        }
        return list;
    }

}
