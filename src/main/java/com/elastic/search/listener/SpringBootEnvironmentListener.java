package com.elastic.search.listener;

import com.elastic.search.common.domain.SystemManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SpringBoot 环境监听
 *
 * @author SHOUSHEN LUAN
 */
@Slf4j
public class SpringBootEnvironmentListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static AtomicBoolean isInit = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        if (isInit.compareAndSet(false, true)) {
            setManagerConfig(environment);
        }
    }

    private void setManagerConfig(Environment environment) {
        log.info("-----------------spring boot admin--------------------");
        SystemManager.initEnvironment(environment);
        log.info("-----------------over--------------------");
    }

}
