package com.elastic.search.elasticsearch.evo;

import org.elasticsearch.client.transport.TransportClient;

import javax.annotation.Resource;

/**
 * @author niuzhiwei
 */
public class ESClient {

    @Resource
    private TransportClient transportClient;

    public ESSearchRequestBuilder prepareSearch(String index, String type) {
        return new ESSearchRequestBuilder(transportClient.prepareSearch(index).setTypes(type));
    }
}
