package com.elastic.search.elasticsearch.dataobject;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 批量新增请求参数
 *
 * @author niuzhiwei
 */
public class BatchSaveESObject implements Serializable {


    private static final long serialVersionUID = 6434788994136588315L;
    private List<SaveESObject> saveDatas;
    /**
     * 设置是否立即刷新到磁盘
     */
    private boolean refresh = true;

    public BatchSaveESObject() {
    }

    public List<SaveESObject> getSaveDatas() {
        return saveDatas;
    }

    public void setSaveDatas(List<SaveESObject> saveDatas) {
        this.saveDatas = saveDatas;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BatchSaveESObject{");
        sb.append("saveDatas=").append(saveDatas);
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
    public BatchSaveESObject setRefresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }
}
