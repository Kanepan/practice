package com.kane.practice.program.logicexecutor.utils;

import com.kane.practice.program.logicexecutor.core.domain.metadata.*;
import com.kane.practice.program.logicexecutor.meta.Action;
import com.kane.practice.program.logicexecutor.meta.Condition;
import com.kane.practice.program.logicexecutor.meta.Meta;
import com.kane.practice.program.logicexecutor.meta.Resource;

public enum MetaDataType {

    /**
     * 未知类型
     */
    UNKNOWN,

    /**
     * 组装上的“逻辑与”关系
     */
    AND,
    /**
     * 组装上的“逻辑或”关系
     */
    OR,
    /**
     * 组装上的“逻辑非”关系
     */
    NOT,

    /** TARGET 类型 */
//    TARGET,
    /**
     * CONDITION 类型
     */
    CONDITION,
    /**
     * ACTION 类型
     */
    ACTION,
    /**
     * RESOURCE 类型
     */
    RESOURCE,

    /**
     * 组装了<code>ConditionDef</code>和<code>ActionDef</code>
     * 的<code>OperationDef</code>
     */
    PAIR,
    /**
     * 绑定了一个 boolean 参数的元数据
     */
//    CONDITIONAL;
    ;

    public static MetaDataType valueOf(MetaData<Meta> meta) {
        if (meta instanceof AndMetaData) {
            return AND;
        } else if (meta instanceof OrMetaData) {
            return OR;
        } else if (meta instanceof NotMetaData) {
            return NOT;
        } else if (meta instanceof PairMetaData) {
            return PAIR;
        } else if (meta instanceof SingleMetaData) {
            if (((SingleMetaData) meta).getMeta() instanceof Condition) {
                return CONDITION;
            } else if (((SingleMetaData) meta).getMeta() instanceof Action) {
                return ACTION;
            } else if (((SingleMetaData) meta).getMeta() instanceof Resource) {
                return RESOURCE;
            }
            return UNKNOWN;
        }
//        else if (meta instanceof ConditionalMetaData) {
//            return CONDITIONAL;
//        }
        else if (meta != null) {
            return valueOf(meta);
        }
        return UNKNOWN;
    }


}
