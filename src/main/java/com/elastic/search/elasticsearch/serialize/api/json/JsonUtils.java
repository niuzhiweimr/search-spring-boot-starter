package com.elastic.search.elasticsearch.serialize.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.elastic.search.common.exception.ErrorCode;
import com.elastic.search.common.exception.OpenSystemException;

import java.io.IOException;
import java.util.*;

public class JsonUtils {
    public static boolean isNull(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull() || jsonNode.isMissingNode()) {
            return true;
        }
        return false;
    }

    public static String toString(Object value) {
        if (value != null) {
            if (JsonNode.class.isAssignableFrom(value.getClass())) {
                JsonNode node = (JsonNode) value;
                if (node.isArray() || node.isObject()) {
                    return node.toString();
                } else {
                    return node.asText();
                }
            } else {
                return value.toString();
            }
        }
        return null;
    }

    public static String toString(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isValueNode()) {
            if (node.isBoolean()) {
                return String.valueOf(node.asBoolean());
            } else if (node.isBigInteger()) {
                return String.valueOf(node.bigIntegerValue());
            } else if (node.isDouble()) {
                return String.valueOf(node.asDouble());
            } else if (node.isInt()) {
                return String.valueOf(node.intValue());
            } else if (node.isLong()) {
                return String.valueOf(node.asLong());
            } else if (node.isShort()) {
                return String.valueOf(node.shortValue());
            } else {
                return node.asText();
            }
        }
        return node.toString();
    }

    public static Object parserValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isArray()) {
            return parserArrayNode((ArrayNode) node);
        } else if (node.isObject()) {
            return parserMap((ObjectNode) node);
        } else {
            if (node.isBigDecimal() || node.isBigInteger() || node.isLong()) {
                return node.asLong();
            } else if (node.isFloat() || node.isDouble()) {
                return node.asDouble();
            } else if (node.isInt() || node.isNumber() || node.isShort()) {
                return node.asInt();
            } else if (node.isBoolean()) {
                return node.asBoolean();
            } else if (node.isTextual()) {
                return node.asText();
            } else {// 其他类型
                return node.textValue();
            }
        }
    }

    public static Map<String, Object> parserMap(ObjectNode node) {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> iterable = node.fieldNames();
        while (iterable.hasNext()) {
            String key = iterable.next();
            JsonNode jsonNode = node.get(key);
            if (jsonNode.isValueNode()) {
                map.put(key, parserValue(jsonNode));
            } else if (jsonNode.isArray()) {
                map.put(key, parserArrayNode((ArrayNode) jsonNode));
            } else if (jsonNode.isObject()) {
                map.put(key, parserMap((ObjectNode) jsonNode));
            }
        }
        return map;
    }

    public static List<Object> parserArrayNode(ArrayNode arrayNode) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode n = arrayNode.get(i);
            if (n.isValueNode()) {
                list.add(parserValue(n));
            } else if (n.isObject()) {
                list.add(parserMap((ObjectNode) n));
            }
        }
        return list;
    }

//	public static final ObjectMapper mapper = new ObjectMapper();

//	static {
//		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		// 设置有属性不能映射成PO时不报错
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//	}

    public static final ObjectMapper mapper = JacksonSerialize.mapper;

    public static JsonNode parser(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new OpenSystemException(ErrorCode.JSON_SERIALIZE_FAIL).setCause(e);
        }
    }

    /**
     * 将Java对象转化到JsonNode
     */
    public static JsonNode convert(Object value) throws JsonProcessingException, IOException {
        if (value == null) {
            return null;
        }
        if (JsonNode.class.isAssignableFrom(value.getClass())) {
            return (JsonNode) value;
        }
        String valueStr = stringify(value);
        return JsonUtils.parser(valueStr);
    }

    public static String stringify(Object pojo) {
        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            throw new OpenSystemException(ErrorCode.JSON_SERIALIZE_FAIL).setCause(e);
        }
    }

    public static <T> T parse(String json, Class<T> a) {
        T result = null;
        try {
            result = mapper.readValue(json, a);
        } catch (Exception e) {
            throw new OpenSystemException(ErrorCode.JSON_SERIALIZE_FAIL).setCause(e);
        }
        return result;
    }

    public static <T> T parse(String json, TypeReference<T> a) {
        T result = null;
        try {
            result = mapper.readValue(json, a);
        } catch (Exception e) {
            throw new OpenSystemException(ErrorCode.JSON_SERIALIZE_FAIL).setCause(e);
        }
        return result;

    }

    public static void main(String[] args) {
        System.out.println(stringify(null));
    }

}
