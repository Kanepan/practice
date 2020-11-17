package com.kane.practice.program.checkcode;

import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.SupplyResult;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TujianCheckcode {
    public static void main(String[] args) {
//        File file = img();
//        check(file);

        String url = "https://login.sina.com.cn/cgi/pin.php?r=87452165&s=0";
        System.out.println(HttpClientUtil.getImg(url).getModule());
    }

    private static void check(File file) {
        String url = "http://api.ttshitu.com/create.json";
        Map<String, String> map = new HashMap();
        map.put("username", "Kanepan");
        map.put("password", "12345678Aa");
        map.put("typeid", "");
        map.put("angle", "");
        map.put("typename", "");
        map.put("remark", "");

        String result = postFile(url, map, "image", file);
        System.out.println(result);
    }

    private static File img() {
        String url = "https://login.sina.com.cn/cgi/pin.php?r=87452165&s=0";
        RawResponse rawResponse = Requests.get(url).send();
        InputStream i = rawResponse.body();
        try {
            File file = new File("temp/" + System.currentTimeMillis() + ".jpg");
            FileUtils.copyInputStreamToFile(i, file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postFile(String url, Map<String, String> parmas, String fileName, File file) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            //服务器地址
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addBinaryBody("image", file);
            if (parmas != null) {
                for (Map.Entry<String, String> entry : parmas.entrySet()) {
                    mEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(mEntityBuilder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
                // 消耗掉response
                EntityUtils.consume(resEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpclient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }
}

