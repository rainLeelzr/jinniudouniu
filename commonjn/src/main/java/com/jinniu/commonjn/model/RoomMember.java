package com.jinniu.commonjn.model;

public class RoomMember implements Entity {

    private static final long serialVersionUID = 1L;
    /**  */
    protected Integer id;
    /**  */
    protected java.util.Date joinTime;
    /**  */
    protected java.util.Date leaveTime;
    /**  */
    protected Integer roomId;
    /**
     * 玩家的位置,取值1,2,3,4...,不大于房间允许的总人数
     */
    protected Integer seat;
    /**  */
    protected Integer state;
    /**  */
    protected Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.util.Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(java.util.Date joinTime) {
        this.joinTime = joinTime;
    }

    public java.util.Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(java.util.Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{\"RoomMember\":{"
                + "\"id\":\"" + id + "\""
                + ", \"joinTime\":" + joinTime
                + ", \"leaveTime\":" + leaveTime
                + ", \"roomId\":\"" + roomId + "\""
                + ", \"seat\":\"" + seat + "\""
                + ", \"state\":\"" + state + "\""
                + ", \"userId\":\"" + userId + "\""
                + "}}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RoomMember other = (RoomMember) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * 玩家加入房间后的状态
     */
    public static enum state {
        UNREADY(1, "待准备"),
        READY(2, "已准备"),
        PLAYING(3, "游戏中"),
        OUT_ROOM(4, "退出房间"),;

        private int code;

        private String name;

        state(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}