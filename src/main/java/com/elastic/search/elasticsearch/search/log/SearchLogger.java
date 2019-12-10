package com.elastic.search.elasticsearch.search.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @author niuzhiwei
 */
@Slf4j
public class SearchLogger {

    public static void error(String msg, Exception ex) {
        log.error(msg, ex);
    }

    public static void log(Object obj) {
        if (log.isDebugEnabled()) {
            log.debug(obj.toString().replace("\n", " ").replace("\t", " "));
        }
    }

    public static void log(String msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg.replace("\n", " ").replace("\t", " "));
        }
    }

}

