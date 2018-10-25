package com.kane.practice.utils.http;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.Header;

import java.util.List;

public class HttpResponse {
    private String location;
    private List<Header> headers;
    private String code;
    private String body;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
