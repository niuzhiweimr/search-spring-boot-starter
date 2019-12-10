package com.elastic.search.elasticsearch.serialize.api.json.parser;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author niuzhiwei
 */
public class GMTDateTimeType implements ParserDateFactory.DateType {

    @Override
    public boolean matches(String value) {
        if (value != null) {
            if (value.contains("T")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Date parse(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(value);
    }
}