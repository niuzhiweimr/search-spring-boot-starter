package com.elastic.search.common.domain;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * @author niuzhiwei
 */
public class SystemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemManager.class);

    public static volatile boolean isInit = false;
    /**
     * 使用前需要先初始化
     */
    private static volatile Environment environment = null;

    /**
     * 初始化系统环境
     *
     * @param environment
     */
    public static void initEnvironment(Environment environment) {
        if (SystemManager.environment == null) {
            if (environment == null) {
                throw new IllegalArgumentException("初始化环境参数不能为空");
            }
            System.out.println("init.environment:" + environment);
            SystemManager.environment = environment;
        }
    }

    /**
     * 获取系统环境
     *
     * @return
     */
    public static Environment getEnvironment() {
        return environment;
    }


    /**
     * 获取系统码
     *
     * @return
     */
    public static int getSystemCode() {
        if (environment == null) {
            LOGGER.warn("[警告]请先调用初始化：SystemManager.initEnvironment()");
            return 0;
        }
        String code = environment.getProperty(GlobalConstant.SYSTEM_CODE);
        if (code == null) {
            LOGGER.warn("[警告]没有设置系统码 application.properties ->'system.code'");
            return 0;
        }
        return Integer.parseInt(code.trim());
    }

    /**
     * 获取当前环境 dev|test|product
     *
     * @return
     */
    public static String getEnv() {
        String env = null;
        if (environment != null) {
            String[] activeProfiles = environment.getActiveProfiles();
            if (activeProfiles != null && activeProfiles.length > 0) {
                env = activeProfiles[0];
            } else {
                env = environment.getProperty("spring.profiles.active");
            }
        }
        if (StringUtils.isBlank(env)) {
            LOGGER.error("没有获取到有效的环境->spring.profiles.active=[local,dev,test,prepare,product]");
        }
        return env;
    }


    /**
     * 将当前状态码追加系统码
     *
     * @param code
     * @return
     */
    public static int appendSysCode(int code) {
        if (code < 0) {
            return -(getSystemCode() + Math.abs(code));
        }
        return Integer.parseInt(getSystemCode() + "" + code);
    }

    /**
     * 获取当前系统secret
     *
     * @return
     */
    public static String getOpenSecret() {
        String open_secret = environment.getProperty(GlobalConstant.OPEN_SECRET_CONFIG_KEY);
        if (open_secret == null) {
            LOGGER.info("没有获取到配置`{}`", GlobalConstant.OPEN_SECRET_CONFIG_KEY);
        }
        return open_secret;
    }

    public static Logger getLogger() {
        return LOGGER;
    }


    /**
     * 获取端口号
     *
     * @return
     */
    public static Integer getPort() {
        Integer port = null;
        try {
            if (environment != null) {
                String val = environment.getProperty("server.port");
                if (val != null) {
                    port = Integer.parseInt(val.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (port == null) {
            System.err.println("获取应用端口失败检查配置:`server.port`");
        }
        return port;
    }

}
