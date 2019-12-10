package com.elastic.search.elasticsearch.dataobject.conditions;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 内部击中条件控制，目前用于去重使用
 *
 * @author niuzhiwei
 */
public class InnerHitsCondition implements Serializable {


    private static final long serialVersionUID = -7164665005840381704L;
    /**
     * 此次命中的名字
     */
    private String hitName;

    /**
     * 内部小范围的排序条件
     */
    private List<OrderCondition> orderConditions;

    /**
     * 所需域过滤
     */
    private List<String> fieldNames;

    /**
     * 此次命中需要返回上部文档数目
     */
    private int hitSize = 1;

    public InnerHitsCondition() {
    }

    public String getHitName() {
        return hitName;
    }

    public void setHitName(String hitName) {
        this.hitName = hitName;
    }

    public List<OrderCondition> getOrderConditions() {
        return orderConditions;
    }

    public void setOrderConditions(List<OrderCondition> orderConditions) {
        this.orderConditions = orderConditions;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public int getHitSize() {
        return hitSize;
    }

    public void setHitSize(int hitSize) {
        this.hitSize = hitSize;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InnerHitsCondition{");
        sb.append("hitName='").append(hitName).append('\'');
        sb.append(", orderConditions=").append(orderConditions);
        sb.append(", fieldNames=").append(fieldNames);
        sb.append(", hitSize=").append(hitSize);
        sb.append('}');
        return sb.toString();
    }
}
