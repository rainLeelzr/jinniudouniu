package com.jinniu.interfaces.monitor;

/**
 * 监控任务接口。
 * 监控任务的成功与否，由实现类来实现
 */
public interface MonitorTask extends Runnable {

    /**
     * 获取任务名称
     */
    String getTaskName();

    /**
     * 完成一次监控任务后，是否需要添加到任务监控管理器,等待再次运行此监控任务
     *
     * @return
     */
    //boolean continueWatch();

    /**
     * 监控任务的执行逻辑
     */
    @Override
    void run();

    /**
     * 设置监控任务成功时，执行的回调方法
     */
    void setSuccessCallback(Runnable success);

    /**
     * 设置监控任务失败时，执行的回调方法
     */
    void setFailCallback(Runnable fail);

    /**
     * 设置监控任务结束后，不管任务成功与否，都需要执行的回调方法
     */
    void setFinishCallback(Runnable finish);
}
