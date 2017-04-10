package com.jinniu.commonjn.util.vo;

import java.util.Date;
import java.util.Map;

/**
 * 用于封装用户房间战绩的vo类
 */
public class ScoreVo {
    private Date createdTime;
    private Integer state;
    private Integer roomCode;
    private Map<String, Integer> players;

    public static final Integer WIN = 1;
    public static final Integer LOSE = 2;

    public ScoreVo() {
    }

    @Override
    public String toString() {
        return "ScoreVo{" +
                "createdTime=" + createdTime +
                ", state=" + state +
                ", roomCode=" + roomCode +
                ", players=" + players +
                '}';
    }

    public ScoreVo(Date createdTime, Integer state, Integer roomCode, Map<String, Integer> players) {
        this.createdTime = createdTime;
        this.state = state;
        this.roomCode = roomCode;
        this.players = players;
    }

    public Date getCreatedTime() {

        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(Integer roomCode) {
        this.roomCode = roomCode;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }
}
