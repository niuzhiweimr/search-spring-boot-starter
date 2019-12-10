package com.elastic.search.starter;

import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.utils.EmptyUtils;
import com.elastic.search.elasticsearch.condition.*;
import com.elastic.search.elasticsearch.config.IndexAdmin;
import com.elastic.search.elasticsearch.evo.ESClient;
import com.elastic.search.elasticsearch.groovy.NestedUpdateGroovyScritpBuilder;
import com.elastic.search.elasticsearch.infrastructure.handler.CollapseResponseHandler;
import com.elastic.search.elasticsearch.infrastructure.handler.ESQueryResponseHandler;
import com.elastic.search.elasticsearch.infrastructure.handler.ESStatisticResponseHandler;
import com.elastic.search.elasticsearch.search.api.SearchService;
import com.elastic.search.elasticsearch.search.utils.ModifyIndexFactory;
import com.elastic.search.elasticsearch.searchservice.ESSearchServiceImpl;
import com.elastic.search.elasticsearch.searchservice.ESStatisticServiceImpl;
import com.elastic.search.elasticsearch.service.ESNestedSearchService;
import com.elastic.search.elasticsearch.service.ESStatisticOperatorService;
import com.elastic.search.elasticsearch.validator.*;
import com.elastic.search.listener.SpringBootEnvironmentListener;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author niuzhiwei
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({EtcElasticSearchProperties.class})
public class ElasticSearchStarter {

    @Resource
    private EtcElasticSearchProperties etcElasticSearchProperties;


    /*************************** 初始化ES上下文工具类****************************/

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SearchBeanContext searchBeanContext() {
        return new SearchBeanContext();
    }

    /*************************** 初始化ES启动环境初始化类****************************/

    @Bean
    @ConditionalOnMissingBean(SpringBootEnvironmentListener.class)
    public SpringBootEnvironmentListener springBootAdminListener() {
        return new SpringBootEnvironmentListener();
    }

    /*************************** 初始化ES索引管理****************************/

    @Bean
    public IndexAdmin indexAdmin() {
        return new IndexAdmin();
    }

    @Bean
    public ModifyIndexFactory modifyIndexFactory(){
        return new ModifyIndexFactory();
    }

    /*************************** 初始化ES操作service****************************/

    @Bean
    public ESStatisticOperatorService esStatisticOperatorService() {
        return new ESStatisticOperatorService();
    }

    @Bean
    public ESNestedSearchService esNestedSearchService() {
        return new ESNestedSearchService();
    }

    @Bean
    public ESSearchServiceImpl esSearchService() {
        return new ESSearchServiceImpl();
    }

    @Bean
    public ESStatisticServiceImpl esStatisticService() {
        return new ESStatisticServiceImpl();
    }

    @Bean
    public SearchService searchService() {
        return new SearchService();
    }

    /*************************** 初始化ES嵌套更新脚本类****************************/

    @Bean
    public NestedUpdateGroovyScritpBuilder nestedUpdateGroovyScritpBuilder() {
        return new NestedUpdateGroovyScritpBuilder();
    }

    /*************************** 初始化ES查询构建器****************************/

    @Bean
    public CollapseConditionBuilder collapseConditionBuilder() {
        return new CollapseConditionBuilder();
    }

    @Bean
    public GroupConditionBuilder groupConditionBuilder() {
        return new GroupConditionBuilder();
    }

    @Bean
    public QueryConditionBuilder queryConditionBuilder() {
        return new QueryConditionBuilder();
    }

    @Bean
    public RelationConditionBuilder relationConditionBuilder() {
        return new RelationConditionBuilder();
    }

    @Bean
    public SearchConditionBuilder searchConditionBuilder() {
        return new SearchConditionBuilder();
    }

    @Bean
    public SearchSourceBuilder searchSourceBuilder() {
        return new SearchSourceBuilder();
    }

    /*************************** 初始化ES响应处理类****************************/


    @Bean
    public CollapseResponseHandler collapseResponseHandler() {
        return new CollapseResponseHandler();
    }


    @Bean
    public ESQueryResponseHandler esQueryResponseHandler() {
        return new ESQueryResponseHandler();
    }


    @Bean
    public ESStatisticResponseHandler esStatisticResponseHandler() {
        return new ESStatisticResponseHandler();
    }


    /*************************** 初始化ES参数验证类****************************/

    @Bean
    public BaseUpdateValidator baseUpdateValidator() {
        return new BaseUpdateValidator();
    }

    @Bean
    public BaseValidator baseValidator() {
        return new BaseValidator();
    }

    @Bean
    public CollapseQueryValidator collapseQueryValidator() {
        return new CollapseQueryValidator();
    }

    @Bean
    public ESBatchDeleteValidator esBatchDeleteValidator() {
        return new ESBatchDeleteValidator();
    }

    @Bean
    public ESBatchSaveValidator esBatchSaveValidator() {
        return new ESBatchSaveValidator();
    }

    @Bean
    public ESBatchUpdateValidator esBatchUpdateValidator() {
        return new ESBatchUpdateValidator();
    }

    @Bean
    public ESConditionDeleteValidator esConditionDeleteValidator() {
        return new ESConditionDeleteValidator();
    }

    @Bean
    public ESConditionUpdateValidator esConditionUpdateValidator() {
        return new ESConditionUpdateValidator();
    }

    @Bean
    public ESDeleteValidator esDeleteValidator() {
        return new ESDeleteValidator();
    }

    @Bean
    public ESQueryValidator esQueryValidator() {
        return new ESQueryValidator();
    }

    @Bean
    public ESSaveValidator esSaveValidator() {
        return new ESSaveValidator();
    }

    @Bean
    public ESUpdateValidator esUpdateValidator() {
        return new ESUpdateValidator();
    }

    @Bean
    public SearchConditionValidator searchConditionValidator() {
        return new SearchConditionValidator();
    }

    @Bean
    public StatisticByConditionsValidator statisticByConditionsValidator() {
        return new StatisticByConditionsValidator();
    }


    /*************************** 初始化ES操作客户端****************************/

    @Bean
    @ConditionalOnMissingBean(ESClient.class)
    public ESClient esClient() {
        return new ESClient();
    }

    @Bean
    @ConditionalOnMissingBean(TransportClient.class)
    public TransportClient transportClient() {
        Settings settings = Settings.EMPTY;
        if (!EmptyUtils.isEmpty(etcElasticSearchProperties.getClusterName())) {
            settings = Settings.builder()
                    .put("cluster.name", etcElasticSearchProperties.getClusterName())
                    .build();
        }
        TransportClient transportClient = null;
        try {
            transportClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(etcElasticSearchProperties.getHost())
                            , etcElasticSearchProperties.getPort()));
        } catch (UnknownHostException e) {
            log.error("创建elasticsearch客户端失败");
            throw new BeanInitializationException("创建elasticsearch客户端失败");
        }
        log.info("创建elasticsearch客户端成功");
        return transportClient;
    }

    @Bean
    @ConditionalOnMissingBean(BulkProcessor.class)
    public BulkProcessor bulkProcessor() throws UnknownHostException {

        Settings settings = Settings.EMPTY;
        if (!EmptyUtils.isEmpty(etcElasticSearchProperties.getClusterName())) {
            settings = Settings.builder()
                    .put("cluster.name", etcElasticSearchProperties.getClusterName())
                    .build();
        }

        TransportClient transportClient = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(etcElasticSearchProperties.getHost())
                        , etcElasticSearchProperties.getPort()));
        return BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                log.error("{} data bulk failed,reason :{}", bulkRequest.numberOfActions(), throwable);
            }
            //分批，每10000条请求当成一批请求。默认值为1000
        }).setBulkActions(1000)
                //每次5MB，刷新一次bulk。默认为5m
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                //每5秒一定执行，不管已经队列积累了多少。默认不设置这个值
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                //设置并发请求数，如果是0，那表示只有一个请求就可以被执行，如果为1，则可以积累并被执行。默认为1.
                .setConcurrentRequests(1)
                //这里有个backoff策略，最初等待100ms,然后按照指数增长并重试3次。每当一个或者多个bulk请求失败,并出现EsRejectedExecutionException异常时.就会尝试重试。这个异常表示用于处理请求的可用计算资源太少。如果要禁用这个backoff策略，需要用backoff.nobackoff()。
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }


    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}

