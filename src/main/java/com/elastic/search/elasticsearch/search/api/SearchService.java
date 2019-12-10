package com.elastic.search.elasticsearch.search.api;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
public class SearchService {

    @Resource
    private Client client;

    /**
     * 统一索引中数据总数量
     *
     * @param index
     * @param type
     * @return
     */
    public long countAll(String index, String type) {
        SearchResponse response = client.prepareSearch(index).setTypes(type).setSize(1).get();
        return response.getHits().totalHits;
    }

    /**
     * 批量保存
     *
     * @param batchList
     * @return
     */
    public boolean batchSave(List<IndexRequestBuilder> batchList) {
        if (batchList.size() > 0) {
            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
            for (IndexRequestBuilder requestBuilder : batchList) {
                bulkRequestBuilder.add(requestBuilder);
            }
            return !bulkRequestBuilder.get().hasFailures();
        } else {
            return false;
        }
    }
}
