package com.market.util;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule("DateTimeModule", Version.unknownVersion());
        module.addSerializer(Date.class, new DateSerializer());
        module.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        // null的字段不输出,减少数据量,也避免.NET系统用基本类型反序列化本系统的包装类型字段,导致出错
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(module);
    }

    public static ObjectMapper getObjectMapperInstance() {
        return objectMapper;
    }

    /**
     * 将object序列化为string
     *
     * @param obj
     * @return String
     */
    public static String jsonSerialize(Object obj) {
        String str = null;
        try {
            str = objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            Log.e("","JsonUtils.jsonSerialize():%s", ex);
        }
        return str;
    }

    /**
     * 将string反序列化为object
     *
     * @param str
     * @param tClass
     * @return T
     */
    public static <T> T jsonDeserialize(String str, Class<T> tClass) {
        T instance = null;
        try {
            instance = objectMapper.readValue(str, tClass);
        } catch (Exception ex) {
            Log.e("","JsonUtils.jsonDeserialize():%s", ex);
        }
        return instance;
    }

    public static <T> T jsonDeserialize(String str, TypeReference<T> tType) {
        T instance = null;
        try {
            instance = objectMapper.readValue(str, tType);
        } catch (Exception ex) {
            Log.e("","JsonUtils.jsonDeserialize():%s", ex);
        }
        return instance;
    }

    /**
     * 反序列化成list
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonDeserialize2List(String json, Class<T> clazz) {
        if (json == null || json.equals("") || Objects.isNull(clazz)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            Log.e("","JsonUtils.jsonDeserialize2List():%s", e);
            return null;
        }
    }
}
