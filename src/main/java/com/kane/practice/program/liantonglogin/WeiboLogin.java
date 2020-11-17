package com.kane.practice.program.liantonglogin;

import com.kane.practice.utils.http.ProxyInfo;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeiboLogin {

    public static void main(String[] args) throws Exception {
        processing();
    }

    public static void processing() throws Exception {
        ProxyInfo proxyInfo = getProxy();
        WebDriver webDriver = init(proxyInfo);

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        sleep(4000);
        WebElement loginElement = webDriver.findElement(By.id("loginName"));
        Actions actions = new Actions(webDriver);

//        actions.moveToElement(loginElement);
//        actions.click(loginElement).perform();
//        actions.sendKeys("hhg531@163.com").perform();
        loginElement.sendKeys("jwf_008@163.com");

        WebElement passwordElement = webDriver.findElement(By.id("loginPassword"));
        passwordElement.sendKeys("pslov232ed");

        WebElement loginActionElement = webDriver.findElement(By.id("loginAction"));
        loginActionElement.click();

        sleep(5000);

        WebElement captcha = webDriver.findElement(By.id("embed-captcha"));
        captcha.click();


        sleep(3000);


        sliding(webDriverWait, webDriver);
    }

    private static ProxyInfo getProxy() {
        ProxyInfo proxyInfo = new ProxyInfo();
        String url = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=&city=0&yys=0&port=11&time=1&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=";
        RawResponse rawResponse = Requests.get(url).send();
        String body = rawResponse.readToText().trim();
        proxyInfo.setProxyHost(body.split(":")[0]);
        proxyInfo.setProxyPort(Integer.parseInt(body.split(":")[1]));
        return proxyInfo;
    }


    private static void sliding(WebDriverWait webDriverWait, WebDriver webDriver) {

//        moveBtn = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.className("geetest_slider_button")));

        sleep(5000);
        WebElement moveBtn = webDriver.findElement(By.className("geetest_slider_button"));
        WebElement canvas = webDriver.findElement(By.className("geetest_canvas_slice")); //geetest_canvas_slice geetest_absolute
        //不并发，直接用时间戳
        String imgPath = "img/" + System.currentTimeMillis() + ".png";
        File screenShot = canvas.getScreenshotAs(OutputType.FILE);
        try {
            //下载滑块图片
            FileUtils.copyFile(screenShot, new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取滑块图片失败");
            return;
        }

        //获取移动距离
        int width = canvas.getSize().getWidth();

        Integer calcMoveDistance = calcMoveDistance(imgPath, 50, width);
        if (calcMoveDistance == null) {
            System.out.println("获取移动距离失败");
            return;
        }

        System.out.println("移动距离 :" + calcMoveDistance);

        //获取移动轨迹并移动
        List<Integer> moveLocus = getMoveLocus(calcMoveDistance);
        Actions actions = new Actions(webDriver);
        actions.clickAndHold(moveBtn).perform();
        for (Integer locus : moveLocus) {
            actions.moveByOffset(locus, 0).perform();
        }
        actions.release(moveBtn).perform();
    }


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

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static WebDriver init(ProxyInfo proxyInfo) {
        WebDriver webDriver = null;
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kanep\\Desktop\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("'disable-infobars'");
        if (proxyInfo != null) {
            options.addArguments("--proxy-server=http://" + proxyInfo.getProxyHost() + ":" + proxyInfo.getProxyPort());
        }
        //不知道为啥无头浏览器滑块验证元素出不来，只有开界面才能跑
//            options.addArguments("--headless");

        webDriver = new ChromeDriver(options);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        try {
            //访问充值页
            webDriver.get("https://passport.weibo.cn/signin/login");
        } catch (Exception e) {
            System.out.println("访问充值页超时");
            return null;
        }
        return webDriver;
    }


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

}
