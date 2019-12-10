package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;

/**
 * <p>
 * 访问 es 必需参数
 *
 * @author niuzhiwei
 */
public class BaseESObject implements Serializable {

    private static final long serialVersionUID = -8766919218680214602L;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 索引
     */
    private String indexName;

    /**
     * 类型
     */
    private String typeName;

    BaseESObject() {
    }

    BaseESObject(String systemName, String indexName, String typeName) {
        this.systemName = systemName;
        this.indexName = indexName;
        this.typeName = typeName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseESObject{");
        sb.append("systemName='").append(systemName).append('\'');
        sb.append(", indexName='").append(indexName).append('\'');
        sb.append(", typeName='").append(typeName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
