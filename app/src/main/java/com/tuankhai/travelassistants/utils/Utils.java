package com.tuankhai.travelassistants.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Khai on 31/08/2017.
 */

public class Utils {

    public static <T> T readValue(byte[] data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static <T> T readValue(String data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }
}
