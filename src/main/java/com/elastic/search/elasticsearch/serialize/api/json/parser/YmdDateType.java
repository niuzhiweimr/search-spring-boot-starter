package com.elastic.search.elasticsearch.serialize.api.json.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author niuzhiwei
 */
public class YmdDateType implements ParserDateFactory.DateType {

    @Override
    public boolean matches(String value) {
        if (value != null) {
            if (value.length() == "yyyy-MM-dd".length()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Date parse(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(value);
    }
}