package com.kane.practice.program.logicexecutor.meta.action;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.meta.Action;

public class DiscountAction implements Action {
//    @Override
//    public String getMetaName() {
//        return "";
//    }

    @Override
    public boolean execute(LogicContext context) {
//        Integer amount = (Integer) context.getParams()[0].getValue();
//        Integer amount = (Integer) context.getParams()[0].getValue();

//        System.out.println("DiscountAction execute: " + amount);
//        context.setReturnValue("DiscountAction execute");
        System.out.println("DiscountAction execute");
        return true;
    }
}
