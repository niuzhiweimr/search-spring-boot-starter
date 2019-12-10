package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * es 单文档响应结果
 *
 * @author niuzhiwei
 */
public class ESDocument implements Serializable {
    private static final long serialVersionUID = -9005544083555928843L;
    /**
     * 文档数据内容 或 按指定 Fields 返回
     */
    private Map<Object, Object> dataMap;

    public ESDocument() {
    }

    public ESDocument(Map<Object, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<Object, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Object, Object> dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ESDocument{");
        sb.append("dataMap=").append(dataMap);
        sb.append('}');
        return sb.toString();
    }
}
