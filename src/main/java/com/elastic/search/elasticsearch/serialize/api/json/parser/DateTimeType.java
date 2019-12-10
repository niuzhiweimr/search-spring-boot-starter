package com.elastic.search.elasticsearch.serialize.api.json.parser;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author niuzhiwei
 */
public class DateTimeType implements ParserDateFactory.DateType {

    @Override
    public boolean matches(String value) {
        if (value != null) {
            return value.length() >= "yyyy-MM-dd HH:mm:ss".length();
        }
        return false;
    }

    @Override
    public Date parse(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
    }
}
