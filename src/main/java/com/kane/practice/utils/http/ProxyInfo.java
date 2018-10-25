package com.kane.practice.utils.http;

import java.util.concurrent.atomic.AtomicLong;

public class ProxyInfo {
    private String proxyHost;
    private int proxyPort;
    private String proxyUserName;
    private String proxyPwd;
    private String proxyType;// Proxy.Type HTTP SOCKS DIRECT
    private String location;
    private boolean isAnonymity;// 是否匿名
    private AtomicLong useCount=new AtomicLong(0);
    private AtomicLong failCount=new AtomicLong(0);

    public ProxyInfo() {
        super();
    }

    public ProxyInfo(String proxyHost, int proxyPort, String proxyType) {
        super();
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyType = proxyType;
    }

    public ProxyInfo(String proxyHost, int proxyPort, String proxyUserName, String proxyPwd, String proxyType) {
        super();
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUserName = proxyUserName;
        this.proxyPwd = proxyPwd;
        this.proxyType = proxyType;
    }

    public String getProxyHost() {

        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {

        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {

        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {

        this.proxyPort = proxyPort;
    }

    public String getProxyUserName() {

        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {

        this.proxyUserName = proxyUserName;
    }

    public String getProxyPwd() {

        return proxyPwd;
    }

    public void setProxyPwd(String proxyPwd) {

        this.proxyPwd = proxyPwd;
    }

    public String getProxyType() {

        return proxyType;
    }

    public void setProxyType(String proxyType) {

        this.proxyType = proxyType;
    }

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }

    public boolean isAnonymity() {

        return isAnonymity;
    }

    public void setAnonymity(boolean isAnonymity) {

        this.isAnonymity = isAnonymity;
    }

    public AtomicLong getUseCount() {
        return useCount;
    }

    public AtomicLong getFailCount() {
        return failCount;
    }

    public  Long useCountjia(){
        return useCount.getAndIncrement();
    }

    public  Long failCountjia(){
        return failCount.getAndIncrement();
    }

    @Override
    public String toString() {
        return "ProxyInfo{" +
                "proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUserName='" + proxyUserName + '\'' +
                ", proxyPwd='" + proxyPwd + '\'' +
                ", proxyType='" + proxyType + '\'' +
                ", location='" + location + '\'' +
                ", isAnonymity=" + isAnonymity +
                ", useCount=" + useCount +
                ", failCount=" + failCount +
                '}';
    }
}
