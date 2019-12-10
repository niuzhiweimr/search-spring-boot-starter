package com.elastic.search.elasticsearch.serialize.api.json.parser;


import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author niuzhiwei
 */
public class ParserDateFactory {

    private static final Set<DateType> dateTypes = new HashSet<>();
    public static final DateType DEFAULT = new DateTimeType();
    static {
        dateTypes.add(new LongType());
        dateTypes.add(new YmdDateType());
        dateTypes.add(new DateTimeType());
        dateTypes.add(new GMTDateTimeType());
    }

    /**
     * 注册日期类型解析器
     *
     * @param dateType
     * @return
     */
    public static boolean registerDateType(DateType dateType) {
        return dateTypes.add(dateType);
    }

    public static Date parser(String value) throws ParseException {
        Iterator<DateType> iterator = dateTypes.iterator();
        while (iterator.hasNext()) {
            DateType dateType = iterator.next();
            if (dateType.matches(value)) {
                try {
                    Date date = dateType.parse(value);
                    if (date != null) {
                        return date;
                    }
                } catch (Exception e) {
                }
            }
        }
        return DEFAULT.parse(value);
    }

    public static interface DateType {
        /**
         * 验证是否匹配解析格式
         *
         * @param value
         * @return
         */
        public boolean matches(String value);

        /**
         * 解析Value到Date类型
         *
         * @param value
         * @return
         */
        public Date parse(String value) throws ParseException;
    }

}
