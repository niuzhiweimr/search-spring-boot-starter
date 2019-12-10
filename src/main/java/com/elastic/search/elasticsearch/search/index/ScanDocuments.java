package com.elastic.search.elasticsearch.search.index;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author niuzhiwei
 */
public class ScanDocuments implements Iterator<SearchHit> {

    private String index;
    private String type;
    private int size;
    private SearchHits hits;
    private AtomicInteger next_pos = new AtomicInteger(0);
    private int tmp_index = 0;
    private Client esClient;
    long totalHits = 0;
    String sortName;
    SortOrder sortOrder;
    private AtomicBoolean isStart = new AtomicBoolean(false);

    public ScanDocuments(String index, String type, int size) {
        this.index = index;
        this.type = type;
        this.size = size;

    }

    @Override
    public boolean hasNext() {
        if (isStart.get()) {
            if (hits != null && hits.getHits().length > tmp_index) {
                return true;
            }
            execute();
            return hits.getHits().length > 0;
        }
        throw new IllegalArgumentException("请先调用start()");
    }

    @Override
    public SearchHit next() {
        if (hits.getHits().length > tmp_index) {
            try {
                return hits.getAt(tmp_index);
            } finally {
                tmp_index++;
            }
        } else {
            return null;
        }
    }

    public void start(Client esClient, String sortName, SortOrder order) {
        this.esClient = esClient;
        this.sortName = sortName;
        this.sortOrder = order;
        isStart.set(true);
        // SearchResponse response =
        // esClient.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.matchAllQuery())
        // .setSearchType(SearchType.DEFAULT).setScroll(timeValue).setSize(size).get();
        // this.scrollId = response.getScrollId();
    }

    public void execute() {
        SearchResponse response = esClient.prepareSearch(index).setTypes(type).setFrom(next_pos.get())
                .addSort(sortName, sortOrder).setSize(size).get();
        this.hits = response.getHits();
        // 调整下一次加载数据的偏移量
        next_pos.getAndAdd(size);
        // 重置数据索引偏移量
        tmp_index = 0;
    }

}
