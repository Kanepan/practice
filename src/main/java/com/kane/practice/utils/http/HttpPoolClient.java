package com.kane.practice.utils.http;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.*;
import java.security.KeyStore;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public enum HttpPoolClient {
    INS;
    private static final Logger logger = LoggerFactory.getLogger(HttpPoolClient.class);

    private static final String contextType = "text/html,application/xml;charset=UTF-8";
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
    private static final String UA_IE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
    // private static final String contextTypeJson =
    // "application/json;charset=UTF-8";
    // private static final String contextTypeXml = "application/xml";

    private static PoolingHttpClientConnectionManager httpPool = new PoolingHttpClientConnectionManager();
    private static int maxTotal = 2000;
    private static int maxPerRout = 1000;
    private static int connecttimeOut = 10000;
    private static int readTimeOut = 10000;
    private static String isNotAutoRedirect_key = "_isNotAutoRedirect";
    private static String isCustomTime_key = "_isCustomTime";
    private static DefaultHttpRequestRetryHandler handler = new DefaultHttpRequestRetryHandler(0, false);

    private static RequestConfig requestCookieConfig = RequestConfig.custom().setSocketTimeout(readTimeOut)
            .setConnectTimeout(connecttimeOut).build();


//	private static final Cache<String, CloseableHttpClient> clientMap = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

//	public final Cache<String, CloseableHttpClient> clientLongTimeMap = CacheBuilder.newBuilder().expireAfterWrite(29, TimeUnit.MINUTES).build();

    // 当在指定时间内没有被读或者写则被回收，这个更符合业务场景，而且这样就不用new 几个 cache了

    private static final int cookieExpire = 30;

    private static final Cache<String, CloseableHttpClient> clientMap2 = CacheBuilder.newBuilder().expireAfterAccess(cookieExpire, TimeUnit.MINUTES).build();

    private final static Cache<String, CookieStore> cookieMap = CacheBuilder.newBuilder().expireAfterWrite(cookieExpire, TimeUnit.MINUTES).build();


    /*	static {
            httpPool.setMaxTotal(maxTotal);// 连接池最大并发连接数
            httpPool.setDefaultMaxPerRoute(maxPerRout);// 单路由最大并发数
        }*/
    static String keyStoreFile = "cert/cacerts";// 证书路径src
    static String password = "changeit";
    static Registry<ConnectionSocketFactory> reg = null;

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
        httpPool = new PoolingHttpClientConnectionManager(reg);
        httpPool.setMaxTotal(maxTotal);// 连接池最大并发连接数
        httpPool.setDefaultMaxPerRoute(maxPerRout);// 单路由最大并发数

        //这个在代理中能生效
        SocketConfig sc = SocketConfig.custom().setSoTimeout(20000).build();
        httpPool.setDefaultSocketConfig(sc);

        new Thread(new Runnable() {//起个线程，定时清除空闲超过4秒的连接
            @Override
            public void run() {
                while (true) {
                    httpPool.closeExpiredConnections();
                    httpPool.closeIdleConnections(4, TimeUnit.SECONDS);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
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

    private HttpPoolClient() {

    }


    public SupplyResult<String> sendGetByCookie(String url, String clientKey, Map<String, String> headers) {
        SupplyResult<String> result = new SupplyResult<String>();
        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }
        Integer statusCode = -1;
        logger.warn("request: " + url);
        HttpGet get = null;
        try {
            get = new HttpGet(url.trim());
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }
        get.setConfig(requestCookieConfig);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = getHttpClientByKey(clientKey);
            response = httpclient.execute(get);
            statusCode = response.getStatusLine().getStatusCode();
            result.setResultCode(String.valueOf(statusCode));
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
//			logger.warn("get response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    public SupplyResult<String> sendPostByCookieByHeader(String url, Map<String, String> paramMap, Map<String, String> headers, String clientKey) {
        SupplyResult<String> result = new SupplyResult<String>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
            post.setHeader("User-Agent", UA_CHROME);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
        post.setConfig(requestCookieConfig);

        CloseableHttpResponse response = null;
        try {

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null) {
                for (Entry<String, String> m : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
            }

            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    post.setHeader(key, headers.get(key));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            response = getHttpClientByKey(clientKey).execute(post);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            logger.warn("post response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }


    public SupplyResult<String> sendPostByCookieByHeader(String url, Map<String, String> paramMap, Map<String, String> headers, String clientKey, ProxyInfo proxyInfo) {
        SupplyResult<String> result = new SupplyResult<String>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
            post.setHeader("User-Agent", UA_CHROME);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
        HttpClientContext httpContext = null;
        if (proxyInfo != null) {
            Builder builder = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(20000).setRedirectsEnabled(true);
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            post.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            post.setConfig(requestCookieConfig);
        }

        CloseableHttpResponse response = null;
        try {

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null) {
                for (Entry<String, String> m : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
            }

            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    post.setHeader(key, headers.get(key));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

            response = getHttpClientByKey(clientKey).execute(post, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            logger.warn("post response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }


    public SupplyResult<HttpResponse> sendPostByCookieByHeaderRep(String url, Map<String, String> paramMap, Map<String, String> headers, String clientKey, ProxyInfo proxyInfo) {
        SupplyResult<HttpResponse> result = new SupplyResult<>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
            post.setHeader("User-Agent", UA_CHROME);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
        HttpClientContext httpContext = null;
        if (proxyInfo != null) {
            Builder builder = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(20000).setRedirectsEnabled(true);
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            post.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            post.setConfig(requestCookieConfig);
        }

        CloseableHttpResponse response = null;
        try {

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null) {
                for (Entry<String, String> m : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
            }

            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    post.setHeader(key, headers.get(key));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            response = getHttpClientByKey(clientKey).execute(post, httpContext);
            statusCode = response.getStatusLine().getStatusCode();


            HttpResponse httpResponse = new HttpResponse();
            List<Header> resHeaders = Arrays.asList(response.getAllHeaders());
            for (Header header : resHeaders) {
                if ("Location".equals(header.getName().trim())) {
                    httpResponse.setLocation(header.getValue());
                }
            }
            httpResponse.setHeaders(resHeaders);
            httpResponse.setCode(statusCode + "");

            result.setModule(httpResponse);
            if (statusCode == 302) {
                result.setStatus(SupplyResult.STATUS_SUCCESS);
                result.setResultCode(statusCode + "");
                return result;
            }


            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }


            String resultStr = EntityUtils.toString(response.getEntity());
            logger.warn("post response : " + resultStr);
            httpResponse.setBody(resultStr);
            result.setModule(httpResponse);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    public SupplyResult<HttpResponse> sendPostByCookieReturnReps(String url, Map<String, String> paramMap, String clientKey) {
        SupplyResult<HttpResponse> result = new SupplyResult<HttpResponse>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;
        try {
            post = new HttpPost(url.trim());
            post.setHeader("User-Agent", UA_CHROME);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
        post.setConfig(requestCookieConfig);

        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null) {
                for (Entry<String, String> m : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            post.setHeader("Accept", contextType);
            response = getHttpClientByKey(clientKey).execute(post);
            statusCode = response.getStatusLine().getStatusCode();

            HttpResponse httpResponse = new HttpResponse();
            List<Header> headers = Arrays.asList(response.getAllHeaders());
            for (Header header : headers) {
                if ("Location".equals(header.getName().trim())) {
                    httpResponse.setLocation(header.getValue());
                }
            }
            httpResponse.setHeaders(headers);
            httpResponse.setCode(statusCode + "");

            result.setModule(httpResponse);
            if (statusCode == 302) {
                result.setStatus(SupplyResult.STATUS_SUCCESS);
                result.setResultCode(statusCode + "");
                return result;
            }

            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }

            String resultStr = EntityUtils.toString(response.getEntity());
            httpResponse.setBody(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);
        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    /**
     * @param clientKey 一般是不同登录名。
     *                  如果两个网站同用同个登录名， 则也COOKIE 互不干扰。
     * @return
     */
/*	public CloseableHttpClient getHttpClientByKey(String clientKey) {
		CloseableHttpClient result = clientLongTimeMap.asMap().get(clientKey);
		if(result == null){
			result = clientMap.asMap().get(clientKey);
		}
		if (result == null) {
			CookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient newClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(httpPool).setRetryHandler(handler).build();
			result = clientMap.asMap().putIfAbsent(clientKey, newClient);
//			cookieMap.asMap().putIfAbsent(clientKey, cookieStore);
			if (result == null) {
				result = newClient;
			}
		}
		return result;
	}*/
    public CloseableHttpClient getHttpClientByKey(String clientKey) {
        CloseableHttpClient result = clientMap2.asMap().get(clientKey);
        if (result == null) {
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient newClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(httpPool).setRetryHandler(handler).build();
            result = clientMap2.asMap().putIfAbsent(clientKey, newClient);
            cookieMap.asMap().putIfAbsent(clientKey, cookieStore);
            if (result == null) {
                result = newClient;
            }
        }
        return result;
    }

    public CookieStore getCookieStoreByKey(String clientKey) {
        return cookieMap.asMap().get(clientKey);
    }

    /* ---------------end 华丽的分割线----------------- */

    /*-------------------------带代理的请求  ---------------------------*/
    public SupplyResult<BufferedImage> getImgByProxy(String url, ProxyInfo proxyInfo, String clientKey) {
        SupplyResult<BufferedImage> result = new SupplyResult<BufferedImage>();
        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }
        InputStream inputStream = null;
        Integer statusCode = -1;
        logger.warn("request: " + url);
        HttpGet get = null;
        try {
            get = new HttpGet(url.trim());
            // get.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686;
            // rv:17.0) Gecko/20100101 Firefox/17.0");
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut);
        HttpContext httpContext = null;
        if (proxyInfo != null) {
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            get.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            get.setConfig(requestCookieConfig);
        }
        CloseableHttpResponse response = null;
        try {
            get.setHeader("Accept", contextType);
            response = getHttpClientByKey(clientKey).execute(get, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                result.setResultMsg(statusCode + " error");
                return result;
            }

            try {
                if (response.getEntity() == null) {
                    result.setStatus(SupplyResult.STATUS_FAILED);
                    result.setResultMsg("图片为空");
                    result.setResultCode(statusCode + "");
                    return result;
                }

                InputStream input = response.getEntity().getContent();
                BufferedImage image = ImageIO.read(input);
                result.setModule(image);
                result.setStatus(SupplyResult.STATUS_SUCCESS);
                return result;
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("inputStream close error", e);
                }
            }
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }
        return result;
    }

    public SupplyResult<String> sendGetByProxy(String url, ProxyInfo proxyInfo, String clientKey, Integer readTimeOutParam, Integer connecttimeOutParam) {
        return sendGetByProxy(url, proxyInfo, clientKey, null, readTimeOutParam, connecttimeOutParam);
    }

    public SupplyResult<String> sendGetByProxy(String url, ProxyInfo proxyInfo, String clientKey, Map<String, String> headers) {
        return sendGetByProxy(url, proxyInfo, clientKey, headers, null, null);
    }

    public SupplyResult<String> sendGetByProxy(String url, ProxyInfo proxyInfo, String clientKey) {
        return sendGetByProxy(url, proxyInfo, clientKey, null, null, null);
    }

    public SupplyResult<String> sendGetByProxy(String url, ProxyInfo proxyInfo, String clientKey, Map<String, String> headers, Integer readTimeOutParam, Integer connecttimeOutParam) {
        SupplyResult<String> result = new SupplyResult<String>();
        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }
        Integer statusCode = -1;
//		logger.warn("request: " + url);
        HttpGet get = null;
        try {
            get = new HttpGet(url.trim());
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }
        Builder builder = null;
        if (readTimeOutParam != null && connecttimeOutParam != null) {
            builder = RequestConfig.custom().setSocketTimeout(readTimeOutParam).setConnectTimeout(connecttimeOutParam);
        } else {
            builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut);
        }

        HttpContext httpContext = null;
        if (proxyInfo != null) {
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            get.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            get.setConfig(builder.build());
        }

        CloseableHttpResponse response = null;

        get.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
        if (headers != null && headers.size() != 0) {
            for (Entry<String, String> e : headers.entrySet()) {
                get.setHeader(e.getKey(), e.getValue());
            }
        }

        try {
            response = getHttpClientByKey(clientKey).execute(get, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            result.setResultCode(String.valueOf(statusCode));
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            // logger.warn("get response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    private HttpProxyInfo getHttpProxyInfo(ProxyInfo proxyInfo, Builder builder, Boolean isRedirect, Boolean isCustomTime) {
        HttpProxyInfo newHttpProxyInfo = new HttpProxyInfo();
        HttpHost proxy = new HttpHost(proxyInfo.getProxyHost(), proxyInfo.getProxyPort());
        builder.setProxy(proxy);
        if (proxyInfo.getProxyUserName() != null && proxyInfo.getProxyPwd() != null) {
            builder.setAuthenticationEnabled(true);
            HttpClientContext httpContext = new HttpClientContext();
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxyInfo.getProxyHost(), proxyInfo.getProxyPort()),
                    new UsernamePasswordCredentials(proxyInfo.getProxyUserName(), proxyInfo.getProxyPwd()));
            httpContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credsProvider);
            newHttpProxyInfo.setHttpContext(httpContext);
        }
        newHttpProxyInfo.setRequestConfig(builder.build());
        return newHttpProxyInfo;
    }

    /* ---------------end 华丽的分割线----------------- */

    private String mapToString(Map<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer("");
        for (Entry<String, String> e : paramMap.entrySet()) {
            sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }
        return sb.toString();
    }

    private void closeResponse(CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            logger.error("closeableHttpResponse close error", e);
        }
    }

    private void checkException(SupplyResult<?> result, Exception e) {
        logger.error("IOException ", e);
        result.setHttpMsg(e.getMessage());
        if (e instanceof SocketTimeoutException) {
            result.setStatus(SupplyResult.STATUS_UNCONFIRM);
            result.setResultMsg("请求超时");
        } else if (e instanceof SocketException) {
            String messge = e.getMessage();
            if (StringUtils.isNotEmpty(messge) && StringUtils.contains(messge, "reset")) {
                result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                result.setResultMsg("请求超时");
            } else {
                result.setStatus(SupplyResult.STATUS_FAILED);
                result.setResultMsg("网络连接失败");
            }
        } else if (e instanceof NoHttpResponseException) {
            result.setStatus(SupplyResult.STATUS_UNCONFIRM);
            result.setResultMsg("请求超时noHttpResponse");
        } else {
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("网络连接失败");
        }
    }

    public SupplyResult<String> sendPostStrByProxy(String url, String paramStr, ProxyInfo proxyInfo, String clientKey) {
        return sendPostStrByProxy(url, paramStr, proxyInfo, clientKey, null);
    }

    public SupplyResult<String> sendPostStrByProxy(String url, String paramStr, ProxyInfo proxyInfo, String clientKey, Map<String, String> headers) {
        SupplyResult<String> result = new SupplyResult<String>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + paramStr);

        HttpContext httpContext = null;
        if (proxyInfo != null) {
            Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut);
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            post.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            post.setConfig(requestCookieConfig);
        }
        post.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
        if (headers != null && headers.size() != 0) {
            for (Entry<String, String> e : headers.entrySet()) {
                post.setHeader(e.getKey(), e.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(paramStr, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            post.setEntity(entity);
            response = getHttpClientByKey(clientKey).execute(post, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            result.setResultCode(String.valueOf(statusCode));
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            logger.warn("post response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }
        return result;
    }

    public SupplyResult<Map<String, String>> sendPostByCookieProxyAndGetLocation(String url, Map<String, String> paramMap
            , String clientKey, ProxyInfo proxyInfo, Map<String, String> reqHeaders) {
        SupplyResult<Map<String, String>> result = new SupplyResult<Map<String, String>>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut);
        HttpContext httpContext = null;
        if (proxyInfo != null) {
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            post.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            post.setConfig(requestCookieConfig);
        }
        logger.warn("post request: " + url + "  params : " + mapToString(paramMap));

        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null) {
                for (Entry<String, String> m : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            post.setHeader("Accept", contextType);

            if (reqHeaders != null && !reqHeaders.isEmpty()) {
                for (String key : reqHeaders.keySet()) {
                    post.setHeader(key, reqHeaders.get(key));
                }
            }

            response = getHttpClientByKey(clientKey).execute(post, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 302) {
                Header[] headers = response.getAllHeaders();
                Map<String, String> resHeadersMap = new HashMap<String, String>();
                int i = 0;
                while (i < headers.length) {
                    resHeadersMap.put(headers[i].getName(), headers[i].getValue());
                    ++i;
                }
                result.setStatus(SupplyResult.STATUS_SUCCESS);
                result.setResultCode(statusCode + "");
                result.setModule(resHeadersMap);
                return result;
            }
            if (statusCode != 200 && statusCode != 302) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                if (statusCode >= 500) {
                    result.setStatus(SupplyResult.STATUS_UNCONFIRM);
                    result.setResultCode(statusCode + "");
                    result.setResultMsg(statusCode + " error");
                    return result;
                }

                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            logger.warn("post response : " + resultStr);
            result.setStatus(SupplyResult.STATUS_UNCONFIRM);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    public SupplyResult<IdentityHashMap<String, Object>> sendGetByCookieProxyAndUri(String url, String uri, boolean isRedirect, String clientKey, ProxyInfo proxyInfo) {
        SupplyResult<IdentityHashMap<String, Object>> result = new SupplyResult<IdentityHashMap<String, Object>>();
        IdentityHashMap<String, Object> map = new IdentityHashMap<>();
        result.setModule(map);
        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }
        Integer statusCode = -1;
        HttpGet get = null;
        try {
            get = new HttpGet(url.trim());
            get.setURI(new URI(get.getURI().toString() + "?" + uri));
            logger.warn("request: " + url + "?" + uri);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }
        //不让httpclient自动重定向
        Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut).setRedirectsEnabled(isRedirect);
        HttpContext httpContext = null;
        if (proxyInfo != null) {
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, isRedirect, null);
            httpContext = httpProxyInfo.getHttpContext();
            get.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            RequestConfig requestCookieConfig2 = builder.build();
            get.setConfig(requestCookieConfig2);
        }

        CloseableHttpResponse response = null;
        try {
            response = getHttpClientByKey(clientKey).execute(get, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 302 && !isRedirect) {
                logger.warn("post response httpcode: " + statusCode);
                Header[] headers = response.getAllHeaders();
                int i = 0;
                while (i < headers.length) {
//					System.out.println(headers[i].getName() + ":" + headers[i].getValue());
                    map.put(new String(headers[i].getName()), headers[i].getValue());
                    ++i;
                }
                result.setStatus(SupplyResult.STATUS_SUCCESS);
            } else if (statusCode == 200 && isRedirect) {
                String resultStr = EntityUtils.toString(response.getEntity());
//				logger.warn("post response : " + resultStr);
                map.put("body", resultStr);
                result.setStatus(SupplyResult.STATUS_SUCCESS);
            } else {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    public SupplyResult<HttpResponse> sendGetByCookieReturnReps(String url, Map<String, String> reqHeaders, String clientKey, ProxyInfo proxyInfo) {
        SupplyResult<HttpResponse> result = new SupplyResult<HttpResponse>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpGet get = null;
        try {
            get = new HttpGet(url.trim());
            if (reqHeaders != null && !reqHeaders.isEmpty()) {
                for (String key : reqHeaders.keySet()) {
                    get.setHeader(key, reqHeaders.get(key));
                }
            }
            get.setHeader("User-Agent", UA_CHROME);
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }
        HttpClientContext httpContext = HttpClientContext.create();
        if (proxyInfo != null) {
            Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut);
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, null, null);
            httpContext = httpProxyInfo.getHttpContext();
            get.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            get.setConfig(requestCookieConfig);
        }
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient closeableHttpClient = getHttpClientByKey(clientKey);
            response = closeableHttpClient.execute(get, httpContext);
            statusCode = response.getStatusLine().getStatusCode();

            HttpResponse httpResponse = new HttpResponse();
            result.setModule(httpResponse);
            List<Header> headers = Arrays.asList(response.getAllHeaders());
//			CookieStore cookieStore = ( (HttpClient)closeableHttpClient).getCookieStore();
            httpResponse.setHeaders(headers);
            httpResponse.setCode(statusCode + "");

            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            List<URI> redirectLocations = httpContext.getRedirectLocations();
            String lastRedirectLocationsStr = "";
            if (redirectLocations != null && !redirectLocations.isEmpty()) {
                lastRedirectLocationsStr = redirectLocations.get(redirectLocations.size() - 1).toString();
            }
            httpResponse.setLocation(lastRedirectLocationsStr);
            String resultStr = EntityUtils.toString(response.getEntity());
            httpResponse.setBody(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);
        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    public SupplyResult<String> sendPostJsonByProxy(String url, String params, String clientKey, int connectTimeOut,
                                                    int readTimeOut, boolean isRedirect, ProxyInfo proxyInfo, Map<String, String> headers) {
        SupplyResult<String> result = new SupplyResult<String>();

        if (url == null) {
            result.setResultMsg("请求参数缺失");
            return result;
        }

        Integer statusCode = -1;
        HttpPost post = null;

        try {
            post = new HttpPost(url.trim());
        } catch (Exception e) {
            logger.error("url error " + url, e.getMessage());
            result.setStatus(SupplyResult.STATUS_FAILED);
            result.setResultMsg("url error " + url);
            return result;
        }

        logger.warn("post request: " + url + "  params : " + params);
        Builder builder = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connectTimeOut).setRedirectsEnabled(isRedirect);
        HttpContext httpContext = null;
        if (proxyInfo != null) {
            HttpProxyInfo httpProxyInfo = getHttpProxyInfo(proxyInfo, builder, isRedirect, true);
            httpContext = httpProxyInfo.getHttpContext();
            post.setConfig(httpProxyInfo.getRequestConfig());
        } else {
            post.setConfig(builder.build());
        }
        post.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");

        if (headers != null && headers.size() != 0) {
            for (Entry<String, String> e : headers.entrySet()) {
                post.setHeader(e.getKey(), e.getValue());
            }
        }

        CloseableHttpResponse response = null;
        try {

            StringEntity entity = new StringEntity(params, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            response = getHttpClientByKey(clientKey).execute(post, httpContext);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.error("connect " + url + " error httpCode : " + statusCode);
                result.setResultMsg("请求失败  code : " + statusCode);
                result.setResultCode(statusCode + "");
                return result;
            }
            String resultStr = EntityUtils.toString(response.getEntity());
            // logger.warn("post response : " + resultStr);
            result.setModule(resultStr);
            result.setStatus(SupplyResult.STATUS_SUCCESS);

        } catch (UnsupportedEncodingException e) {
            logger.error("sendDataByPost UnsupportedEncodingException ", e);
        } catch (Exception e) {
            checkException(result, e);
        } finally {
            closeResponse(response);
        }

        return result;
    }

    /**
     * 注入cookie
     *
     * @param clientKey
     * @param cookieList
     * @return
     */
    public CloseableHttpClient injectCookieByKey(String clientKey, List<Cookie> cookieList) {
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


    public void removeHttpClient(String clientKey) {
        try {
            clientMap2.asMap().remove(clientKey);
        } catch (Throwable t) {
            logger.error("", t);
        }
    }


    public CloseableHttpClient injectCookieByKey(String clientKey, Cookie cookie) {
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


    public CloseableHttpClient injectCookieByKey(String clientKey, CookieStore cookieStore) {
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

    public CloseableHttpClient injectCookieByKey(String clientKey, String cookieStr, String url) throws Exception {
        CloseableHttpClient httpClient;
        List<Cookie> cookieList = cookieStrToList(cookieStr, url);
        httpClient = injectCookieByKey(clientKey, cookieList);
        return httpClient;
    }

    public List<Cookie> cookieStrToList(String cookieStr, String url) throws Exception {
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

    public CookieStore cloneCookieStore(CookieStore cookieStore) {
        CookieStore cookieStoreNew = new BasicCookieStore();
        try {
            if (cookieStore == null) {
                return null;
            }
            for (Cookie cookie :
                    cookieStore.getCookies()) {
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                BeanUtils.copyProperties(cookie, basicClientCookie);
                cookieStoreNew.addCookie(basicClientCookie);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return cookieStoreNew;
    }

    public  void removeHttpClientByKey(String clientKey) {
        clientMap2.asMap().remove(clientKey);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        ProxyInfo proxyInfo = new ProxyInfo();
        proxyInfo.setProxyHost("27.191.71.155");//47.254.130.16:8010  115.29.215.94:8020  112.124.105.0:6060
        proxyInfo.setProxyPort(4241);

//		proxyInfo.setUser("H61E03L4F2P634WP");
//		proxyInfo.setPwd("6477A310A0A9A996");

//		proxyInfo.setUser("root");
//		proxyInfo.setPwd("wanliang.0");

        long time = System.currentTimeMillis();
        System.out.println(HttpPoolClient.INS.sendGetByProxy("http://office.kanepan.top:889/", proxyInfo, "123", null));

        System.out.println("耗时 :" + (System.currentTimeMillis() - time) / 1000 + "秒");
    }

}
