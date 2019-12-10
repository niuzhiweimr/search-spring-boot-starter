 # 版本说明
   
    默认spring boot 版本为 2.1.5.RELEASE 下载完成项目后可更改为自己项目使用的spring boot版本  
    默认ElasticSearch 版本为 6.4.2 不建议升级ES版本因为有些函数再高版本有改动-注：ES7版本对6版本不兼容

 # 集成说明
                     
1：关于ElasticSearch的配置参数 详见类 { EtcElasticSearchProperties }
  
3：maven导入坐标 ${version}根据自己编译的版本选择导入
 
        <dependency>
           <groupId>com.elastic.search</groupId>
            <artifactId>search-spring-boot-starter</artifactId>
            <version>${version}</version>
        </dependency>
 
 # 使用说明
  
 1 操作函数详见此包 { com.elastic.search.elasticsearch.infrastructure.executor }
  
 2 配置索引名称需要继承 { BaseTypeIndexConfiguration } 注 :所有索引配置必须继承此类 
   ,建议将所有索引多配置到一个类中方便进行管理
   
   例：
     
      public class ElasticsearchConfig extends BaseTypeIndexConfiguration {
    
 
         private ElasticsearchConfig(String indexName, String typeName) {
             this.setIndexName(indexName);
             this.setTypeName(typeName);
         }
     
     
         private static class OrderSearch {
             private static final ElasticsearchConfig CONF = new ElasticsearchConfig(EsConstants.INDEX_OPERATOR_WAYBILL, EsConstants.TYPE_OPERATOR_WAYBILL);
         }
     
         public static ElasticsearchConfig order() {
             return OrderSearch.CONF;
         }
     
     
     }

    
  3 执行ElasticSearch操作 
  
   (增加)例：
        
            索引配置
            private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();

    
             //创建需要存入的对象
             EsOrder esOrder = new EsOrder();
             esOrder.setId(1000003L);
             esOrder.setOrderId("123");
             esOrder.setAmount(10.0);
             esOrder.setCategoryId(11);
             esOrder.setProductCode("123");
             esOrder.setStoreName("测试");
             esOrder.setQuantity(1);
             esOrder.setCreateTime(new Date());
             
              Boolean execute = new BaseSearchSaveExecutor<EsOrder>() {
                         @Override
                         public ElasticsearchConfig getConfig() {
                             return esConfiguration;
                         }
                     }.execute(esOrder);
               
  #TODO: 其他操作后续完善此文档
                                          
