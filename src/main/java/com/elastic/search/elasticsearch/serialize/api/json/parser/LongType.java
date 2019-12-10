package com.elastic.search.elasticsearch.serialize.api.json.parser;


import java.util.Date;

/**
 * @author niuzhiwei
 */
public class LongType implements ParserDateFactory.DateType {
    @Override
    public boolean matches(String value) {
        if (value != null) {
            try {
                Long.parseLong(value.trim());
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    @Override
    public Date parse(String value) {
        return new Date(Long.parseLong(value.trim()));
    }
}
