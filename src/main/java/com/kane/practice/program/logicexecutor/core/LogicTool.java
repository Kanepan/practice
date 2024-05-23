package com.kane.practice.program.logicexecutor.core;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.core.domain.metadata.MetaData;
import com.kane.practice.program.logicexecutor.core.domain.metadata.PairMetaData;
import com.kane.practice.program.logicexecutor.core.domain.metadata.SingleMetaData;
import com.kane.practice.program.logicexecutor.core.domain.param.DefaultParam;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import com.kane.practice.program.logicexecutor.meta.action.DiscountAction;
import com.kane.practice.program.logicexecutor.meta.condition.OverAmountCondition;
import com.kane.practice.program.logicexecutor.meta.condition.OverSumCondition;

import java.math.BigDecimal;

public class LogicTool {
    private MetaData<Meta> metaData;

    public void build(MetaData<Meta> metaData) {
        this.metaData = metaData;
        //metaData to json
        String json = MetaParser.toJsonString(metaData);
        System.out.println("json: " + json);
    }


    public static void main(String[] args) {
        OverAmountCondition overAmountCondition = new OverAmountCondition();
        Param[] pp = new Param[]{
                new DefaultParam("OrderAmountResource", Param.Type.RESOURCE),
                new DefaultParam("overAmount", Param.Type.SETTING)
        };
        MetaData<Meta> metaDataCondtion = new SingleMetaData<>(overAmountCondition, pp);

        OverSumCondition overSumCondition = new OverSumCondition();
        Param[] pp2 = new Param[]{
                new DefaultParam("OrderSumResource", Param.Type.RESOURCE),
                new DefaultParam("overSum", Param.Type.SETTING)
        };
        MetaData<Meta> metaDataConditon2 = new SingleMetaData<>(overSumCondition, pp2);

        MetaData<Meta> conditions = metaDataCondtion.or(metaDataConditon2);

        DiscountAction discountAction = new DiscountAction();
        MetaData<Meta> metaDataAction = new SingleMetaData<>(discountAction, null);

        PairMetaData<Meta> pairMetaData = new PairMetaData<>(conditions, metaDataAction);


        // save pairMetaData
        LogicTool logicTool = new LogicTool();

        String json = MetaParser.toJsonString(pairMetaData);
        System.out.println("json: " + json);

        // load pairMetaData
        MetaData<Meta> metaData = MetaParser.fromJson(json);

        // 设置 参数

        MetaExecutor metaExecutor = new MetaExecutor();
        LogicContext context = new LogicContext();

        context.putParam("overAmount", new BigDecimal(1000));
        context.putParam("overSum", 2);

        context.setAmount(new BigDecimal(1800));
        context.setOrderSum(1);

        boolean result = metaExecutor.excute(metaData, context);
        System.out.println("最终结果" + result);
    }

}
