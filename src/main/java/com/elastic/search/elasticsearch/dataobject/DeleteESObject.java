package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * es 删除请求参数
 *
 * @author niuzhiwei
 */
public class DeleteESObject extends BaseESObject implements Serializable {
    private static final long serialVersionUID = 7438197271883469477L;

    /**
     * 文档唯一标志
     */
    private Map<Object, Object> ukMap;

    /**
     * 嵌套文档删除
     */
    private Map<Object, Object> nestedMap;
    /**
     * 设置是否立即刷新到磁盘
     */
    private boolean refresh = true;

    public DeleteESObject() {
    }

    public DeleteESObject(String systemName, String indexName, String typeName) {
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
        this.ukMap = ukMap;
    }

    public void setId(Object id) {
        this.setUkMap(Collections.singletonMap("id", id));
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeleteESObject{");
        sb.append(super.toString());
        sb.append("ukMap=").append(ukMap);
        sb.append('}');
        return sb.toString();
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}

