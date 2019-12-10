package com.elastic.search.elasticsearch.infrastructure.handler;


import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESDocument;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class CollapseResponseHandler {

    public Map<String, ESResponse> handler(final SearchResponse searchResponse, final CollapseQueryObject esObject) {
        final Map<String, ESResponse> responseMap = new HashMap<>();

        final SearchHits searchHits = searchResponse.getHits();
        if (searchHits == null) {
            return responseMap;
        }

        final SearchHit[] outHits = searchHits.getHits();
        if (outHits == null || outHits.length == 0) {
            return responseMap;
        }

        final List<String> hitNames = esObject.getHitNames();
        if (hitNames == null || hitNames.size() == 0) {
            return responseMap;
        }

        for (final SearchHit outHit : outHits) {
            final Map<String, SearchHits> innerHits = outHit.getInnerHits();
            for (final String hitName : hitNames) {
                final SearchHits innerSearchHits = innerHits.get(hitName);
                if (innerSearchHits == null) {
                    continue;
                }

                final SearchHit[] hitResult = innerSearchHits.getHits();
                if (hitResult == null || hitResult.length == 0) {
                    continue;
                }

                for (final SearchHit searchHitFields : hitResult) {
                    Map dataMap = null;
                    try {
                        dataMap = ESSearchConvertor.json2Object(searchHitFields.getSourceAsString(), Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (dataMap != null && !dataMap.isEmpty()) {
                        ESDocument esDocument = new ESDocument();
                        esDocument.setDataMap(dataMap);
                        ESResponse esResponse = responseMap.get(hitName);
                        if (esResponse == null) {
                            esResponse = new ESResponse();
                            esResponse.setSystemName(esObject.getSystemName());
                            esResponse.setIndexName(esObject.getIndexName());
                            final String typeName = esObject.getTypeName();
                            if (StringUtils.isNotBlank(typeName)) {
                                esResponse.setTypeName(typeName);
                            }
                            responseMap.put(hitName, esResponse);
                        }
                        List<ESDocument> esDocuments = esResponse.getEsDocuments();
                        if (esDocuments == null) {
                            esResponse.setEsDocuments(new ArrayList<>());
                        }
                        esResponse.getEsDocuments().add(esDocument);
                    }
                }
            }
        }
        return responseMap;
    }
}
