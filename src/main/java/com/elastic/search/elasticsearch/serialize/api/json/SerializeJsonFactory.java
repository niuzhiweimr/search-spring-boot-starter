package com.elastic.search.elasticsearch.serialize.api.json;


import com.elastic.search.common.exception.ErrorCode;

/**
 * @author niuzhiwei
 */
public class SerializeJsonFactory {

    private static boolean support_jackson = false, support_gson = false;

    static {
        init();
    }

    private static void init() {
        try {
            Class.forName("com.google.gson.Gson");
            support_gson = true;
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            support_jackson = true;
        } catch (ClassNotFoundException e) {
        }
    }

    public static SerializeJsonApi getInstance() {
        if (support_jackson) {
            return JacksonSerialize.INSTANCE;
        } else if (support_gson) {
            return GsonSerialize.INSTANCE;
        } else {
            throw ErrorCode.JSON_SERIALIZE_FAIL.throwError("没有支持的JSON序列化实现");
        }
    }

}

