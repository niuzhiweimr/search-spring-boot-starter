package com.elastic.search.common.boot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es 上下文
 *
 * @author niuzhiwei
 */
public class SearchBeanContext implements ApplicationContextAware {

    /**
     * 上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 默认构造方法，注入上下文,
     * implements ApplicationContextAware 自动调用
     *
     * @param applicationContext
     * @return
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SearchBeanContext.applicationContext = applicationContext;
    }

    /**
     * 获取 bean
     */
    public static <T> T getBean(Class<T> clazz) {
        T obj;
        try {
            //从上下文获取 bean
            obj = applicationContext.getBean(clazz);
        } catch (Exception e) {
            obj = null;
        }
        //返回 bean
        return obj;
    }


    /**
     * 获取 bean 的类型
     */
    public static <T> List<T> getBeansOfType(Class<T> clazz) {
        //声明一个结果
        Map<String, T> map;
        try {
            //获取类型
            map = applicationContext.getBeansOfType(clazz);
        } catch (Exception e) {
            map = null;
        }
        //返回 bean 的类型
        return map == null ? null : new ArrayList<>(map.values());
    }


    /**
     * 获取所有被注解的 bean
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        Map<String, Object> map;
        try {
            //获取注解的 bean
            map = applicationContext.getBeansWithAnnotation(annotation);
        } catch (Exception e) {
            map = null;
        }
        return map;
    }


}
