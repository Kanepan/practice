package com.kane.practice.program.logicexecutor.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.kane.practice.program.logicexecutor.core.domain.metadata.MetaData;
import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import com.kane.practice.program.logicexecutor.utils.MetaDataHandler;
import com.kane.practice.program.logicexecutor.utils.ParameterValueHandler;

import java.lang.reflect.Type;
import java.util.List;

public class MetaParser {
    static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final Type TYPE_METADATA = new TypeToken<MetaData<Meta>>() {
    }.getType();
    public static final Type TYPE_METADATA_LIST = new TypeToken<List<MetaData<Meta>>>() {
    }.getType();

    private static final ThreadLocal<Gson> localGson = new ThreadLocal<Gson>() {
        @Override
        protected Gson initialValue() {
            return new GsonBuilder()
                    .registerTypeAdapter(MetaData.class, new MetaDataHandler())
                    .registerTypeAdapter(Param.class, new ParameterValueHandler())
//                    .registerTypeAdapter(MarketingTool.class, new MarketingToolHandler())
//                    .registerTypeAdapter(MarketingActivity.class, new MarketingActivityHandler())
//                    .registerTypeAdapter(MarketingDetail.class, new MarketingDetailHandler())
                    .setDateFormat(JSON_DATE_FORMAT)
                    .serializeNulls()
                    .create();
        }
    };

    static String toJsonString(MetaData<Meta> metaData) {
        JsonElement jsonElement = localGson.get().toJsonTree(metaData, TYPE_METADATA);

        return localGson.get().toJson(jsonElement);
    }

    static <T extends Meta> MetaData<T> fromJson(String jsonMetaData) {
        return localGson.get().fromJson(jsonMetaData, TYPE_METADATA);
    }


    public static String getAsStringQuietly(JsonElement jsonElement, String defaultValue) {
        if (jsonElement != null && jsonElement instanceof JsonPrimitive) {
            try {
                return jsonElement.getAsString();
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static <E extends Enum<E>> E getStringAsEnum(JsonElement jsonElement, E defaultValue) {
        if (jsonElement != null && jsonElement instanceof JsonPrimitive) {
            try {
                return (E) Enum.valueOf(defaultValue.getClass(), jsonElement.getAsString());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }


}

