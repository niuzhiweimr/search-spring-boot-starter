package com.elastic.search.elasticsearch.condition;


import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.conditions.InnerHitsCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.OrderCondition;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 瓦解查询条件构建器
 *
 * @author niuzhiwei
 */
public class CollapseConditionBuilder {

    @Resource
    private SearchSourceBuilder searchSourceBuilder;

    public CollapseBuilder build(final SearchRequestBuilder searchRequestBuilder, CollapseQueryObject esObject) {

        CollapseBuilder collapseBuilder = new CollapseBuilder(esObject.getFieldName());

        final List<InnerHitsCondition> innerHitsConditions = esObject.getInnerHitsConditions();
        List<InnerHitBuilder> innerHits = new ArrayList<>(innerHitsConditions.size());
        for (InnerHitsCondition innerHitsCondition : innerHitsConditions) {
            InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
            innerHitBuilder.setName(innerHitsCondition.getHitName());
            innerHitBuilder.setSize(innerHitsCondition.getHitSize());
            final List<OrderCondition> orderConditions = innerHitsCondition.getOrderConditions();
            if (orderConditions != null && orderConditions.size() > 0) {
                for (OrderCondition orderCondition : orderConditions) {
                    final FieldSortBuilder sort = searchSourceBuilder.getSort(orderCondition);
                    innerHitBuilder.addSort(sort);
                }
            }
            FetchSourceContext fetchSourceContext;
            final List<String> fieldNames = innerHitsCondition.getFieldNames();
            if (fieldNames == null || fieldNames.size() == 0) {
                fetchSourceContext = new FetchSourceContext(Boolean.TRUE, Strings.EMPTY_ARRAY, Strings.EMPTY_ARRAY);
            } else {
                fetchSourceContext = new FetchSourceContext(Boolean.TRUE, fieldNames.toArray(new String[0]),
                        Strings.EMPTY_ARRAY);
            }
            innerHitBuilder.setFetchSourceContext(fetchSourceContext);
            innerHits.add(innerHitBuilder);
        }
        collapseBuilder.setInnerHits(innerHits);
        return collapseBuilder;
    }
}
