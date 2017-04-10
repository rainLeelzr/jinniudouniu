package com.jinniu.interfaces.monitor.response;

/**
 * 判断客户端是否响应了打出“碰牌”或者“过”
 */
public class PengResponseHelper implements ResponseHelper {

    private String roomId;

    private String userId;

    @Override
    public boolean hasResponse(String version) {

        return false;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
