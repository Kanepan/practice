package com.kane.practice.program.logicexecutor.core.context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class LogicContext {
    private Map<String, Object> paramsMap = new HashMap<>();

    private ResponseContext responseContext = new ResponseContext();

    private Map<String, Object> resourceContext = new HashMap<>();

    private BigDecimal amount;

    private Integer orderSum;

    private int multiple = 1;

    private BigDecimal discountMoney;

    private boolean hasBenefit = false;


    public ResponseContext getResponseContext() {
        return responseContext;
    }

    public void putResource(String key, Object value) {
        resourceContext.put(key, value);
    }

    public Object getResource(String key) {
        return resourceContext.get(key);
    }

    public void putParam(String key, Object value) {
        paramsMap.put(key, value);
    }

    public Object getParam(String key) {
        return paramsMap.get(key);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public boolean isHasBenefit() {
        return hasBenefit;
    }

    public void setHasBenefit(boolean hasBenefit) {
        this.hasBenefit = hasBenefit;
    }
}
