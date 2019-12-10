package com.elastic.search.common.utils;

import java.util.List;

/**
 * @author niuzhiwei
 */
public class ListUtils {

    public ListUtils() {
    }

    public static synchronized Boolean isBlank(List<?> list) {
        return null == list || list.size() == 0;
    }

    public static synchronized Boolean isNotBlank(List<?> list) {
        return null != list && list.size() != 0;
    }

}
