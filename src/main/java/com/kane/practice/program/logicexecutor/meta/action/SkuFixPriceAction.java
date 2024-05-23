package com.kane.practice.program.logicexecutor.meta.action;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Action;

import java.math.BigDecimal;

public class SkuFixPriceAction implements Action {
    @Override
    public boolean execute(LogicContext context) {
        String fixPriceStr = (String) context.getParam("skuFixPrice");
        BigDecimal fixPrice = new BigDecimal(fixPriceStr);

        Integer sum = context.getOrderSum();
        BigDecimal discountAmount = fixPrice.multiply(new BigDecimal(sum));

        BigDecimal discountFee = context.getAmount().subtract(discountAmount);
        BigDecimal oldDiscountFee = context.getResponseContext().getDiscountFee();
        if (oldDiscountFee == null) {
            oldDiscountFee = BigDecimal.ZERO;
        }

        context.getResponseContext().setDiscountFee(oldDiscountFee.add(discountFee));
        System.out.println(String.format("原价%s 一口价后%s", context.getAmount(), discountAmount));
        System.out.println("优惠信息" + context.getResponseContext().getName());
        return true;
    }
}
