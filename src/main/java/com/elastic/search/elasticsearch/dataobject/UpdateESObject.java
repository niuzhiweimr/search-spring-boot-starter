package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.enums.OperateTypeEnum;

import java.io.Serializable;

/**
 * <p>
 * es 更新请求参数
 *
 * @author niuzhiwei
 */
public class UpdateESObject extends SaveESObject implements Serializable {

    private static final long serialVersionUID = -6759532047078992621L;

    /**
     * 若要实现数据一致，请指定，要更新文档的版本 当es中文档版本与指定版本一致时更新成功，否则失败；
     */
    private int docVersion;

    /**
     * nested数据类型更新
     */
    private NestedESObject nestedESObject;

    /**
     * 嵌套数据操作类型
     */
    private OperateTypeEnum nestedOperateType;

    public UpdateESObject() {
    }

    public UpdateESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    /**
     * 更新类型
     *
     * @return
     */
    public boolean nestedUpdate() {
        return nestedESObject != null;
    }

    public int getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(int docVersion) {
        this.docVersion = docVersion;
    }

    public NestedESObject getNestedESObject() {
        return nestedESObject;
    }

    public void setNestedESObject(NestedESObject nestedESObject) {
        this.nestedESObject = nestedESObject;
    }

    public OperateTypeEnum getNestedOperateType() {
        return nestedOperateType;
    }

    public void setNestedOperateType(OperateTypeEnum nestedOperateType) {
        this.nestedOperateType = nestedOperateType;
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UpdateESObject{");
        sb.append(super.toString());
        sb.append(", docVersion=").append(docVersion);
        sb.append(", nestedESObject=").append(nestedESObject);
        sb.append(", nestedOperateType=").append(nestedOperateType);
        sb.append('}');
        return sb.toString();
    }
}
