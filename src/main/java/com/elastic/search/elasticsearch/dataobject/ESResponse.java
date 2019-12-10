package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.conditions.PageCondition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ES响应对象
 *
 * @author niuzhiwei
 */
public class ESResponse extends BaseESObject implements Serializable {

    private static final long serialVersionUID = 4936774622586572237L;

    private List<ESDocument> esDocuments;

    /**
     * 根据提供的函数名称进行相关取值操作
     */
    private Map<String, List<Map<String, Object>>> countResult;
    /**
     * 分页条件，当且仅当调用方分页后有值
     */
    private PageCondition pageCondition;

    public ESResponse() {
    }

    public List<ESDocument> getEsDocuments() {
        return esDocuments;
    }

    public void setEsDocuments(List<ESDocument> esDocuments) {
        this.esDocuments = esDocuments;
    }

    public Map<String, List<Map<String, Object>>> getCountResult() {
        return countResult;
    }

    public void setCountResult(Map<String, List<Map<String, Object>>> countResult) {
        this.countResult = countResult;
    }

    public PageCondition getPageCondition() {
        return pageCondition;
    }

    public void setPageCondition(PageCondition pageCondition) {
        this.pageCondition = pageCondition;
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ESResponse{");
        sb.append(super.toString()).append(",");
        sb.append("esDocuments=").append(esDocuments);
        sb.append(", countResult=").append(countResult);
        sb.append(", pageCondition=").append(pageCondition);
        sb.append('}');
        return sb.toString();
    }
}
