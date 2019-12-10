 # 版本说明
   
    默认spring boot 版本为 2.1.5.RELEASE 下载完成项目后可更改为自己项目使用的spring boot版本  
    默认ElasticSearch 版本为 6.4.2 极大简化ES操作难度 注：ES版本不要升级如果升级版本太高可能部分函数不兼容

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

    
  3 执行ElasticSearch操作  注：以下示例操作并非最全函数可以自己探索 ，「索引配置」是类中的一个静态属性每个示例都写是为了方便理解
  
   (增)例：
        
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
               
    
   (删)例：                                          
   
   1)主键删除
       
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
        
        Boolean execute = new BaseSearchDeleteExecutor<Boolean>() {
                   @Override
                   public ElasticsearchConfig getConfig() {
                       return esConfiguration;
                   }
               }.execute(1000002L);
                       
   2)条件删除
     
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
        
        List<SearchCondition> searchConditions = SearchConditionUtils.start()
                        .addCondition("id", 1000000, ConditionExpressionEnum.EQUAL)
                        .end();
        Boolean execute = new BaseSearchDeleteExecutor<Boolean>() {
                    @Override
                    public ElasticsearchConfig getConfig() {
                        return esConfiguration;
                    }
                }.execute(searchConditions);
        

   (改)例：
   
   1)主键更新：
   
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
        
        EsOrder esOrder = new EsOrder();
        esOrder.setId(1000001L);
        esOrder.setOrderId("123");
        esOrder.setAmount(10.0);
        esOrder.setCategoryId(11);
        esOrder.setProductCode("123");
        esOrder.setStoreName("测试");
        esOrder.setQuantity(1);
        esOrder.setCreateTime(new Date());
              
        Boolean execute = new BaseSearchUpdateExecutor<EsOrder>() {
                  @Override
                  public ElasticsearchConfig getConfig() {
                      return esConfiguration;
                  }
              }.execute(esOrder);                                            
 
   2)条件更新：
       
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
      
        EsOrder esOrder = new EsOrder();
        esOrder.setId(1000001L);
        esOrder.setOrderId("123");
        esOrder.setAmount(10.0);
        esOrder.setCategoryId(11);
        esOrder.setProductCode("123");
        esOrder.setStoreName("测试");
        esOrder.setQuantity(1);
        esOrder.setCreateTime(new Date());
      
        //条件
        List<SearchCondition> searchConditions = SearchConditionUtils.start()
                      .addCondition("id", 1000000, ConditionExpressionEnum.EQUAL)
                      .end();
      
        Boolean execute = new BaseSearchUpdateExecutor<EsOrder>() {
                  @Override
                  public ElasticsearchConfig getConfig() {
                      return esConfiguration;
                  }
              }.execute(esOrder, searchConditions);

   (查)例：
    
   1)普通条件查询
     
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();

        List<SearchCondition> searchConditions = SearchConditionUtils.start()
                        .addCondition("orderId", 123, ConditionExpressionEnum.EQUAL)
                        .addCondition("id", 1000002, ConditionExpressionEnum.EQUAL)
                        .end();
        List<EsOrder> list = new BaseSearchQueryExecutor<EsOrder>() {
                    @Override
                    public ElasticsearchConfig getConfig() {
                        return esConfiguration;
                    }
                }.list(searchConditions);
        
      
   2)分页查询
   
        //索引配置
        private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
        
        List<SearchCondition> searchConditions = SearchConditionUtils.start()
                        .addCondition("orderId", 123, ConditionExpressionEnum.EQUAL)
                        .end();
                        
         List<EsOrder> list = new BaseSearchQueryExecutor<EsOrder>() {
                    @Override
                    public ElasticsearchConfig getConfig() {
                        return esConfiguration;
                    }
                }.list(searchConditions, 0, 2);
   
   3)分页排序查询
      
           //索引配置
           private static ElasticsearchConfig esConfiguration = ElasticsearchConfig.order();
           
           //分页
           PageCondition pageCondition = PageConditionUtils.create(2, 0);
           //排序
           List<OrderCondition> orderConditions = OrderConditionUtils.start().addCondition("createTime", SortEnum.DESC).end();
           //条件
           List<SearchCondition> searchConditions = SearchConditionUtils.start()
                          .addCondition("orderId", 123, ConditionExpressionEnum.EQUAL)
                          .end();
                           
           List<EsOrder> list = new BaseSearchQueryExecutor<EsOrder>() {
                       @Override
                       public ElasticsearchConfig getConfig() {
                           return esConfiguration;
                       }
                   }.list(searchConditions, orderConditions, pageCondition);