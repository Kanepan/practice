package com.kane.practice.utils.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HttpClientUtil {

    public static final ConcurrentMap<String, CloseableHttpClient> clientMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
    static String keyStoreFile = "cert/cacerts";// 证书路径src
    static String password = "changeit";
    static Registry<ConnectionSocketFactory> reg = null;
    static PoolingHttpClientConnectionManager httpPool;
    private static int maxTotal = 2000;
    private static int maxPerRout = 50;
    private static int connectTimeOut = 20000;
    private static int readTimeOut = 60000;
    private static DefaultHttpRequestRetryHandler handler = new DefaultHttpRequestRetryHandler(1, false);
    private static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(readTimeOut)
            .setConnectTimeout(connectTimeOut).build(); // 设置请求和传输超时时间

    static {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStoreFile);
            logger.warn("cert InputStream:" + in);
            ks.load(in, password.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            SSLContext ctx = SSLContexts.custom().loadTrustMaterial(ks, new TrustSelfSignedStrategy()).build();
            //ctx.init(null, tmf.getTrustManagers(), null);
            reg = RegistryBuilder.<ConnectionSocketFactory>create().register("http", new MyConnectionSocketFactory())
                    .register("https", new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }


        httpPool = new PoolingHttpClientConnectionManager();
        //这个在代理中能生效
        SocketConfig sc = SocketConfig.custom().setSoTimeout(20000).build();
        httpPool.setDefaultSocketConfig(sc);
        httpPool.setMaxTotal(maxTotal);// 连接池最大并发连接数
        httpPool.setDefaultMaxPerRoute(maxPerRout);// 单路由最大并发数
    }

    private static boolean isProxyNull(ProxyInfo proxy) {
        if (proxy == null || proxy.getProxyHost() == null || "".equals(proxy.getProxyHost())
                || proxy.getProxyPort() == 0) {
            return true;
        }
        return false;

    }

    private static void closeResponse(CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            logger.error("closeableHttpResponse close error", e);
        }
    }

    private static void checkException(SupplyResult<?> result, Throwable e) {
        logger.error("Throwable ", e);
        if (e instanceof SocketTimeoutException) {
            result.setStatus(SupplyResult.STATUS_UNCONFIRM);
            result.setResultMsg("请求超时");
        } else if (e instanceof HttpHostConnectException) {
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("连接被拒绝");
        } else if (e instanceof SocketException) {
            String message = e.getMessage();
            if (StringUtils.isNotEmpty(message) && StringUtils.contains(message, "reset")) {
                result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                result.setResultMsg("请求超时");
            } else {
                result.setStatus(SupplyResult.STATUS_FAILED);
                result.setResultMsg("网络连接失败");
            }
        } else {
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("网络连接失败");
        }
    }

    public static SupplyResult<BufferedImage> getImg(String url) {
        return getImg(url, null, null);
    }

    public static SupplyResult<BufferedImage> getImg(String url, String clientKey) {
        return getImg(url, null, clientKey);
    }

    public static SupplyResult<HttpResponse> getResp(String url, Map<String, String> headerMap, final ProxyInfo proxy,
                                                     String clientKey, RequestConfig requestConfig) {
        return executeResp("GET", url, null, headerMap, proxy, clientKey, requestConfig);
    }

    public static SupplyResult<BufferedImage> getImg(String url, Map<String, String> headerMap, String clientKey) {
        SupplyResult<BufferedImage> supplyResult = new SupplyResult<BufferedImage>();
        if (url == null) {
            supplyResult.setResultMsg("请求参数缺失");
            supplyResult.setFailed();
            return supplyResult;
        }
        BufferedImage read = null;
        CloseableHttpResponse execute = null;
        try {
            execute = execute("GET", url, null, headerMap, null, clientKey, null);
            read = ImageIO.read(execute.getEntity().getContent());
        } catch (Throwable e) {
            checkException(supplyResult, e);
        } finally {
            closeResponse(execute);
        }
        if (read != null) {
            supplyResult.setModule(read);
            supplyResult.setSuccess();
        }
        return supplyResult;
    }

    public static SupplyResult<String> get(String url, Map<String, String> headerMap, final ProxyInfo proxy,
                                           String clientKey, RequestConfig requestConfig) {
        return executeStr("GET", url, null, headerMap, proxy, clientKey, requestConfig);
    }

    public static SupplyResult<String> get(String url, Map<String, String> headerMap, String clientKey) {
        return get(url, headerMap, null, clientKey, null);
    }

    public static SupplyResult<String> get(String url, String clientKey) {
        return get(url, null, null, clientKey, null);
    }

    public static SupplyResult<String> get(String url, Map<String, String> headerMap) {
        return get(url, headerMap, null);
    }

    public static SupplyResult<String> get(String url, final ProxyInfo proxy) {
        return get(url, null, proxy, null, null);
    }

    public static SupplyResult<String> get(String url) {
        return get(url, null, null);
    }

    public static SupplyResult<String> get(String url, RequestConfig requestConfig) {
        return get(url, null, null, null, requestConfig);
    }

    /**
     * URL自动编码
     *
     * @param url
     * @param paramMap
     * @param charset
     * @param headerMap
     * @param clientKey
     * @return SupplyResult<String>
     * @author: harvies
     * @createTime: Jun 6, 2017 12:08:48 PM
     * @history:
     */
    public static SupplyResult<String> post(String url, Map<String, String> paramMap, String charset,
                                            Map<String, String> headerMap, String clientKey) {
        List<NameValuePair> parameters = new ArrayList<>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeStr("POST", url, urlEncodedFormEntity, headerMap, null, clientKey, null);
    }

    public static SupplyResult<String> post(String url, Map<String, String> paramMap, String charset,
                                            Map<String, String> headerMap, String clientKey,ProxyInfo proxyInfo) {
        List<NameValuePair> parameters = new ArrayList<>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeStr("POST", url, urlEncodedFormEntity, headerMap, proxyInfo, clientKey, null);
    }

    public static SupplyResult<String> post(String url, Map<String, String> paramMap, String charset,
                                            Map<String, String> headerMap, String clientKey, RequestConfig requestConfig) {
        List<NameValuePair> parameters = new ArrayList<>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeStr("POST", url, urlEncodedFormEntity, headerMap, null, clientKey, requestConfig);
    }

    public static SupplyResult<String> post(String url, String params, Map<String, String> headerMap,
                                            String clientKey) {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotBlank(params)) {
            httpEntity = EntityBuilder.create().setText(params).build();
        }
        return executeStr("POST", url, httpEntity, headerMap, null, clientKey, null);
    }


    public static SupplyResult<String> postJson(String url, String params, Map<String, String> headerMap, String clientKey) {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotBlank(params)) {
            httpEntity = new StringEntity(params, "UTF-8");
        }
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        return executeStr("POST", url, httpEntity, headerMap, null, clientKey, null);
    }


    public static SupplyResult<String> postJson(String url, String params,
                                                String clientKey) {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotBlank(params)) {
            httpEntity = EntityBuilder.create().setText(params).build();
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        return executeStr("POST", url, httpEntity, headerMap, null, clientKey, null);
    }

    public static SupplyResult<String> postJson(String url, String params) {
        return postJson(url, params, null);
    }

    public static SupplyResult<String> postJson(String url, JSONObject jsonObject) {
        return postJson(url, JSONObject.toJSONString(jsonObject));
    }

    public static SupplyResult<String> postJson(String url, JSONObject jsonObject, String clientKey) {
        return postJson(url, JSONObject.toJSONString(jsonObject), clientKey);
    }

    public static SupplyResult<HttpResponse> postResp(String url, String params, Map<String, String> headerMap,
                                                      String clientKey) {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotBlank(params)) {
            httpEntity = EntityBuilder.create().setText(params).build();
        }
        return executeResp("POST", url, httpEntity, headerMap, null, clientKey, null);
    }

    public static SupplyResult<HttpResponse> postResp(String url, Map<String, String> paramMap, String charset,
                                                      Map<String, String> headerMap, String clientKey, RequestConfig requestConfig) {
        List<NameValuePair> parameters = new ArrayList<>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeResp("POST", url, urlEncodedFormEntity, headerMap, null, clientKey, requestConfig);
    }

    public static SupplyResult<HttpResponse> postResp(String url, Map<String, String> paramMap, String charset,
                                                      Map<String, String> headerMap, String clientKey) {
        return postResp(url, paramMap, charset, headerMap, clientKey, null);
    }

    private static SupplyResult<HttpResponse> executeResp(String requestType, String url, HttpEntity httpEntity,
                                                          Map<String, String> headerMap, final ProxyInfo proxy, String clientKey, RequestConfig requestConfig) {
        SupplyResult<HttpResponse> result = new SupplyResult<>();
        HttpResponse httpResponse = new HttpResponse();
        result.setModule(httpResponse);
        Integer statusCode;
        CloseableHttpResponse response = null;
        try {
            response = execute(requestType, url, httpEntity, headerMap, proxy, clientKey,
                    requestConfig);
            if (response != null) {
                statusCode = response.getStatusLine().getStatusCode();
                List<Header> headers = Arrays.asList(response.getAllHeaders());
                for (Header header : headers) {
                    if ("Location".equals(header.getName().trim())) {
                        httpResponse.setLocation(header.getValue());
                    }
                }
                httpResponse.setHeaders(headers);
                httpResponse.setCode(statusCode + "");
                if (statusCode != 200) {
                    logger.error("connect " + url + " error httpCode : " + statusCode);
                    if (statusCode >= 500) {
                        result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                        result.setResultCode(statusCode + "");
                        result.setResultMsg(statusCode + " error");
                        return result;
                    }
                    result.setStatus(SupplyResult.STATUS_FAILED);
                    result.setResultCode(statusCode + "");
                    result.setResultMsg(statusCode + " error");
                    return result;
                }
                String contentType = null;
                if (headerMap != null) {
                    contentType = headerMap.get("Content-Type");
                }
                String charset = null;
                ContentType contentType1 = ContentType.get(response.getEntity());
                if (StringUtils.isBlank(charset) && contentType1 != null) {
                    Charset charset1 = contentType1.getCharset();
                    if (charset1 != null) {
                        charset = charset1.displayName();
                    }
                }
                if (StringUtils.isBlank(charset) && StringUtils.isNotBlank(contentType)) {
                    if (contentType.toUpperCase().contains("GBK")) {
                        charset = "GBK";
                    }
                }
                if (StringUtils.isBlank(charset)) {
                    charset = "UTF-8";
                }
                httpResponse.setBody(EntityUtils.toString(response.getEntity(), charset));
                result.setSuccess();
            }
        } catch (Throwable e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }
        return result;
    }

    private static SupplyResult<String> executeStr(String requestType, String url, HttpEntity httpEntity,
                                                   Map<String, String> headerMap,  ProxyInfo proxy, String clientKey, RequestConfig requestConfig) {
        SupplyResult<String> result = new SupplyResult<>();
        Integer statusCode = -1;
        CloseableHttpResponse response = null;

        try {
            response = execute(requestType, url, httpEntity, headerMap, proxy, clientKey,
                    requestConfig);
            if (response != null) {
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    logger.error("connect " + url + " error httpCode : " + statusCode);
                    if (statusCode >= 500) {
                        result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                        result.setResultCode(statusCode + "");
                        result.setResultMsg(statusCode + " error");
//                        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
                        return result;
                    }
                    result.setStatus(SupplyResult.STATUS_FAILED);
                    result.setResultCode(statusCode + "");
                    result.setResultMsg(statusCode + " error");
                    return result;
                }
                String contentType = null;
                if (headerMap != null) {
                    contentType = headerMap.get("Content-Type");
                }

                String charset = null;
                ContentType contentType1 = ContentType.get(response.getEntity());
                if (StringUtils.isBlank(charset) && contentType1 != null) {
                    Charset charset1 = contentType1.getCharset();
                    if (charset1 != null) {
                        charset = charset1.displayName();
                    }
                }
                if (StringUtils.isBlank(charset) && StringUtils.isNotBlank(contentType)) {
                    if (contentType.toUpperCase().contains("GBK")) {
                        charset = "GBK";
                    }
                }
                if (StringUtils.isBlank(charset)) {
                    charset = "UTF-8";
                }

                if(response.getHeaders("Set-Cookie")!=null){
                    for(Header h:response.getHeaders("Set-Cookie")){
                        System.out.println("Set-Cookie  --------------" + h.getValue());
                    }

                }
                result.setModule(EntityUtils.toString(response.getEntity(), charset));
                result.setSuccess();
            }
        } catch (Throwable e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }
        return result;
    }

    private static CloseableHttpResponse execute(String requestType, String url, HttpEntity httpEntity,
                                                 Map<String, String> headerMap, final ProxyInfo proxy, String clientKey, RequestConfig requestConfig) throws Throwable {
        CloseableHttpResponse execute = null;
        if (!isProxyNull(proxy)) {
            // 代理用户名和密码验证
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    PasswordAuthentication p = new PasswordAuthentication(proxy.getProxyUserName(),
                            proxy.getProxyPwd().toCharArray());
                    return p;
                }
            });
        }
        try {
            HttpClientContext context = HttpClientContext.create();
            if (!isProxyNull(proxy)) {
                InetSocketAddress socksaddr = new InetSocketAddress(proxy.getProxyHost(), proxy.getProxyPort());
                context.setAttribute("socks.address", socksaddr);
                context.setAttribute("socks.proxyType", proxy.getProxyType());
            }
            HttpRequestBase requestBase = null;
            if ("get".equals(requestType.toLowerCase()) || requestType == null || "".equals(requestType)) {
                requestBase = new HttpGet(url);

            } else if ("post".equals(requestType.toLowerCase())) {
                requestBase = new HttpPost(url);
            }
            requestBase.setHeader("User-Agent", UA);
            if (requestConfig != null) {
                Builder copy = RequestConfig.copy(requestConfig);
                if (requestConfig.getConnectionRequestTimeout() > 0) {
                    copy.setConnectTimeout(requestConfig.getConnectionRequestTimeout());
                }
                if (requestConfig.getConnectTimeout() > 0) {
                    copy.setConnectTimeout(requestConfig.getConnectTimeout());
                }
                requestBase.setConfig(copy.build());
            } else {
                requestBase.setConfig(defaultRequestConfig);
            }

            if (requestBase instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase post = (HttpEntityEnclosingRequestBase) requestBase;
                post.setEntity(httpEntity);
            }
            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    requestBase.setHeader(key, headerMap.get(key));
                }
            }
            CloseableHttpClient httpclient = null;
            if (!"".equals(clientKey) && clientKey != null) {
                httpclient = getHttpClientByKey(clientKey);
            } else {
                httpclient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler).build();
            }
            execute = httpclient.execute(requestBase, context);

        } catch (
                Throwable e) {
            throw e;
        } finally {
            // 连接池不用关闭连接
        }
        return execute;
    }

    /**
     * @param clientKey 一般是不同登录名。 如果两个网站同用同个登录名， 则也COOKIE 互不干扰。
     * @return
     */
    public static CloseableHttpClient getHttpClientByKey(String clientKey) {
        CloseableHttpClient result = clientMap.get(clientKey);
        if (result == null) {
            CloseableHttpClient newClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler)
                    .build();
            result = clientMap.putIfAbsent(clientKey, newClient);
            if (result == null) {
                result = newClient;
            }
        }
        return result;
    }

    public static CookieStore getCookieStoreByKey(String clientKey) {
        CookieStore cookieStore = null;
        try {
            CloseableHttpClient httpClient = getHttpClientByKey(clientKey);
            Class<?> closeableHttpClientClass = httpClient.getClass();
            Field field = closeableHttpClientClass.getDeclaredField("cookieStore");
            field.setAccessible(true);
            cookieStore = (CookieStore) field.get(httpClient);
        } catch (Exception e) {
            logger.error("", e);
        }
        return cookieStore;
    }

    /**
     * 注入cookie
     *
     * @param clientKey
     * @param cookieList
     * @return
     */
    public static CloseableHttpClient injectCookieByKey(String clientKey, List<Cookie> cookieList) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClientByKey(clientKey);
            Class<?> closeableHttpClientClass = httpClient.getClass();
            Field field = closeableHttpClientClass.getDeclaredField("cookieStore");
            field.setAccessible(true);
            CookieStore cookieStore = (CookieStore) field.get(httpClient);
            //清空已存在的key
            cookieStore.clear();
            for (Cookie cook :
                    cookieList) {
                cookieStore.addCookie(cook);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return httpClient;
    }


    public static CloseableHttpClient injectCookieByKey(String clientKey, Cookie cookie) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClientByKey(clientKey);
            Class<?> closeableHttpClientClass = httpClient.getClass();
            Field field = closeableHttpClientClass.getDeclaredField("cookieStore");
            field.setAccessible(true);
            CookieStore cookieStore = (CookieStore) field.get(httpClient);
            //清空已存在的key
            cookieStore.clear();
            cookieStore.addCookie(cookie);
        } catch (Exception e) {
            logger.error("", e);
        }
        return httpClient;
    }


    public static CloseableHttpClient injectCookieByKey(String clientKey, CookieStore cookieStore) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClientByKey(clientKey);
            Class<?> closeableHttpClientClass = httpClient.getClass();
            Field field = closeableHttpClientClass.getDeclaredField("cookieStore");
            field.setAccessible(true);
            field.set(httpClient, cookieStore);
        } catch (Exception e) {
            logger.error("", e);
        }
        return httpClient;
    }

    public static CloseableHttpClient injectCookieByKey(String clientKey, String cookieStr, String url) throws Exception {
        CloseableHttpClient httpClient;
        List<Cookie> cookieList = cookieStrToList(cookieStr, url);
        httpClient = injectCookieByKey(clientKey, cookieList);
        return httpClient;
    }

    public static List<Cookie> cookieStrToList(String cookieStr, String url) throws Exception {
        List<Cookie> cookieList = new ArrayList<>();
        if (StringUtils.isBlank(cookieStr)) {
            return cookieList;
        }
        String[] split = StringUtils.split(cookieStr, ";");
        for (String s :
                split) {
            String trim = s.trim();
            String[] split1 = StringUtils.split(trim, "=");
            String key = null;
            String value = null;
            if (split1.length == 2) {
                key = split1[0];
                value = split1[1];
            } else if (split1.length == 1) {
                key = split1[0];
                value = "";
            } else {
                continue;
            }
            BasicClientCookie2 cookie = new BasicClientCookie2(key, value);
            cookie.setDomain(new URL(url).getHost());
            cookie.setExpiryDate(DateUtils.addDays(new Date(), 1000));
            cookie.setPath("/");
            cookieList.add(cookie);
        }
        return cookieList;
    }

    public static CookieStore cloneCookieStore(CookieStore cookieStore) {
        CookieStore cookieStoreNew = new BasicCookieStore();
        try {
            if (cookieStore == null) {
                return null;
            }
            for (Cookie cookie :
                    cookieStore.getCookies()) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                BeanUtils.copyProperties(basicClientCookie, cookie);
                cookieStoreNew.addCookie(basicClientCookie);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return cookieStoreNew;
    }

    public static void removeHttpClientByKey(String clientKey) {
        clientMap.remove(clientKey);
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient newClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler)
                .build();
        return newClient;
    }

    private static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            Object socksaddr = context.getAttribute("socks.address");
            Object proxyType = context.getAttribute("socks.proxyType");
            if (socksaddr != null && proxyType != null) {
                Proxy proxy = new Proxy(Proxy.Type.valueOf(proxyType.toString()), (SocketAddress) socksaddr);
                return new Socket(proxy);
            }
            return super.createSocket(context);
        }
    }


    private static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {
        public MySSLConnectionSocketFactory(final SSLContext sslContext) {
            super(sslContext, NoopHostnameVerifier.INSTANCE);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            Object socksaddr = context.getAttribute("socks.address");
            Object proxyType = context.getAttribute("socks.proxyType");
            if (socksaddr != null && proxyType != null) {
                Proxy proxy = new Proxy(Proxy.Type.valueOf(proxyType.toString()), (SocketAddress) socksaddr);
                return new Socket(proxy);
            }
            return super.createSocket(context);
        }
    }

    public static String mapToString(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> e : map.entrySet()) {
            if (e.getValue() != null) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }
}
