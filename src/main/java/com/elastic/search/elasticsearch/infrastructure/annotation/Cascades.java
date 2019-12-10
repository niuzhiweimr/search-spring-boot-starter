package com.elastic.search.elasticsearch.infrastructure.annotation;

import java.lang.annotation.*;

/**
 * @author niuzhiwei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cascades {

    Cascade[] value();
}