package com.elastic.search.elasticsearch.groovy;


import com.elastic.search.elasticsearch.dataobject.NestedESObject;
import com.elastic.search.elasticsearch.dataobject.enums.OperateTypeEnum;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class NestedUpdateGroovyScritpBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NestedUpdateGroovyScritpBuilder.class);

    public Script build(NestedESObject nestedESObject, OperateTypeEnum operateType, Map<Object, Object> dataMap) {
        switch (operateType) {
            case ADD:
                return addTag(nestedESObject, dataMap);
            case UPDATE:
                return updateTag(nestedESObject, dataMap);
            case DELETE:
                return deleteTag(nestedESObject, dataMap);
            default:
                return null;
        }
    }

    /**
     * 新增标签脚本构建器
     *
     * @param nestedESObject 嵌套关系说明
     * @param dataMap        新增对象数据
     * @return add tag groovy script
     */
    private static Script addTag(NestedESObject nestedESObject, Map<Object, Object> dataMap) {
        // 1 构建参数
        Map<String, Object> params = new HashMap<>();
        final Map<String, Object> objectValue = removerPrefix(dataMap);
        List<Map<String, Object>> listValue = new ArrayList<>();
        listValue.add(objectValue);

        params.put("objectValue", objectValue);
        //若为空，直接添加会导致后期添加不进去
        params.put("listValue", listValue);

        StringBuilder sb = new StringBuilder();
        final String fieldName = nestedESObject.getFieldName();
        final NestedESObject nextNestedESObject = nestedESObject.getNextNestedESObject();
        if (nestedESObject.isList()) {
            // 一级 集合嵌套添加
            if (!nestedESObject.hasNext()) {
                sb.append("if(ctx._source.").append(fieldName).append("==null){ctx._source.");
                sb.append(fieldName).append("=params.listValue").append("}else{").append("ctx._source.");
                sb.append(fieldName).append("+=params.objectValue}");
            } else {// 二级 集合嵌套添加
                final String nextfieldName = nextNestedESObject.getFieldName();
                sb.append("ctx._source.").append(fieldName).append(".");
                sb.append("findAll{");
                final String[] idValues = nestedESObject.getIdValues();
                if (nestedESObject.hasRestriction()) {// 有筛选条件
                    sb.append("if(");
                    final int length = idValues.length;
                    for (int i = 0; i < idValues.length; i++) {
                        params.put("id1", idValues[i]);
                        sb.append("it.id==id1");
                        if (i + 1 != length) {
                            sb.append("||");
                        }
                    }
                    sb.append(")");
                    if (nestedESObject.hasNext()) {
                        sb.append("{if(it.").append(nextfieldName).append("==null){it.");
                        sb.append(nextfieldName).append("=params.listValue").append("}else{it.");
                        sb.append(nextfieldName).append("+=params.objectValue}}}");
                    }
                } else {// 没有筛选条件
                    sb.append("if(it.").append(nextfieldName).append("==null){it.");
                    sb.append(nextfieldName).append("=params.listValue").append("}else{it.");
                    sb.append(nextfieldName).append("+=params.objectValue}}");
                }
            }
        } else {// 一级为object 二级为集合
            String nextfieldName = nextNestedESObject.getFieldName();
            sb.append("if(ctx._source.").append(fieldName).append(".").append(nextfieldName);
            sb.append("==null){ctx._source.").append(fieldName).append(".").append(nextfieldName);
            sb.append("=params.listValue").append("}else{").append("ctx._source.").append(fieldName).append(".");
            sb.append(nextfieldName).append(".add(params.objectValue)}");
        }

        LOGGER.debug("nested data type add painless script:[{}],params:{}", sb.toString(), params);
        return new Script(ScriptType.INLINE, "painless", sb.toString(), params);
    }

    /**
     * 删除标签脚本构建器
     *
     * @param nestedESObject 嵌套关系说明
     * @param idMap          待删除对象ID
     * @return delete tag groovy script
     */
    private static Script deleteTag(NestedESObject nestedESObject, Map<Object, Object> idMap) {
        // 1 构建参数
        Map<String, Object> params = removerPrefix(idMap);
        StringBuilder sb = new StringBuilder();
        final String fieldName = nestedESObject.getFieldName();
        final NestedESObject nextNestedESObject = nestedESObject.getNextNestedESObject();

        if (nestedESObject.isList()) {
            if (!nestedESObject.hasNext()) {// 一级 集合嵌套添加
                sb.append("ctx._source.").append(fieldName);
                sb.append(".removeAll{it.id==id}");
            } else {// 二级 集合嵌套添加
                final String nextfieldName = nextNestedESObject.getFieldName();
                sb.append("ctx._source.").append(fieldName).append(".findAll{");
                final String[] idValues = nestedESObject.getIdValues();
                if (nestedESObject.hasRestriction()) {// 有筛选条件
                    sb.append("if(");
                    final int length = idValues.length;
                    for (int i = 0; i < idValues.length; i++) {
                        params.put("id_" + i, idValues[i]);
                        sb.append("it.id==id_" + i);
                        if (i + 1 != length) {
                            sb.append("||");
                        }
                    }
                    sb.append(")");
                    sb.append("{it.").append(nextfieldName).append(".removeAll{it.id==params.id}}}");
                } else {// 没有筛选条件
                    sb.append("it.").append(nextfieldName).append(".removeAll {");
                    sb.append("it.id").append("==params.id").append("}}");
                }
            }
        } else {// 一级为object 二级为集合
            //ctx._source.student.books.removeAll{it.size=id}
            sb.append("ctx._source.").append(fieldName).append(".").append(nextNestedESObject.getFieldName());
            sb.append(".removeAll{it.id==params.id}");
        }

        LOGGER.debug("nested data type delete painless script:[{}],params:{}", sb.toString(), params);
        return new Script(ScriptType.INLINE, "painless", sb.toString(), params);
    }

    private static Script updateTag(NestedESObject nestedESObject, Map<Object, Object> dataMap) {
        // 1 构建参数
        Map<String, Object> updateParamsMap = removerPrefix(dataMap);
        Map<String, Object> params = new HashMap<>();
        params.putAll(updateParamsMap);

        StringBuilder sb = new StringBuilder();
        final String fieldName = nestedESObject.getFieldName();
        final NestedESObject nextNestedESObject = nestedESObject.getNextNestedESObject();
        final String[] idValues = nestedESObject.getIdValues();
        if (nestedESObject.isList()) {
            if (!nestedESObject.hasNext()) {// 一级 集合嵌套添加
                sb.append("ctx._source.").append(fieldName).append(".findAll{");
                sb.append("if(it.id=id){");
                for (Map.Entry<String, Object> stringObjectEntry : updateParamsMap.entrySet()) {
                    final String key = stringObjectEntry.getKey();
                    if (!"id".equals(key)) {
                        sb.append("it.").append(key).append("=");
                        sb.append(key).append(";");
                    }
                }
                sb.deleteCharAt(sb.lastIndexOf(";")).append("}}");
            } else {// 二级 集合嵌套添加
                //ctx._source.quests.findAll {  if(it.qid==qid3) {  it.kps.findAll{  if(it.kid==kid){ it.kname=kname;it
                // .kmd=kmd} } }}
                final String nextfieldName = nextNestedESObject.getFieldName();
                sb.append("ctx._source.").append(fieldName).append(".findAll{");
                if (nestedESObject.hasRestriction()) {// 有筛选条件

                    sb.append("if(");
                    final int length = idValues.length;
                    for (int i = 0; i < idValues.length; i++) {
                        params.put("id_" + i, idValues[i]);
                        sb.append("it.id==params.id_" + i);
                        if (i + 1 != length) {
                            sb.append("||");
                        }
                    }
                    sb.append("){");

                    sb.append("it.").append(nextfieldName).append(".findAll{if(it.id==params.id){");
                    for (Map.Entry<String, Object> stringObjectEntry : updateParamsMap.entrySet()) {
                        sb.append("it.").append(stringObjectEntry.getKey()).append("=params.");
                        sb.append(stringObjectEntry.getKey()).append(";");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(";")).append("}}}}");
                } else {// 没有筛选条件
                    sb.append("it.").append(nextfieldName).append(".findAll{if(it.id==params.id){");
                    for (Map.Entry<String, Object> stringObjectEntry : updateParamsMap.entrySet()) {
                        sb.append("it.").append(stringObjectEntry.getKey()).append("=params.");
                        sb.append(stringObjectEntry.getKey()).append(";");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(";")).append("}}}");
                }
            }
        } else {// 一级为object 二级为集合
            sb.append("ctx._source.").append(fieldName).append(".").append(nextNestedESObject.getFieldName());
            sb.append(".findAll {if(it.id==params.id){");
            for (Map.Entry<String, Object> stringObjectEntry : updateParamsMap.entrySet()) {
                sb.append("it.").append(stringObjectEntry.getKey()).append("=params.");
                sb.append(stringObjectEntry.getKey()).append(";");
            }
            sb.deleteCharAt(sb.lastIndexOf(";")).append("}}");
        }

        LOGGER.info("nested data type update painless script:[{}],params:{}", sb.toString(), params);
        return new Script(ScriptType.INLINE, "painless", sb.toString(), params);

    }

    /**
     * 移除map前缀
     *
     * @param dataMap 待更新数据
     * @return 移除前缀的map
     */
    private static Map<String, Object> removerPrefix(Map<Object, Object> dataMap) {
        Map<String, Object> removePrefix = new HashMap<>();
        for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
            String oldKey = (String) entry.getKey();
            String newKey = oldKey
                    .substring(oldKey.lastIndexOf(".") + 1, oldKey.length());
            removePrefix.put(newKey, entry.getValue());
        }
        return removePrefix;
    }
}
