package com.elastic.search.starter;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ES配置
 *
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "search.es")
public class EtcElasticSearchProperties {

    /**
     * es地址
     */
    private String host;

    /**
     * es端口
     */
    private Integer port;

    /**
     * 集群名称
     */
    private String clusterName;
}
