package com.kane.practice.program.logicexecutor.utils;

import com.google.gson.*;
import com.kane.practice.program.logicexecutor.core.MetaParser;
import com.kane.practice.program.logicexecutor.core.domain.param.DefaultParam;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;

import java.lang.reflect.Type;

public class ParameterValueHandler implements JsonSerializer<Param>, JsonDeserializer<Param> {

    private static final String FIELD_CODE = "paramCode";
    private static final String FIELD_KIND = "kind";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_ARRAY = "array";
    private static final String FIELD_VALUE = "value";

    /**
     * {@inheritDoc}
     */
    public JsonElement serialize(Param src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonParameter = new JsonObject();

        Param.Type kind = src.getType();
        jsonParameter.addProperty(FIELD_CODE, src.getName());
        jsonParameter.addProperty(FIELD_KIND, kind.toString());

        switch (kind) {
            case DEFINED:
//                ParameterDef parameterDef = src.getParameterDef();
//                ValueType valueType = parameterDef.getValueType();
//                boolean isArray = parameterDef.isArray();

//                jsonParameter.addProperty(FIELD_TYPE, valueType.toString());
//                if (isArray) {
//                    jsonParameter.addProperty(FIELD_ARRAY, isArray);
//                }
//
//                Class<?> valueClass = getValueClass(valueType, isArray);
//                try {
//                    jsonParameter.add(FIELD_VALUE,
//                            context.serialize(src.getValue(), valueClass));
//                } catch (ClassCastException e) {
//                    throw new RuntimeException(
//                            "参数值的类型与定义所声明的不一致, 声明类型为: " + valueClass.getName() +
//                                    ", 实际类型为: " + src.getValue().getClass().getName() +
//                                    ", paramId=" + src.getId() +
//                                    ", paramDefName=" + parameterDef.getName() +
//                                    ", metaDef=" + src.getParentMeta(), e);
//                }
                break;
            case RESOURCE:
                jsonParameter.add(FIELD_VALUE, context.serialize(src.getValue(), MetaParser.TYPE_METADATA));
                break;
            case SETTING:
                // nothing to do
        }

        return jsonParameter;
    }

    /**
     * {@inheritDoc}
     */
    public Param deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json instanceof JsonObject) {
            JsonObject jsonParameter = (JsonObject) json;

            String code = MetaParser.getAsStringQuietly(jsonParameter.get(FIELD_CODE), null);
            Param.Type kind = MetaParser.getStringAsEnum(jsonParameter.get(FIELD_KIND), Param.Type.SETTING);

            JsonElement jsonParamValue = jsonParameter.get(FIELD_VALUE);

            switch (kind) {
                case DEFINED:
//                    ValueType valueType = MetaParser.getStringAsEnum(jsonParameter.get(FIELD_TYPE), ValueType.String);
//                    boolean isArray = MetaParser.getAsBooleanQuietly(jsonParameter.get(FIELD_ARRAY), false);
//
//                    Class<?> type = getValueClass(valueType, isArray);
//                    Object value = context.deserialize(jsonParamValue, type);
//
//                    value = MarketingUtils.castAsValueType(value, valueType, isArray);
//                    return new DefaultParam(id, null, null, kind, null);
                    return new DefaultParam(code, kind);
                case RESOURCE:
//                    MetaData<Resource> resourceMeta = context.deserialize(jsonParamValue,
//                            MetaParser.TYPE_METADATA);
                    return new DefaultParam(code, kind);
                case SETTING:
                    return new DefaultParam(code, kind);
            }
        }
        throw new JsonParseException(
                "Illegal Json format of ParameterValue while parsing the Json String");
    }

//    private Class<?> getValueClass(ValueType valueType, boolean isArray) {
//        Class<?> type = valueType.getType();
//        // 对 Number 类型进行松弛
//        if (Number.class.isAssignableFrom(type)) {
//            type = Number.class;
//        }
//        if (isArray) {
//            return Array.newInstance(type, 0).getClass();
//        } else {
//            return type;
//        }
//    }
}