package com.kane.practice.program.logicexecutor.meta.action;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Action;

import java.math.BigDecimal;

public class DiscountAction implements Action {
//    @Override
//    public String getMetaName() {
//        return "";
//    }

    @Override
    public boolean execute(LogicContext context) {
        String discountStr = (String) context.getParam("discountRate");
        BigDecimal amount = (BigDecimal) context.getResource("OrderAmountResource");
        BigDecimal discountAmount = (new BigDecimal(discountStr).multiply(amount)).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal discountFee = amount.subtract(discountAmount);
        if (discountFee.compareTo(BigDecimal.ZERO) < 0) {
            discountFee = BigDecimal.ZERO;
        }
        context.getResponseContext().setDiscountFee(discountFee);
        System.out.println(String.format("原价%s 打折后%s", amount, discountAmount));
        return true;
    }
}
