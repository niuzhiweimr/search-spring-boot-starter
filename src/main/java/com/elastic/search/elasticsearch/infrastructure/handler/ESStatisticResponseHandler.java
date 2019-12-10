package com.elastic.search.elasticsearch.infrastructure.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class ESStatisticResponseHandler {

    public Map<String, Number> handler(final Aggregations aggregations) {
        Map<String, Number> statisticResult = new HashMap<>();
        if (aggregations == null) {
            return null;
        }

        for (Aggregation aggregation : aggregations.asList()) {
            if (aggregation instanceof NumericMetricsAggregation.SingleValue) {
                final NumericMetricsAggregation.SingleValue numericProperty = (NumericMetricsAggregation
                        .SingleValue) aggregation;
                final String functionName = numericProperty.getName();
                final double value = numericProperty.value();

                statisticResult.put(functionName, value);
            }
        }
        return statisticResult;
    }
}

