package com.elastic.search.elasticsearch.serialize.api.xml;


import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author niuzhiwei
 */
public class XStreamAlias {

    private final Set<Class<?>> CACHE = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(XStreamAlias.class);

    public static XStreamAlias create() {
        return new XStreamAlias();
    }

    private boolean initMap(XStream xStream, Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            initAlias(xStream, entry.getKey());
            initAlias(xStream, entry.getValue());
        }
        return false;
    }

    private boolean initList(XStream xStream, List<?> list) {
        if (list.size() > 0) {
            return initAlias(xStream, list.get(0));
        }
        return false;
    }

    public boolean initAlias(XStream xStream, Object obj) {
        if (obj == null) {
            return false;
        }
        if (Map.class.isAssignableFrom(obj.getClass())) {
            return initMap(xStream, (Map<?, ?>) obj);
        } else if (List.class.isAssignableFrom(obj.getClass())) {
            return initList(xStream, (List<?>) obj);
        } else {
            if (obj.getClass().getPackage().getName().startsWith("com.hivescm")) {
                if (CACHE.contains(obj.getClass())) {
                    return true;
                }
                xStream.alias(obj.getClass().getSimpleName(), obj.getClass());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("alias:{}->{}", obj.getClass().getSimpleName(), obj.getClass());
                }
                boolean res = true;
                Field[] fields = obj.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    try {
                        if (!fields[i].isAccessible()) {
                            fields[i].setAccessible(true);
                        }
                        if (fields[i].getType().isPrimitive()) {
                            continue;
                        }
                        Object object = fields[i].get(obj);
                        if (!initAlias(xStream, object)) {
                            res = false;
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new IllegalArgumentException("error", e);
                    }
                }
                if (res) {
                    CACHE.add(obj.getClass());
                }
                return res;
            }
        }
        return true;
    }
}
