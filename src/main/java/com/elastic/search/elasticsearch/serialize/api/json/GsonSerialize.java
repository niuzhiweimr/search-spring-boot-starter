package com.elastic.search.elasticsearch.serialize.api.json;

import com.google.gson.Gson;

/**
 * @author niuzhiwei
 */
public class GsonSerialize implements SerializeJsonApi {

    public static GsonSerialize INSTANCE = new GsonSerialize();

    private static Gson gson = new Gson();

    @Override
    public String encode(Object t) {
        return gson.toJson(t);
    }

    @Override
    public <T> T decode(String json, Class<T> clazz) {
        return (T) gson.fromJson(json, clazz);
    }

    @Override
    public SerializeJsonApi getInstance() {
        return INSTANCE;
    }
}

