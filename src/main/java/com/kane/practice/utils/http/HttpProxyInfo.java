package com.kane.practice.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;

public class HttpProxyInfo {

	private HttpClientContext httpContext;
	private RequestConfig requestConfig;

	public HttpClientContext getHttpContext() {
		return httpContext;
	}

	public void setHttpContext(HttpClientContext httpContext) {
		this.httpContext = httpContext;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}
	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

}
