package com.jinniu.interfaces.monitor.response.task;


import com.jinniu.interfaces.monitor.MonitorManager;
import com.jinniu.interfaces.monitor.response.AbstractResponseTimeoutMonitorTask;
import com.jinniu.interfaces.monitor.response.PengResponseHelper;
import com.jinniu.interfaces.monitor.response.ResponseHelper;
import com.jinniu.interfaces.monitor.response.callback.fail.NoPengResponseRunnable;

/**
 * 客户端需要打出“碰牌”或者“过”的响应监控任务
 */
public class PengCardMonitorTask extends AbstractResponseTimeoutMonitorTask {

    /**
     * 玩家所在房间Id
     */
    private String roomId;

    public static class Builder {

        /**
         * 任务名称
         */
        private String taskName = "碰牌响应";

        /**
         * 客户端需要在多少毫秒内响应
         */
        private int responseTimes = 20 * 1000;

        private PengCardMonitorTask task;

        private PengResponseHelper pengResponseHelper;

        private NoPengResponseRunnable failCallback;

        public Builder() {
            pengResponseHelper = new PengResponseHelper();
            failCallback = new NoPengResponseRunnable();

            task = new PengCardMonitorTask();
            task.taskName = taskName;
            task.responseTimes = responseTimes;
            task.responseHelper = pengResponseHelper;
            task.failCallback = failCallback;
        }

        public Builder setTaskName(String taskName) {
            task.taskName = taskName;
            return this;
        }

        public Builder setResponseTimes(int responseTimes) {
            task.responseTimes = responseTimes;
            task.timeout = responseTimes + System.currentTimeMillis();
            return this;
        }

        public Builder setResponseHelper(ResponseHelper responseHelper) {
            task.responseHelper = responseHelper;
            return this;
        }

        public Builder setRoomId(String roomId) {
            task.roomId = roomId;
            pengResponseHelper.setRoomId(roomId);
            failCallback.setRoomId(roomId);
            return this;
        }

        public Builder setUserId(String userId) {
            task.setUserIds(new String[]{userId});
            pengResponseHelper.setUserId(userId);
            failCallback.setUserId(userId);
            return this;
        }

        public Builder setVersion(String version) {
            task.setVersion(version);
            return this;
        }

        public Builder setSuccess(Runnable success) {
            task.successCallback = success;
            return this;
        }

        public Builder setFailCallback(Runnable failCallback) {
            task.failCallback = failCallback;
            return this;
        }

        public Builder setFinish(Runnable finish) {
            task.finishCallback = finish;
            return this;
        }

        public Builder setMonitorManager(MonitorManager monitorManager) {
            task.monitorManager = monitorManager;
            return this;
        }

        public PengCardMonitorTask build() {
            return task;
        }
    }

}
