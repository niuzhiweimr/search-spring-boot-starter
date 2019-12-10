package com.elastic.search.elasticsearch.search.utils;


import com.elastic.search.elasticsearch.search.index.ScanDocuments;
import com.elastic.search.elasticsearch.config.IndexUtils;
import com.elastic.search.elasticsearch.search.api.SearchService;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author niuzhiwei
 */
public class ModifyIndexFactory {

    @Resource
    private SearchService searchService;

    @Resource
    private Client client;

    /**
     * <b>重建索引处理</b>
     * <ul>
     * <li>将变更后的数据结构创建一个临时索引</li>
     * <li>将当前索引中的数据迁移到临时索引</li>
     * <li>重建当前索引(删除+重建索引)</li>
     * <li>将临时索引中的数据迁移到当前索引</li>
     * <li>删除临时索引</li>
     * </ul>
     *
     * @param index            索引名称
     * @param type             类型
     * @param updateProperties 更新属性处理
     * @param sortName         排序字段
     * @param order            排序方式
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean reindex(String index, String type, UpdateProperties updateProperties, String sortName, SortOrder order)
            throws IOException, InterruptedException, ExecutionException {
        String tmp_index = index + "_tmp";
        MappingMetaData metaData = IndexUtils.loadIndexMeta(client, index, type);
        Map<String, Object> data = updateProperties.execute(metaData.getSourceAsMap());
        if (!IndexUtils.createIndex(client, tmp_index, type, data)) {
            throw new IllegalArgumentException("创建临时索引失败");
        }
        //将数据拷贝到临时索引
        copy_data(index, tmp_index, type, sortName, order);
        //删除主索引
        IndexUtils.deleteIndex(client, index);
        //重建主索引
        if (!IndexUtils.createIndex(client, index, type, data)) {
            throw new IllegalArgumentException("重建主索引失败");
        }
        // 从临时索引中拷贝到主索引中
        copy_data(tmp_index, index, type, sortName, order);
        // 删除临时索引
        IndexUtils.deleteIndex(client, tmp_index);
        return true;
    }

    /**
     * 同一个ES服务中拷贝数据
     *
     * @param srcIndex  原索引
     * @param dectIndex 目标索引
     * @param type      索引类型
     * @param sortName
     */
    private void copy_data(String srcIndex, String dectIndex, String type, String sortName, SortOrder order) {
        int batch = 100;
        ScanDocuments scanDocuments = new ScanDocuments(srcIndex, type, batch);
        scanDocuments.start(client, sortName, order);
        int count = 0;
        List<IndexRequestBuilder> batchList = new ArrayList<>(batch);
        while (scanDocuments.hasNext()) {
            SearchHit hit = scanDocuments.next();
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(dectIndex, type).setId(hit.getId())
                    .setSource(hit.getSourceAsMap());
            batchList.add(indexRequestBuilder);
            if (batchList.size() == batch) {
                boolean success = searchService.batchSave(batchList);
                System.out.println("批量提交" + batch + "->endIndex:" + count + " 状态:" + success);
            }
            count++;
        }
        if (batchList.size() > 0) {
            boolean success = searchService.batchSave(batchList);
            System.out.println("批量提交" + batch + "->endIndex:" + count + " 状态:" + success);
        }
        System.out.println("迁移数据:" + count);
    }

    public static interface UpdateProperties {
        /**
         * 修改索引数据结构(属性结构为索引中的原始格式)
         *
         * <pre>
         * public default Map<String, Object> process(Map<String, Object> properties) {
         * 	Map<String, Object> weight = (Map<String, Object>) properties.get("weight");
         * 	weight.put("type", "integer");
         * 	weight.remove("field");
         * 	return properties;
         * }
         * </pre>
         *
         * @param properties
         */
        public Map<String, Object> adjustField(Map<String, Object> properties);

        @SuppressWarnings("unchecked")
        default Map<String, Object> execute(Map<String, Object> data) {
            Map<String, Object> properties = (Map<String, Object>) data.get("properties");
            properties = this.adjustField(properties);
            data.put("properties", properties);
            return data;
        }
    }
}