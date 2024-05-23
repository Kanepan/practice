package com.kane.practice.program.logicexecutor.core.context;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseContext {

    private String name;

    private String desc;
    /**
     * 减去金额
     */
    private BigDecimal discountFee;


    public String getName() {
        String proName = "";
        if (this.getDiscountFee() != null && this.getDiscountFee().compareTo(BigDecimal.ZERO) > 0) {
            proName = proName + "省" + this.getDiscountFee() + "元";
            return proName;
        } else {
            return name;
        }
    }

}
