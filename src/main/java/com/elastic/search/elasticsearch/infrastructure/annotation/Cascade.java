package com.elastic.search.elasticsearch.infrastructure.annotation;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(Cascades.class)
public @interface Cascade {
    /**
     * 关联字段名称
     * @return
     * @Author
     */
    String value() default "";
    /**
     * 关联字段名称
     * @return
     * @Author
     */
    String[] fields() default {};
}

