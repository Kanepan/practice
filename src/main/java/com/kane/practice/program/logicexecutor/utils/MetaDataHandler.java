package com.kane.practice.program.logicexecutor.utils;


import com.google.gson.*;
import com.kane.practice.program.logicexecutor.core.MetaParser;
import com.kane.practice.program.logicexecutor.core.domain.metadata.*;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import com.kane.practice.program.logicexecutor.meta.MetaContext;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetaDataHandler implements JsonSerializer<MetaData>, JsonDeserializer<MetaData> {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_ELEMENTS = "childs";
    private static final String FIELD_META_ID = "metaId";
    private static final String FIELD_PARAMS = "params";
//    private static final String FIELD_TRADE_STATUS = "tradeStatus";

    private static final String FIELD_CONDITION = "condition";
    private static final String FIELD_ACTION = "action";
    private static final String FIELD_PARAM_ID = "paramId";

    private static final String FIELD_META_NAME = "name";

    /**
     * {@inheritDoc}
     */
    public JsonElement serialize(MetaData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonMetaData = new JsonObject();

        MetaDataType type = MetaDataType.valueOf(src);
        jsonMetaData.addProperty(FIELD_TYPE, type.toString());

        switch (type) {
            case AND:
            case OR:
            case NOT:
                serializeCompositeMeta((MultiMetaData) src, context, jsonMetaData);
                break;
            case PAIR:
                serializeDualMeta((PairMetaData) src, context, jsonMetaData);
                break;
//            case CONDITIONAL:
//                serializeCompositeMeta((CompositeMetaData) src, context, jsonMetaData);
//                jsonMetaData.addProperty(FIELD_PARAM_ID, src.getParameterValues()[0].getId());
//                break;
//            case TARGET:
            case CONDITION:
            case ACTION:
            case RESOURCE:
//                jsonMetaData.addProperty(FIELD_META_ID, src.getMetaDef().getMetaName());
                jsonMetaData.add(FIELD_PARAMS, context.serialize(src.getParams()));
                jsonMetaData.addProperty(FIELD_META_NAME, src.getMetaDef().getClass().getSimpleName());

//                if (src.getTradeStatus() != null) {
//                    jsonMetaData.addProperty(FIELD_TRADE_STATUS, src.getTradeStatus());
//                }
                break;
            case UNKNOWN:
            default:
        }

        return jsonMetaData;
    }

    private void serializeCompositeMeta(MultiMetaData metaData,
                                        JsonSerializationContext context, JsonObject jsonMetaData) {
        Collection<MetaData> metaDatas = metaData.getChildMetaDatas();
        JsonElement jsonMetaDataList = context.serialize(metaDatas, MetaParser.TYPE_METADATA_LIST);
        jsonMetaData.add(FIELD_ELEMENTS, jsonMetaDataList);
    }

    private void serializeDualMeta(PairMetaData pairMetaData,
                                   JsonSerializationContext context, JsonObject jsonMetaData) {
        JsonElement jsonCondition = context.serialize(pairMetaData.getConditon(), MetaParser.TYPE_METADATA);
        jsonMetaData.add(FIELD_CONDITION, jsonCondition);
        JsonElement jsonAction = context.serialize(pairMetaData.getAction(), MetaParser.TYPE_METADATA);
        jsonMetaData.add(FIELD_ACTION, jsonAction);
    }

    /**
     * {@inheritDoc}
     */
    public MetaData deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonMetaData = (JsonObject) json;

            MetaDataType type = MetaParser.getStringAsEnum(jsonMetaData.get(FIELD_TYPE), MetaDataType.UNKNOWN);
            switch (type) {
                case AND:
                    return new AndMetaData(deserializeMetaDataList(jsonMetaData, context));
                case OR:
                    return new OrMetaData(deserializeMetaDataList(jsonMetaData, context));
                case NOT:
                    return new NotMetaData(deserializeMetaDataList(jsonMetaData, context).get(0));
                case PAIR:
                    return deserializePairMeta(jsonMetaData, context);
//                case CONDITIONAL:
//                    return deserializeConditionalMetaData(jsonMetaData, context);
//                case TARGET:
                case CONDITION:
                case ACTION:
                case RESOURCE:
                    return deserializeRegularMeta(type, jsonMetaData, context);
                case UNKNOWN:
                default:
                    throw new JsonParseException("未知的 MetaData 类型: " + type);
            }
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonParseException("从 Json 中读取 MetaData 失败");
        }
    }

    private List<MetaData> deserializeMetaDataList(JsonObject jsonCompositeMetaData,
                                                   JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonArray jsonMetaDataList = jsonCompositeMetaData.get(FIELD_ELEMENTS).getAsJsonArray();
            final int size = jsonMetaDataList.size();

            if (size < 1) {
                throw new JsonParseException(
                        "复合型 MetaData 必须有 " + FIELD_ELEMENTS + "字段");
            }

            ArrayList<MetaData> list = new ArrayList<MetaData>(size);

            for (int i = 0; i < size; i++) {
                JsonElement jsonMetaDataElement = jsonMetaDataList.get(i);
                MetaData metaData = context.deserialize(jsonMetaDataElement, MetaParser.TYPE_METADATA);
                list.add(metaData);
            }

            return list;
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("从 Json 中读取复合型 MetaData 失败", e);
        }
    }

    private MetaData deserializePairMeta(JsonObject jsonDualMetaData, JsonDeserializationContext context) {
        MetaData condition = context.deserialize(jsonDualMetaData.get(FIELD_CONDITION), MetaParser.TYPE_METADATA);
        MetaData action = context.deserialize(jsonDualMetaData.get(FIELD_ACTION), MetaParser.TYPE_METADATA);
        return new PairMetaData(condition, action);
    }

//    private ConditionalMetaData deserializeConditionalMetaData(JsonObject jsonConditionalMetaData,
//                                                               JsonDeserializationContext context) throws JsonParseException {
//        MetaData metaData = deserializeMetaDataList(jsonConditionalMetaData, context).get(0);
//        String conditionalParam = jsonConditionalMetaData.get(FIELD_PARAM_ID).getAsString();
//        return new ConditionalMetaData(metaData, conditionalParam);
//    }

    private MetaData deserializeRegularMeta(MetaDataType type, JsonObject jsonMetaData, JsonDeserializationContext context) {
//        MetaDef metaDef = new VirtualMetaDef(jsonMetaData.get(FIELD_META_ID).getAsLong(), type);


        Class<?> clazz = null;
        try {
            clazz = Class.forName(MetaContext.getMetaClassName(jsonMetaData.get(FIELD_META_NAME).getAsString()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 实例化对象
        Object instance = BeanUtils.instantiateClass(clazz);
        Meta meta = (Meta) instance;
//        MetaData metaData = context.deserialize(jsonMetaData, MetaParser.TYPE_METADATA);
        Param[] params = context.deserialize(
                jsonMetaData.get(FIELD_PARAMS), Param[].class);

        return new SingleMetaData(meta, params);
    }
}
