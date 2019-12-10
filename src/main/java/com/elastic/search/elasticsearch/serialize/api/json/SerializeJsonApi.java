package com.elastic.search.elasticsearch.serialize.api.json;

import com.elastic.search.elasticsearch.serialize.SerializeApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niuzhiwei
 */
public interface SerializeJsonApi extends SerializeApi<String> {

    public static final Logger LOGGER = LoggerFactory.getLogger(SerializeApi.class);

    /**
     * 序列化
     */
    @Override
    public String encode(Object t);

    /**
     * 反序列化
     */
    @Override
    public <T> T decode(String json, Class<T> type);

    public SerializeJsonApi getInstance();
}