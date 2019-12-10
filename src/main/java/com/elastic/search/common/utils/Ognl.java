package com.elastic.search.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class Ognl {

    public static boolean isNotNumber(Object o) throws IllegalArgumentException {
        if (o == null) {
            return false;
        }
        if (o instanceof Number) {
            if (o instanceof Double) {
                if (((Number) o).doubleValue() == 0.0) {
                    return true;
                }
            }
            if (((Number) o).intValue() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * test for Map,Collection,String,Array isEmpty
     *
     * @param o
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o, int flag) throws IllegalArgumentException {
        if (o == null) {
            return true;
        }
        if (o instanceof Number) {
            if (o instanceof Double) {
                if (((Number) o).doubleValue() == 0.0) {
                    return true;
                }
            }
            //0也可以插入到数据表
            if (((Number) o).intValue() == 0 && flag == 0) {
                return false;
            }
            // 查询的时候用
            if (((Number) o).intValue() == 0 && flag == 1) {
                return true;
            }

        }
        if (o instanceof String) {
            if (((String) o).length() == 0) {
                return true;
            }
        } else if (o instanceof Collection) {
            if (((Collection) o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).isEmpty()) {
                return true;
            }
        } else {
            return false;
        }

        return false;
    }

    /**
     * test for Map,Collection,String,Array isNotEmpty
     *
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o, 0);
    }

    public static boolean isNumber(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Number) {
            return true;
        }
        if (o instanceof String) {
            String str = (String) o;
            if (str.length() == 0) {
                return false;
            }
            if (str.trim().length() == 0) {
                return false;
            }
            return StringUtils.isNumeric(str);
        }
        return false;
    }

    public static boolean isBlank(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            String str = (String) o;
            return isBlank(str);
        }
        return false;
    }

    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
