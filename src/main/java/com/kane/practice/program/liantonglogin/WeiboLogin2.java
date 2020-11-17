package com.kane.practice.program.liantonglogin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.HttpResponse;
import com.kane.practice.utils.http.SupplyResult;
import com.kane.practice.utils.security.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

public class WeiboLogin2 {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    private static String client = "1";

    public static void main(String[] args) throws Exception {

        Map<String, String> header3 = new HashMap();

        header3.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 Edg/84.0.522.52");
        header3.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");


        SupplyResult<String> rrrr = HttpClientUtil.get("https://login.sina.com.cn/signup/signin.php?r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode", header3, client);
//
//        System.out.println(rrrr);


        //登录账号和密码
        String account = "huangpian1165@163.com";
        String pwd = "hgtgdgd3pm";

//        account = "tusdrnbk35439@sina.com";
//        pwd = "kby950nit";
//
//        account = "ktxstnax50685@sina.com";
//        pwd = "ssw157tbf";

//        account = " lyywmhvp64071@sina.com";
//        pwd = "xhl632iwy";


        //新浪微博登录
        SupplyResult<String> result = login(account, pwd);
//        String cookie = result.getCookie();
//        cookie = getSUB(cookie);
//        System.out.println("cookie:" + cookie);


        //
        JSONObject jo = JSONObject.parseObject(result.getModule());


        Map<String, String> header = new HashMap();
        header.put("Referer", "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 Edg/84.0.522.52");
        //访问【文章同学】的新浪微博首页
//        String html = visitWenZhang();
//
//        //把返回的html内容写入文件，方便打开进行验证是否正确返回
//        System.out.println(html);


        Thread.sleep(100);

        header.put("Referer", "https://login.sina.com.cn/signup/signin.php?r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
        String url = "https://api.weibo.com/oauth2/authorize?client_id=2766276024&redirect_uri=http://uac.10010.com/portal/SinaWeiboLogin&response_type=code";
        url = "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode";

        SupplyResult<String> r = HttpClientUtil.get(url, header, client);
        System.out.println(r);

        JSONObject jjj = JSONObject.parseObject(StringUtils.substringBetween(r.getModule(), "setCrossDomainUrlList(", ");"));

        header.put("Referer", "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
        header.put("Accept", "*/*");


       HttpClientUtil.get("https://i.sso.sina.com.cn/js/ssologin.js", header, client);

        header.put("Sec-Fetch-Dest","script");
        header.put("Sec-Fetch-Mode","no-cors");
        header.put("Sec-Fetch-Site","cross-site");
        JSONArray ja = jjj.getJSONArray("arrURL");
        int i = 0;
        for (Object o : ja) {
//            if (!o.toString().contains("passport.weibo.com")) {
//                continue;
//            }
            url = URLDecoder.decode(o.toString(), "GBK") + "&scriptId=ssoscript" + i++ + "&callback=sinaSSOController.doCrossDomainCallBack&client=ssologin.js(v1.4.19)&_=" + System.currentTimeMillis();
//            url = o.toString() + "&scriptId=ssoscript" + i++ + "&callback=sinaSSOController.doCrossDomainCallBack&client=ssologin.js(v1.4.19)&_=" + System.currentTimeMillis();

            System.out.println(url);
            SupplyResult<String> get = HttpClientUtil.get(url, header, client);
            System.out.println(get);
            Thread.sleep(10);
        }

        Thread.sleep(100);
        Map<String, String> header2 = new HashMap();
        header2.put("Referer", "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
//        header2.put("Referer","http://uac.10010.com");
        header2.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 Edg/84.0.522.52");
        header2.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header2.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        header2.put("Accept-Encoding", "gzip, deflate, br");
//        header2.put("Connection", "keep-alive");
//        header2.put("Host", "api.weibo.com");
//        header2.put("Sec-Fetch-Dest","document");
//        header2.put("Sec-Fetch-Mode", "navigate");
//        header2.put("Sec-Fetch-Site","cross-site");
//        header2.put("Upgrade-Insecure-Requests", "1");
        url = "https://api.weibo.com/oauth2/authorize?client_id=2766276024&redirect_uri=http://uac.10010.com/portal/SinaWeiboLogin&response_type=code";


        SupplyResult<String> r2 = HttpClientUtil.get(url, header2, client);

        System.out.println(r2);

        if (r2.getModule()!=null && r2.getModule().contains("authZForm")) {
            Document document = Jsoup.parse(r2.getModule());
            Element e = document.getElementsByClass("oauth_login_box01").get(0);
            Elements es = e.getElementsByAttributeValue("type", "hidden");
            String authUrl = "https://api.weibo.com/oauth2/authorize";
            Map<String, String> map = new HashMap<String, String>();
            for (Element element : es) {
                System.out.println(element.attr("name") + "----" + element.val());
                map.put(element.attr("name"), element.val());
            }
            map.put("url", "https://login.sina.com.cn/crossdomain2.php?action=login&r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
            header2.put("Referer", "https://api.weibo.com/oauth2/authorize?client_id=2766276024&redirect_uri=http://uac.10010.com/portal/SinaWeiboLogin&response_type=code");
            SupplyResult<HttpResponse> post = HttpClientUtil.postResp(authUrl, map, "utf-8", header2, client);
            System.out.println(post);
            System.out.println(HttpClientUtil.get(post.getModule().getLocation(), header2, client));
        }

        Thread.sleep(1000);
//
//        url = "https://upay.10010.com/npfweb/npfcardstateweb/card_state_fill.htm";
//
//        SupplyResult<String> r3 = HttpClientUtil.get(url, header, client);
//        System.out.println(r3);
        header.put("Referer", url);
        url = "https://upay.10010.com/npfweb/NpfWeb/judgeLogin/isLogin.action?redirectKey=BuyCardLoginReturnUrl&time=0.8522659772902395&callback=jQuery112308308927947590301_1596898490308&_=1596898490309";
        SupplyResult<String> r4 = HttpClientUtil.get(url, header, client);

        CookieStore c = HttpClientUtil.getCookieStoreByKey(client);
        Thread.sleep(3000);
        for (Cookie cookie:c.getCookies()){
            System.out.println(cookie);
        }
        System.out.println(r4);
    }


    private static String check(BufferedImage bufferedImage) {
        String url = "http://api.ttshitu.com/create.json";
        Map<String, String> map = new HashMap();
        map.put("username", "Kanepan");
        map.put("password", "12345678Aa");
        map.put("typeid", "");
        map.put("angle", "");
        map.put("typename", "");
        map.put("remark", "");


        File file = null;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
//            ImageIO.write(bufferedImage, "jpg", out);
            file = new File("temp/" + System.currentTimeMillis() + ".jpg");
            ImageIO.write(bufferedImage, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String result = postFile(url, map, "image", file);
        System.out.println(result);
        return result;
    }

    private static BufferedImage img() {
        String url = "https://login.sina.com.cn/cgi/pin.php?r=8711379&s=0";


        Map<String, String> header = new HashMap();
        header.put("Referer", "https://login.sina.com.cn/signup/signin.php?r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 Edg/84.0.522.52");
        SupplyResult<BufferedImage> result = HttpClientUtil.getImg(url, header, client);

        return result.getModule();
    }

    public static String postFile(String url, Map<String, String> parmas, String fileName, File file) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {

            //服务器地址
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            mEntityBuilder.addBinaryBody(fileName, file);

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


    /**
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: visitWenZhang
     * @Description: 访问【文章同学】的新浪微博
     */
    public static String visitWenZhang() throws Exception {
        String wenzhang = "http://weibo.com/wenzhang626";
        //SUB=_2A257NaVGDeTxGedH7lsZ8yvPwziIHXVYQpGOrDV8PUNbuNAMLROnkW8p9rH2Bsuc2yUSKU1PzJykmlLc7Q..;
        String html = HttpClientUtil.get(wenzhang, client).getModule();
        //System.out.println(html);
        return html;
    }

    /**
     * @param @param  account
     * @param @param  pwd
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: login
     * @Description: 模拟新浪微博登录
     */
    public static SupplyResult login(String account, String pwd) throws Exception {
        String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)&_=" +
                System.currentTimeMillis();

        url = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)&_=" + System.currentTimeMillis();
        String content = prelogin(account);
        Map<String, Object> paramMap = JSONObject.parseObject(content);

        String pubkey = paramMap.get("pubkey").toString();
        String servertime = paramMap.get("servertime").toString();
        String nonce = paramMap.get("nonce").toString();
        String rsakv = paramMap.get("rsakv").toString();

        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Host", "login.sina.com.cn");
//        headers.put("Origin", "http://weibo.com");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Referer", "https://login.sina.com.cn/signup/signin.php?r=https%3A%2F%2Fapi.weibo.com%2Foauth2%2Fauthorize%3Fclient_id%3D2766276024%26redirect_uri%3Dhttp%3A%2F%2Fuac.10010.com%2Fportal%2FSinaWeiboLogin%26response_type%3Dcode");

        Map<String, String> params = new HashMap<String, String>();
        params.put("entry", "account");
        params.put("gateway", "1");
        params.put("from", "null");
        params.put("savestate", "30");
        params.put("useticket", "0");
        params.put("pagerefer", "https://api.weibo.com/oauth2/authorize?client_id=2766276024&redirect_uri=http://uac.10010.com/portal/SinaWeiboLogin&response_type=code");
        params.put("vsnf", "1");
        params.put("su", encodeAccount(account));
        params.put("service", "account");
        params.put("servertime", servertime);
        params.put("nonce", nonce);
        params.put("pwencode", "rsa2");
        params.put("rsakv", rsakv);
        params.put("sp", getSP(pwd, pubkey, servertime, nonce));
        params.put("encoding", "UTF-8");
        params.put("cdult", "3");
        params.put("domain", "sina.com.cn");
        params.put("prelt", "127");
        params.put("returntype", "TEXT");
        params.put("sr", "1707*960");


//        params.put("entry", "weibo");
//        params.put("gateway", "1");
//        params.put("from", "");
//        params.put("savestate", "7");
//        params.put("useticket", "1");
//        params.put("pagerefer", "http://s.weibo.com/weibo/%25E6%2596%2587%25E7%25AB%25A0%25E5%2590%258C%25E5%25AD%25A6?topnav=1&wvr=6&b=1");
//        params.put("vsnf", "1");
//        params.put("su", encodeAccount(account));
//        params.put("service", "miniblog");
//        params.put("servertime", servertime);
//        params.put("nonce", nonce);
//        params.put("pwencode", "rsa2");
//        params.put("rsakv", rsakv);
//        params.put("sp", getSP(pwd, pubkey, servertime, nonce));
//        params.put("encoding", "UTF-8");
//        params.put("cdult", "2");
//        params.put("domain", "weibo.com");
//        params.put("prelt", "154");
//        params.put("returntype", "TEXT");

        SupplyResult<String> result = HttpClientUtil.post(url, params, "utf-8", headers, client);


        System.out.println(result);
        JSONObject json = JSONObject.parseObject(result.getModule());

        // 2070  验证码不正确   2071 扫码登陆
        if (json.getInteger("retcode") == 4049) {


            BufferedImage bufferedImage = img();


            String check = check(bufferedImage);

            json = JSONObject.parseObject(check);
            params.put("door", json.getJSONObject("data").getString("result"));

        }

        params.put("sp", getSP(pwd, pubkey, servertime, nonce));
        result = HttpClientUtil.post(url, params, "utf-8", headers, client);

        System.out.println(result);

//        Thread.sleep(5600);
        //System.out.println(json);
        return result;
    }

    /**
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: prelogin
     * @Description: 登录必需参数获取
     */
    public static String prelogin(String account) throws Exception {
        String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=&rsakt=mod&client=ssologin.js(v1.4.18)&_=1446099453139";
        url = "http://login.sina.com.cn/sso/prelogin.php?entry=account&callback=sinaSSOController.preloginCallBack&su=" + encodeAccount(account) +
                "&rsakt=mod&client=ssologin.js(v1.4.15)&_=" + System.currentTimeMillis();
        String content = HttpClientUtil.get(url, client).getModule();

        if (null != content && !content.equals("")) {
            content = content.replaceAll("sinaSSOController.preloginCallBack\\((.*)\\)", "$1");
        }
        //System.out.println(content);
        return content;
    }

    /**
     * @param @param  pwd
     * @param @param  pubkey
     * @param @param  servertime
     * @param @param  nonce
     * @param @return
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: getSP
     * @Description: 登录密码加密
     */
    public static String getSP(String pwd, String pubkey, String servertime, String nonce) {
        String t = "10001";
        String message = servertime + "\t" + nonce + "\n" + pwd;
        String result = null;
        try {
            result = rsa(pubkey, t, message);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("RSA加密后的密码：" + result);
        return result;
    }

    /**
     * @param @param  account
     * @param @return
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: encodeAccount
     * @Description: 登录账号编码
     */
    private static String encodeAccount(String account) {
        String userName = "";
        try {
            userName = Base64.encode(URLEncoder.encode(account,
                    "UTF-8").getBytes());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return userName;
    }

    /**
     * @param @param  pubkey
     * @param @param  exponentHex
     * @param @param  pwd
     * @param @return
     * @param @throws IllegalBlockSizeException
     * @param @throws BadPaddingException
     * @param @throws NoSuchAlgorithmException
     * @param @throws InvalidKeySpecException
     * @param @throws NoSuchPaddingException
     * @param @throws InvalidKeyException
     * @param @throws UnsupportedEncodingException
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305 @ qq.com)
     * @Title: rsa
     * @Description: RSA加密
     */
    public static String rsa(String pubkey, String exponentHex, String pwd)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        BigInteger m = new BigInteger(pubkey, 16);
        BigInteger e = new BigInteger(exponentHex, 16);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

        //创建公钥
        RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
        Cipher enc = Cipher.getInstance("RSA");
        enc.init(Cipher.ENCRYPT_MODE, pub);

        byte[] encryptedContentKey = enc.doFinal(pwd.getBytes("UTF-8"));

        return new String(encodeHex(encryptedContentKey));
    }

    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }


}
