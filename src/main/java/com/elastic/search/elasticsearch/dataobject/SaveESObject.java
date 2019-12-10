package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * es 存储数请求参数
 * <p>
 * 注意： 1、针对valueMap属性，value 为集合类型的实际类型需保持一致，否则会导致es索引失败； 2、es
 * 存储的浮点型数字会有精度缺失问题，建议以long或String存储相关数据；
 *
 * @author niuzhiwei
 */
public class SaveESObject extends BaseESObject implements Serializable {

    private static final long serialVersionUID = 3203853796997438161L;

    /**
     * {@link SaveESObject#dataMap} 唯一标志
     */
    private Map<Object, Object> ukMap;

    /**
     * 文档数据内容
     */
    private Map<Object, Object> dataMap;
    /**
     * 设置是否立即刷新到磁盘
     */
    private boolean refresh = true;

    public SaveESObject() {
    }

    public SaveESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public Map<Object, Object> getUkMap() {
        return ukMap;
    }

    /**
     * 请使用setId
     */
    @Deprecated
    public void setUkMap(Map<Object, Object> ukMap) {
        if (ukMap != null && ukMap.size() == 1) {
            if (ukMap.containsKey("id")) {
                this.ukMap = ukMap;
                return;
            }
        }
        throw new IllegalArgumentException("无效的参数(只支持id属性)，建议统一使用setId(...)方法");
    }

    /**
     * 设置Document主键ID (可以使用业务数据主键ID)
     *
     * @param id
     */
    public void setId(Object id) {
        this.setUkMap(Collections.singletonMap("id", id));
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
        final StringBuilder sb = new StringBuilder("SaveESObject{");
        sb.append(super.toString());
        sb.append(", ukMap=").append(ukMap);
        sb.append(", dataMap=").append(dataMap);
        sb.append('}');
        return sb.toString();
    }

    public boolean isRefresh() {
        return refresh;
    }

    /**
     * 设置是否立即刷新到磁盘
     *
     * @param refresh
     */
    public SaveESObject setRefresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }
}
