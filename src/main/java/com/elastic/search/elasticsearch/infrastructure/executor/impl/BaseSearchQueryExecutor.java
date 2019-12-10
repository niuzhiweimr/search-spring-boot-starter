package com.elastic.search.elasticsearch.infrastructure.executor.impl;

import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.SystemException;
import com.elastic.search.common.utils.ListUtils;
import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.google.common.collect.Lists;
import com.elastic.search.elasticsearch.dataobject.ESDocument;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.QueryESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.GroupByCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.OrderCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.PageCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.enums.ConditionExpressionEnum;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.infrastructure.utils.SearchConditionUtils;
import com.elastic.search.elasticsearch.searchservice.ESSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author niuzhiwei
 */
@Slf4j
public abstract class BaseSearchQueryExecutor<T> extends SearchAdapter<QueryESObject> implements SearchExecutor {

    /**
     * 根据主键查询一条数据<br/>
     * T中包含所有需要查询的数据字段，不需要的字段不能出现在T中
     *
     * @param primaryKey
     * @return
     * @throws SystemException
     */
    public synchronized T get(Long primaryKey) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
            SearchCondition sc = new SearchCondition.Builder()
                    .setFieldName(this.primaryKeyName)
                    .setConditionExpression(ConditionExpressionEnum.EQUAL)
                    .setSingleValue(primaryKey).build();
            searchConditions.add(sc);
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && ListUtils.isNotBlank(response.getEsDocuments())) {
                    ESDocument doc = response.getEsDocuments().get(0);
                    T t = ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass()));
                    return t;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键查询一条数据): primaryKey={" + primaryKey + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据主键查询一条数据<br/>
     * T中包含所有需要查询的数据字段，不需要的字段不能出现在T中
     *
     * @param primaryKey
     * @return
     * @throws SystemException
     */
    public synchronized T get(Long primaryKey, Class<T> clazz) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
            SearchCondition sc = new SearchCondition.Builder()
                    .setFieldName(this.primaryKeyName)
                    .setConditionExpression(ConditionExpressionEnum.EQUAL)
                    .setSingleValue(primaryKey + "").build();
            searchConditions.add(sc);
            obj.setNeedFields(this.getNeedFields(clazz));
            obj.setSearchConditions(searchConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && ListUtils.isNotBlank(response.getEsDocuments())) {
                    ESDocument doc = response.getEsDocuments().get(0);
                    T t = ESSearchConvertor.map2Object(doc.getDataMap(), clazz);
                    return t;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键查询一条数据): primaryKey={" + primaryKey + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据多条件查询一条数据
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized T get(List<SearchCondition> searchConditions) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    ESDocument doc = response.getEsDocuments().get(0);
                    T t = ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass()));
                    return t;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据多条件查询一条数据): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 多条件查询列表
     *
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(多条件查询列表): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 多条件查询列表
     *
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, Class<T> clazz) throws SystemException {
        return list(searchConditions, null, clazz);
    }

    /**
     * 多条件查询列表
     *
     * @return
     * @throws SystemException
     */
    public synchronized List<T> listWithOr(List<SearchCondition>... searchConditionLists) throws SystemException {
        try {
            Class<T> clazz = this.getGenericActualType(this.getClass());
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(clazz));
            if (searchConditionLists.length == 0) {
                obj.setSearchConditions(Lists.newArrayList());
            } else {
                obj.setSearchConditions(searchConditionLists[0]);
                QueryESObject parent = obj;
                for (int i = 1; i < searchConditionLists.length; i++) {
                    QueryESObject queryESObject = new QueryESObject();
                    queryESObject.setSearchConditions(searchConditionLists[i]);
                    parent.setNextGroupQuery(queryESObject);
                    parent = queryESObject;
                }
            }
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = result.getResult();
                if ((response != null && ListUtils.isNotBlank(response.getEsDocuments()))) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = Lists.newArrayList();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), clazz));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(或条件查询列表): searchConditionLists={" + searchConditionLists + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据条件查询列表，带分页
     *
     * @param searchConditions
     * @param currentPage
     * @param pageSize
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, int currentPage, int pageSize) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            PageCondition pageCondition = PageCondition.class.newInstance();
            pageCondition.setCurrentPage(currentPage);
            pageCondition.setPageSize(pageSize);
            obj.setPageCondition(pageCondition);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表，带分页): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据条件查询列表，带分页带排序
     *
     * @param searchConditions
     * @param pageCondition
     * @param pageCondition
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, List<OrderCondition> orderConditions, PageCondition pageCondition) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            obj.setPageCondition(pageCondition);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表，带分页): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }


    /**
     * 根据条件查询列表，不带分页带排序
     *
     * @param searchConditions
     * @param orderConditions
     * @param orderConditions
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, List<OrderCondition> orderConditions) throws SystemException {
        return list(searchConditions, orderConditions, this.getGenericActualType(this.getClass()));
    }

    /**
     * 根据条件查询列表，不带分页带排序
     *
     * @param searchConditions 查询条件
     * @param orderConditions  排序条件
     * @param clazz            类型
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, List<OrderCondition> orderConditions, Class<T> clazz) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(clazz));
            obj.setSearchConditions(searchConditions);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && ListUtils.isNotBlank(response.getEsDocuments())) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), clazz));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表，带分页): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据条件查询列表，带分组排序
     *
     * @param searchConditions
     * @param groupByCondition
     * @param groupByCondition
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(List<SearchCondition> searchConditions, List<OrderCondition> orderConditions, GroupByCondition groupByCondition) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            obj.setSearchConditions(searchConditions);
            obj.setGroupByCondition(groupByCondition);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表，带分组排序): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 关联模糊查询<br/>
     * 需配合@Cascade注解使用
     *
     * @param value
     * @param orderConditions
     * @param pageCondition
     * @return
     * @throws SystemException
     * @Author Mr.HK
     */
    public synchronized List<T> cascade(String name, Object value, List<OrderCondition> orderConditions, PageCondition pageCondition) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            List<SearchCondition> conditions = SearchConditionUtils.start().addCondition(String.format("_%s", name), value, ConditionExpressionEnum.LIKE).end();
            obj.setSearchConditions(conditions);
            obj.setPageCondition(pageCondition);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(关联模糊查询): value={" + value + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 关联模糊查询<br/>
     * 需配合@Cascade注解使用
     *
     * @param value
     * @param orderConditions
     * @param name
     * @return
     * @throws SystemException
     */
    public synchronized List<T> cascade(String name, Object value, List<OrderCondition> orderConditions) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            List<SearchCondition> conditions = SearchConditionUtils.start().addCondition(String.format("_%s", name), value, ConditionExpressionEnum.LIKE).end();
            obj.setSearchConditions(conditions);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(关联模糊查询): value={" + value + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 关联模糊查询<br/>
     * 需配合@Cascade注解使用
     *
     * @param value
     * @param name
     * @param name
     * @return
     * @throws SystemException
     */
    public synchronized List<T> cascade(String name, Object value) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            List<SearchCondition> conditions = SearchConditionUtils.start().addCondition(String.format("_%s", name), value, ConditionExpressionEnum.LIKE).end();
            obj.setSearchConditions(conditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(关联模糊查询): value={" + value + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 关联模糊查询<br/>
     * 需配合@Cascade注解使用
     *
     * @param value
     * @param orderConditions
     * @param pageCondition
     * @return
     * @throws SystemException
     */
    public synchronized List<T> cascade(String name, Object value, List<SearchCondition> searchConditions, List<OrderCondition> orderConditions, PageCondition pageCondition) throws SystemException {
        try {
            QueryESObject obj = this.setConfig(this.getConfig(), QueryESObject.class.newInstance());
            obj.setNeedFields(this.getNeedFields(this.getGenericActualType(this.getClass())));
            SearchCondition condition = SearchConditionUtils.newLikeCondition(String.format("_%s", name), value);
            searchConditions.add(condition);
            obj.setSearchConditions(searchConditions);
            obj.setPageCondition(pageCondition);
            obj.setOrderConditions(orderConditions);
            SearchBaseResult<ESResponse> result = SearchBeanContext.getBean(ESSearchService.class).esQuery(obj);
            if (result.isSuccess()) {
                ESResponse response = null;
                if ((response = result.getResult()) != null && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                    List<ESDocument> docs = response.getEsDocuments();
                    List<T> list = new ArrayList<T>();
                    for (ESDocument doc : docs) {
                        list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                    }
                    return list;
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(关联模糊查询): value={" + value + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }
}

