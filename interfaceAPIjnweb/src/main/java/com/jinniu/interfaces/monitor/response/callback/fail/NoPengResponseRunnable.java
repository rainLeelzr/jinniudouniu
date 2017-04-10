package com.jinniu.interfaces.monitor.response.callback.fail;

/**
 * 如果客户端达到超时时间都没有打出“碰牌”或者“过”，则运行此类的run方法
 */
public class NoPengResponseRunnable implements Runnable {

    private String roomId;

    private String userId;

    @Override
    public void run() {

    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
