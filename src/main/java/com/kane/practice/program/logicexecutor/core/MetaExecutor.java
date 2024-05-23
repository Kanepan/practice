package com.kane.practice.program.logicexecutor.core;


import com.kane.practice.program.logicexecutor.core.context.LogicContext;
import com.kane.practice.program.logicexecutor.core.domain.metadata.*;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.*;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

public class MetaExecutor {

    public boolean excute(MetaData<Meta> metaData, LogicContext context) {
        if (metaData instanceof AndMetaData) {
            List<MetaData> childs = ((AndMetaData) metaData).getChildMetaDatas();
            for (MetaData child : childs) {
                // And: 一旦有 false，马上返回 false
                if (!excute(child, context)) {
                    return false;
                }
            }
            return true;
        } else if (metaData instanceof OrMetaData) {
            // Or: 一旦有 true，马上返回 true
            List<MetaData> childs = ((OrMetaData) metaData).getChildMetaDatas();
            for (MetaData child : childs) {
                if (excute(child, context)) {
                    return true;
                }
            }

            return false;
        } else if (metaData instanceof NotMetaData) {
            // Not: 取反
            MetaData<Meta> meta = ((NotMetaData) metaData).getMetaData();
            return !excute(meta, context);
        } else if (metaData instanceof PairMetaData) {
            PairMetaData pair = ((PairMetaData) metaData);
            MetaData conditon = pair.getConditon();
            MetaData action = pair.getAction();
            if (excute(conditon, context)) {
                return excute(action, context);
            }
            return false;
        } else {
            // DefaultMetaData
            return executeLeafMeta(metaData, context);
        }
    }

    private boolean executeLeafMeta(MetaData<Meta> metaData, LogicContext context) {
        SingleMetaData singleMetaData = (SingleMetaData) metaData;

        doGetResource(singleMetaData, context);

        Meta meta = singleMetaData.getMeta();
        if (meta instanceof Condition) {
            return ((Condition) meta).execute(context);
        } else if (meta instanceof Action) {
            boolean result = ((Action) meta).execute(context);
            if (result) {
                context.setHasBenefit(true);
            }
            return result;
        }
        return false;
    }

    private void doGetResource(SingleMetaData singleMetaData, LogicContext context) {
        Param[] params = singleMetaData.getParams();
        if (params == null || params.length == 0) {
            return;
        }
        Arrays.stream(params).forEach(param -> {
            if (Param.Type.RESOURCE.equals(param.getType())) {
                // get resource
                Resource resource = getResourceObject(param.getName());
                if (resource == null) {
                    throw new RuntimeException("Resource not found: " + param.getName());
                }
                Object res = resource.getResource(context);
                context.putResource(param.getName(), res);
            }
        });
    }


    private Resource getResourceObject(String resourceName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(MetaContext.getMetaClassName(resourceName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 实例化对象
        Object instance = BeanUtils.instantiateClass(clazz);
        return (Resource) instance;
    }
}
