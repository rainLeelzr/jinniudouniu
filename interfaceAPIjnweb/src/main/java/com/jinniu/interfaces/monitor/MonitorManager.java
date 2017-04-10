package com.jinniu.interfaces.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务监控管理器
 */
@Component
public class MonitorManager {

    private static final Logger log = LoggerFactory.getLogger(MonitorManager.class);

    private static final ExecutorService executor = Executors.newFixedThreadPool(20);

    public void watch(MonitorTask monitorTask) {
        if (monitorTask == null) {
            throw new IllegalArgumentException("monitorTask不能为null");
        }

        executor.submit(monitorTask);
    }
}
