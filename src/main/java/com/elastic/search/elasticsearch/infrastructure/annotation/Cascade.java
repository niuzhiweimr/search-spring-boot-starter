package com.elastic.search.elasticsearch.infrastructure.annotation;

import java.lang.annotation.*;


/**
 * 关联模糊查询需要用到此注解
 *
 * @author niuzhiwei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(Cascades.class)
public @interface Cascade {
    /**
     * 关联字段名称
     *
     * @return
     * @Author
     */
    String value() default "";

    /**
     * 关联字段名称
     *
     * @return
     * @Author
     */
    String[] fields() default {};
}

