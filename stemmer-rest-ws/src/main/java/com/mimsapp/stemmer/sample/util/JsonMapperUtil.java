package com.mimsapp.stemmer.sample.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonMapperUtil {

    public static <T> String convertToJson(T request) {

        String json = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            json = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static <T> T convertToObject(String json, Class<T> clazz) {

        T obj = null;

        if(json != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                obj = mapper.readValue(json, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }
}
