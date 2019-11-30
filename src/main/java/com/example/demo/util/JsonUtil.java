package com.example.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangzongbo
 */
public class JsonUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static JsonNode getJsonNode(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return MissingNode.getInstance();
        }
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Json to JsonNode failed!");
        }
        return jsonNode;
    }

    public static String toJSONString(Object object) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to Json failed!");
        }
        return jsonString;
    }

    public static <T> T parseObject(String jsonString, Class<T> type) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (Exception e) {
            throw new RuntimeException("Json to Object Failed!", e);
        }
    }

    public static <T> List<T> parseList(String jsonString, Class<T> type) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, getCollectionType(ArrayList.class, type));
        } catch (Exception e) {
            throw new RuntimeException("Json to Object Failed!", e);
        }
    }

    public static <K, V> HashMap<K, V> convertMap(Object object, Class<K> key, Class<V> value) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(object, getCollectionType(HashMap.class, key, value));
        } catch (Exception e) {
            throw new RuntimeException("Object to Map Failed!", e);
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
