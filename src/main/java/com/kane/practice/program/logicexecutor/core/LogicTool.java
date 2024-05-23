package com.kane.practice.program.logicexecutor.core;

import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.core.domain.metadata.MetaData;
import com.kane.practice.program.logicexecutor.core.domain.metadata.PairMetaData;
import com.kane.practice.program.logicexecutor.core.domain.metadata.SingleMetaData;
import com.kane.practice.program.logicexecutor.core.domain.param.DefaultParam;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import com.kane.practice.program.logicexecutor.meta.action.DiscountAction;
import com.kane.practice.program.logicexecutor.meta.action.SkuFixPriceAction;
import com.kane.practice.program.logicexecutor.meta.condition.OverAmountCondition;
import com.kane.practice.program.logicexecutor.meta.condition.OverSumCondition;
import com.kane.practice.program.logicexecutor.meta.condition.StepCheck;

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
        rule2();
    }


    private static void rule2() {
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

        MetaData<Meta> sumOrAmountConditions = metaDataCondtion.or(metaDataConditon2);


//        MetaData<Meta> limitCondtion = new SingleMetaData<>(new LimitCheck(), new Param[]{
//                new DefaultParam("OrderSumResource", Param.Type.RESOURCE),
//                new DefaultParam("limitCheck", Param.Type.SETTING)
//        });

        MetaData<Meta> stepCondtion = new SingleMetaData<>(new StepCheck(), new Param[]{
                new DefaultParam("OrderSumResource", Param.Type.RESOURCE),
                new DefaultParam("stepCheck", Param.Type.SETTING)
        });

        MetaData<Meta> conditions = stepCondtion.and(sumOrAmountConditions);


        SkuFixPriceAction skuFixPriceAction = new SkuFixPriceAction();
        Param[] pp3 = new Param[]{
//                new DefaultParam("OrderAmountResource", Param.Type.RESOURCE),
                new DefaultParam("skuFixPrice", Param.Type.SETTING)
        };
        MetaData<Meta> metaDataAction = new SingleMetaData<>(skuFixPriceAction, pp3);

        PairMetaData<Meta> pairMetaData = new PairMetaData<>(conditions, metaDataAction);


        // save pairMetaData
        LogicTool logicTool = new LogicTool();

        String json = MetaParser.toJsonString(pairMetaData);
        System.out.println("json: " + json);

        // load pairMetaData
        MetaData<Meta> metaData = MetaParser.fromJson(json);


        MetaExecutor metaExecutor = new MetaExecutor();
        LogicContext context = new LogicContext();

        context.putParam("overAmount", new BigDecimal(1000));
        context.putParam("overSum", 2);
        context.putParam("skuFixPrice", "10");
//        context.putParam("limitCheck", 5);
        context.putParam("stepCheck", 5);

        context.setAmount(new BigDecimal(300));
        context.setOrderSum(5);

        boolean result = metaExecutor.excute(metaData, context);
        System.out.println("最终结果" + result);
    }

    private static void rule1() {
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

        MetaData<Meta> sumOrAmountConditions = metaDataCondtion.or(metaDataConditon2);


//        MetaData<Meta> limitCondtion = new SingleMetaData<>(new LimitCheck(), new Param[]{
//                new DefaultParam("OrderSumResource", Param.Type.RESOURCE),
//                new DefaultParam("limitCheck", Param.Type.SETTING)
//        });

        MetaData<Meta> stepCondtion = new SingleMetaData<>(new StepCheck(), new Param[]{
                new DefaultParam("OrderSumResource", Param.Type.RESOURCE),
                new DefaultParam("stepCheck", Param.Type.SETTING)
        });

        MetaData<Meta> conditions = stepCondtion.and(sumOrAmountConditions);


        DiscountAction discountAction = new DiscountAction();
        Param[] pp3 = new Param[]{
                new DefaultParam("OrderAmountResource", Param.Type.RESOURCE),
                new DefaultParam("discountRate", Param.Type.SETTING)
        };
        MetaData<Meta> metaDataAction = new SingleMetaData<>(discountAction, pp3);

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
        context.putParam("discountRate", "0.8");
//        context.putParam("limitCheck", 5);
        context.putParam("stepCheck", 5);

        context.setAmount(new BigDecimal(100));
        context.setOrderSum(5);

        boolean result = metaExecutor.excute(metaData, context);
        System.out.println("最终结果" + result);
    }

}
