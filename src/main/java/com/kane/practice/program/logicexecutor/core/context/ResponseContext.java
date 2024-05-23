package com.kane.practice.program.logicexecutor.core.context;

import lombok.Data;

@Data
public class ResponseContext {

    private String name;

    private String desc;
    /**
     * 减去金额，单位为分
     */
    private long discountFee;

    /**
     * 免邮额度,单位为分
     */
    private long discountPostFee;
}
