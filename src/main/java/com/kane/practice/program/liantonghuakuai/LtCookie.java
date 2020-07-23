package com.kane.practice.program.liantonghuakuai;

import com.alibaba.fastjson.JSONObject;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * <dependency>
 * <groupId>org.seleniumhq.selenium</groupId>
 * <artifactId>selenium-server</artifactId>
 * <version>3.0.1</version>
 * </dependency>
 */

public class LtCookie {

    public static boolean flushProxy = false;

    public static void main(String[] args) {
        Set<String> cookieSet = new HashSet<>();
        String phone = "17601315116";
        String cardPwd = "9807852471543586468";
        String proxyIp = null;
        Integer proxyPort = null;
        for (int i = 0; i < 3; i++) {
            if (flushProxy) {
                String proxyUrl = "http://api.wandoudl.com/api/ip?app_key=323ac7c5aab3abef46ed8330297bd15b&pack=209656&num=1&xy=1&type=2&mr=1&";
                RawResponse send = Requests.get(proxyUrl).send();
                JSONObject jsonObject = JSONObject.parseObject(send.readToText());
                int code = jsonObject.getIntValue("code");
                if (code != 200) {
                    continue;
                }
                JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                proxyIp = data.getString("ip");
                proxyPort = data.getInteger("port");
                System.out.println("获取到代理|" + proxyIp + ":" + proxyPort);
                flushProxy = false;
            }
            String cookie = getCookie(phone, cardPwd, proxyIp, proxyPort);
            if (cookie == null) {
                continue;
            }
            System.out.println("获取到cookie:" + cookie);
            //cookie可用性校验
            String needCodeUrl = "https://upay.10010.com/npfweb/NpfWeb/needCode?pageType=01";
            Map<String, String> needCodeHeader = new HashMap<>();
            needCodeHeader.put("Cookie", cookie);
            needCodeHeader.put("Accept", "text/plain, */*; q=0.01");
            needCodeHeader.put("Accept-Language", "zh-cn");
            needCodeHeader.put("Host", "upay.10010.com");
            needCodeHeader.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
            needCodeHeader.put("Referer", "https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
            needCodeHeader.put("Accept-Encoding", "br, gzip, deflate");
            needCodeHeader.put("Connection", "keep-alive");
            needCodeHeader.put("X-Requested-With", "XMLHttpRequest");
            RawResponse needCodeSend = Requests.get(needCodeUrl).headers(needCodeHeader).socksTimeout(15000).send();
            String s = needCodeSend.readToText();
            if (Objects.equals(s, "yes\n")) {
                //todo cookie不可用
                System.out.println("cookie可用性校验结果：不可用");
            } else {
                System.out.println("cookie可用性校验结果：可用");
                cookieSet.add(cookie);
            }
            System.out.println("当前cookie列表：" + cookieSet);
        }
    }

    public static String getCookie(String phone, String cardPwd, String proxyIp, Integer proxyPort) {
        WebDriver webDriver = null;
        try {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kanep\\Desktop\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("'disable-infobars'");

            if (proxyIp != null && proxyPort != null) {
                options.addArguments("--proxy-server=http://" + proxyIp + ":" + proxyPort);
            }
            //不知道为啥无头浏览器滑块验证元素出不来，只有开界面才能跑
//            options.addArguments("--headless");
            webDriver = new ChromeDriver(options);

            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
            try {
                //访问充值页
                webDriver.get("https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm");
            } catch (Exception e) {
                //移除代理
                flushProxy = true;
                System.out.println("访问充值页超时");
                return null;
            }
            //切换到充值卡
            webDriver.findElement(By.className("cardPayfee")).click();
            //输入账号
            WebElement number = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed payNum']/dd[@class='dd']/div[@class='selectNumCond fixed']/div[@class='myNumber']/input[@id='number']"));
            number.sendKeys(phone);
            //输入卡密
            WebElement password = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/div[@id='rechargeModetwo']/dl[@class='dl fixed rechargePassword']/dd[@class='dd ']/div[@class='password relative']/input[@id='passwordInput']"));
            password.sendKeys(cardPwd);
            //点击下一步
            Thread.sleep(1000);
            WebElement nextBtn02 = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/a[@class='nextBtn nextBtn02']"));
            nextBtn02.click();
            //等待加载状态消失
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/span[@class='loading']")));
            //判断是否存在错误
            String errorText = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/div[@class='error']")).getText();
            if (StringUtils.isNotBlank(errorText)) {
                System.out.println("提交失败:" + errorText);
                if (errorText.contains("验证码校验异常")) {
                    //移除代理
                    flushProxy = true;
                }
                return null;
            }
            //等待出现弹窗和滑块
            WebElement moveBtn;
            webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("tcaptcha_popup"));
            moveBtn = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("tcaptcha_drag_thumb")));
            WebElement oriImg = webDriver.findElement(By.id("slideBkg"));
            //不并发，直接用时间戳
            String imgPath = "img/" + System.currentTimeMillis() + ".png";
            try {
                //下载滑块图片
                FileUtils.copyInputStreamToFile(new URL(oriImg.getAttribute("src")).openStream(), new File(imgPath));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("获取滑块图片失败");
                return null;
            }
            //获取移动距离
            int width = oriImg.getSize().getWidth();
            Integer calcMoveDistance = calcMoveDistance(imgPath, 50, width);
            if (calcMoveDistance == null) {
                System.out.println("获取移动距离失败");
                return null;
            }
            //获取移动轨迹并移动
            List<Integer> moveLocus = getMoveLocus(calcMoveDistance);
            Actions actions = new Actions(webDriver);
            actions.clickAndHold(moveBtn).perform();
            for (Integer locus : moveLocus) {
                actions.moveByOffset(locus, 0).perform();
            }
            actions.release(moveBtn).perform();
            for (int i = 0; i < 10; i++) {
                String tcaptchaNoteText = webDriver.findElement(By.id("tcaptcha_note")).getText();
                String textText = webDriver.findElement(By.xpath("//div[@id='tcaptcha_chabie']/p[@class='tcaptcha-cover-text']")).getText();
                String successText = webDriver.findElement(By.id("tcaptcha_cover_success")).getText();
                if (StringUtils.isBlank(tcaptchaNoteText) && StringUtils.isBlank(textText) && StringUtils.isBlank(successText)) {
                    //滑块还没出现，等一会重试
                    Thread.sleep(500);
                    continue;
                }
                if (StringUtils.isNotBlank(tcaptchaNoteText)) {
                    System.out.println("滑块验证失败:" + tcaptchaNoteText);
                    return null;
                }
                if (StringUtils.isNotBlank(textText)) {
                    System.out.println("滑块验证失败:" + textText);
                    if (textText.contains("网络恍惚了一下")) {
                        //移除代理
                        flushProxy = true;
                    }
                    return null;
                }
                if (!successText.contains("简直比闪电还快") && !successText.contains("这速度简直完美") && !successText.contains("验证成功，精准无敌了")) {
                    System.out.println("未知情况，疑似网厅变动:" + successText);
                    return null;
                }
                //切换回外面
                webDriver.switchTo().defaultContent();
                //等iframe消失
                webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("tcaptcha_popup")));
                //等待加载状态消失
                webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/span[@class='loading']")));
                //判断是否错误
                String errorText1 = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@class='coatLayer']/div[@id='cotainer']/div[@class='rightCont']/div[@class='rightCont-infor']/div[@class='infor-fill pr']/form[@id='cellform']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/div[@class='error']")).getText();
                if (StringUtils.isNotBlank(errorText1)) {
                    System.out.println("滑块通过后异常:" + errorText1);
                    return null;
                }
                Set<String> windowHandles = webDriver.getWindowHandles();
                if (windowHandles.size() == 1) {
                    System.out.println("未获取到确认窗口，疑似网厅变动");
                    return null;
                }
                for (String windowHandle : windowHandles) {
                    if (windowHandle.equals(webDriver.getWindowHandle())) {
                        continue;
                    }
                    //切换到确认窗口
                    webDriver.switchTo().window(windowHandle);
                    //点确认缴费按钮
                    WebElement nextBtn03 = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@id='cotainer']/div[@class='content fixed']/div[@class='rightCont']/form[@id='cellformconfirm']/div[@class='rightCont-infor']/div[@class='infor-fill']/dl[@class='dl fixed submitData']/dd[@class='dd btndd']/a[@class='confirmRecharge nextBtn03']"));
                    nextBtn03.click();
                    String inforFill = webDriver.findElement(By.xpath("/html/body[@id='Body_a']/div[@id='content']/div[@id='wapContent']/div[@id='cotainer']/div[@class='content fixed']/div[@class='rightCont']/form[@id='cellformconfirm']/div[@class='rightCont-infor']/div[@class='infor-fill']")).getText();
                    System.out.println("充值结果:" + inforFill);
                    Cookie upayUser = webDriver.manage().getCookieNamed("upay_user");
                    return upayUser.getName() + "=" + upayUser.getValue();
                }
            }
            System.out.println("滑块验证次数超限");
            return null;
        } catch (TimeoutException e) {
            //移除代理
            flushProxy = true;
            System.out.println("操作超时");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("未知异常");
        } finally {
            if (webDriver != null) webDriver.quit();
        }
        return null;
    }


    private static Integer calcMoveDistance(String filePath, int startDistance, float bgWrapWidth) {
        BufferedImage fullBI;
        try {
            fullBI = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("读取滑块图片失败");
            return null;
        }
        for (int w = 340; w < fullBI.getWidth() - 18; w++) {
            int whiteLineLen = 0;
            for (int h = 0; h < fullBI.getHeight(); h++) {
                if (isBlack28(fullBI, w, h) && isWhite(fullBI, w, h)) {
                    whiteLineLen++;
                } else {
                    continue;
                }
                if (whiteLineLen >= 50) {
                    return (int) ((w - startDistance) / (fullBI.getWidth() / bgWrapWidth));
                }
            }
        }
        return null;
    }

    /**
     * 当前点的后28个是不是黑色
     *
     * @return 后28个中有80%是黑色返回true, 否则返回false
     */
    private static boolean isBlack28(BufferedImage fullBI, int w, int h) {
        int[] fullRgb = new int[3];
        double blackNum = 0;
        int num = Math.min(fullBI.getWidth() - w, 28);
        for (int i = 0; i < num; i++) {
            fullRgb[0] = (fullBI.getRGB(w + i, h) & 0xff0000) >> 16;
            fullRgb[1] = (fullBI.getRGB(w + i, h) & 0xff00) >> 8;
            fullRgb[2] = (fullBI.getRGB(w + i, h) & 0xff);
            if (isBlack(fullRgb)) {
                blackNum = blackNum + 1;
            }
        }

        return blackNum / num > 0.8;
    }

    private static boolean isWhite(BufferedImage fullBI, int w, int h) {
        int[] fullRgb = new int[3];
        fullRgb[0] = (fullBI.getRGB(w, h) & 0xff0000) >> 16;
        fullRgb[1] = (fullBI.getRGB(w, h) & 0xff00) >> 8;
        fullRgb[2] = (fullBI.getRGB(w, h) & 0xff);

        return isWhite(fullRgb);
    }

    private static boolean isWhite(int[] fullRgb) {
        return (Math.abs(fullRgb[0] - 0xff) + Math.abs(fullRgb[1] - 0xff) + Math.abs(fullRgb[2] - 0xff)) < 125;
    }

    private static boolean isBlack(int[] fullRgb) {
        return fullRgb[0] * 0.3 + fullRgb[1] * 0.6 + fullRgb[2] * 0.1 <= 125;
    }

    //移动算法
    private static List<Integer> getMoveLocus(int distance) {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < distance; ) {
            int nextInt = random.nextInt(30) + 10;
            if (i + nextInt > distance) {
                nextInt = distance - i;
            }
            i += nextInt;
            list.add(nextInt);
        }
        return list;
    }

}
