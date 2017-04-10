package com.jinniu.commonjn.util;



import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
* @Title: ThreadPool.java
* @Package commons.lander.util
* @Description: 线程池工具类
* @author chenwenhao 
* @date 2016-12-1 下午2:48:53
 */
public class ThreadPool extends ThreadPoolExecutor {
    private String poolName;

    /**
     * 创建线程数固定大小的线程池
     * 
     * @param poolSize
     * @param poolName 线程池的名称必须设置
     */
    public ThreadPool(int poolSize, String poolName) {
        super(poolSize, poolSize, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new NamingThreadFactory(
                poolName));
        this.poolName = poolName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    private static class NamingThreadFactory implements ThreadFactory {
        private String threadName;
        private AtomicInteger counter = new AtomicInteger(1);

        public NamingThreadFactory(String threadName) {
            this.threadName = threadName;
        }

       
        public Thread newThread(Runnable r) {
            int index = counter.getAndIncrement();
            return new Thread(r, threadName + "-" + index);
        }
    }

    public String toString() {
        String str = super.toString();
        int idx = str.indexOf("[");
        if (idx == -1) {
            return "[name = " + poolName + "]";
        }
        String s = str.substring(idx + 1);
        return "[name = " + poolName + ", " + s;
    }
}
