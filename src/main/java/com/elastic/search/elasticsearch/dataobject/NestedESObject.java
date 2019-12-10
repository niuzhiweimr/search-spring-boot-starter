package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <p>
 * 嵌套对象的增删改请求参数（目前支持3级嵌套）
 *
 * @author niuzhiwei
 */
public class NestedESObject implements Serializable {


    private static final long serialVersionUID = -1318856812842748868L;
    /**
     * 结构文档的根域
     */
    private String fieldName;

    /**
     * {@link #fieldName} 是否为集合
     */
    private boolean isList;

    /**
     * 若 {@link #isList} 是集合，
     * 需要修改内嵌对象的id,若为空，则进行全部匹配进行更新
     */
    private String[] idValues;

    /**
     * 下级嵌套说明
     */
    private NestedESObject nextNestedESObject;

    public NestedESObject() {
    }

    /**
     * 判断是否有条件限制
     *
     * @return <code>true</code> 有条件限制；<code>false</code> 无条件限制；
     */
    public boolean hasRestriction() {
        return idValues != null && idValues.length > 0;
    }

    /**
     * 判断是否有下一个
     *
     * @return <code>true</code> 有；<code>false</code> 无；
     */
    public boolean hasNext() {
        return nextNestedESObject != null;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public String[] getIdValues() {
        return idValues;
    }

    public void setIdValues(String[] idValues) {
        this.idValues = idValues;
    }

    public NestedESObject getNextNestedESObject() {
        return nextNestedESObject;
    }

    public void setNextNestedESObject(NestedESObject nextNestedESObject) {
        this.nextNestedESObject = nextNestedESObject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NestedESObject{");
        sb.append("fieldName='").append(fieldName).append('\'');
        sb.append(", isList=").append(isList);
        sb.append(", idValues=").append(Arrays.toString(idValues));
        sb.append(", nextNestedESObject=").append(nextNestedESObject);
        sb.append('}');
        return sb.toString();
    }
}