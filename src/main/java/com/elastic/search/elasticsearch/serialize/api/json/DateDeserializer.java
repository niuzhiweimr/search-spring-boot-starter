package com.elastic.search.elasticsearch.serialize.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.elastic.search.elasticsearch.serialize.api.json.parser.ParserDateFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * @author niuzhiwei
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jp.getText();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            Date date = ParserDateFactory.parser(value);
            if (date != null) {
                return date;
            }
        } catch (Exception e) {

        }
        throw new IllegalArgumentException("解析`" + value + "`到java.util.Date类型错误");
    }
}