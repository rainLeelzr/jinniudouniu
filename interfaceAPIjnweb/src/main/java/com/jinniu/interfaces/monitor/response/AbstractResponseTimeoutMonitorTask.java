package com.jinniu.interfaces.monitor.response;

import com.jinniu.interfaces.monitor.MonitorManager;
import com.jinniu.interfaces.monitor.MonitorTask;

/**
 * 客户端响应超时监控任务
 */
public abstract class AbstractResponseTimeoutMonitorTask implements MonitorTask {

    /**
     * 任务名称
     */
    protected String taskName;

    /**
     * 客户端需要在多少毫秒内响应
     */
    protected int responseTimes;

    /**
     * 需要响应的客户端Id
     */
    protected String[] userIds;

    /**
     * 需要客户端响应的版本号
     */
    protected String version;

    /**
     * 超时时间，时间戳。如果超过了此时间（毫秒），还有客户端未响应，则为超时。在设置responseTimes时，自动计算出此值
     */
    protected long timeout;
    /**
     * 判断客户端是否有响应的方法
     */
    protected ResponseHelper responseHelper;
    /**
     * 监控任务成功时，执行的方法
     */
    protected Runnable successCallback;
    /**
     * 监控任务失败时，执行的方法
     */
    protected Runnable failCallback;
    /**
     * 监控任务结束后，不管任务成功与否，都需要执行的回调方法
     */
    protected Runnable finishCallback;
    /**
     * 任务监控管理器
     */
    protected MonitorManager monitorManager;
    /**
     * 上一次执行此监控任务的时间。由任务执行线程赋值
     */
    private long lastWatchTime;
    /**
     * 同一个监控任务需要重复监控时，两次任务执行的最短间隔时间（毫秒），默认50毫秒
     */
    private long intervalTime = 50;

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return taskName == null ? "" : taskName;
    }

    /**
     * 客户端响应超时时，需要执行的任务
     */
    @Override
    public void run() {
        //如果这个任务在前intervalTime毫秒内执行过，则把此任务放回队列
        long now = System.currentTimeMillis();
        if (this.lastWatchTime + intervalTime > now) {
            monitorManager.watch(this);
            return;
        }
        lastWatchTime = now;

        // 还未到达超时时间，判断客户端是否已响应。
        // 如果已响应，则监控任务成功，并执行success方法和finish方法；
        // 如果未响应，则把此任务放回监控管理器，等待下次监控
        if (now <= timeout) {
            if (responseHelper.hasResponse(version)) {
                success();
                finish();
            } else {
                monitorManager.watch(this);
            }
            return;
        }

        // 如果达到超时时间，则监控任务失败，不需要再判断客户端是否已响应。
        // 并执行fail方法和finish方法
        fail();
        finish();
    }

    private void success() {
        if (successCallback != null) {
            successCallback.run();
        }
    }

    private void fail() {
        if (failCallback != null) {
            failCallback.run();
        }
    }

    private void finish() {
        if (finishCallback != null) {
            finishCallback.run();
        }
    }

    @Override
    public void setSuccessCallback(Runnable successCallback) {
        this.successCallback = successCallback;
    }

    @Override
    public void setFailCallback(Runnable failCallback) {
        this.failCallback = failCallback;
    }

    @Override
    public void setFinishCallback(Runnable finishCallback) {
        this.finishCallback = finishCallback;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}