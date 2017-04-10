package com.jinniu.commonjn.model.mahjong.vo;

import java.util.List;

/**
 * 用于玩家打牌广播
 */
public class PlayedMahjong {

    private Integer leftCardCount; //剩下可以摸牌的数量
    private Integer playedMahjongId;// 打出的麻将id
    private Integer playedUId;  // 打牌的玩家uid
    private Integer uId; //需要接受广播消息的玩家uid

    /**
     * 个人的手牌id
     */
    private List<Integer> handCardIds;

    /**
     * 碰了的牌
     */
    private List<List<Integer>> pengMahjongIs;

    /**
     * 杠了的牌
     */
    private List<GangVo> gangs;

    private Long version;

    public List<Integer> getHandCardIds() {
        return handCardIds;
    }

    public void setHandCardIds(List<Integer> handCardIds) {
        this.handCardIds = handCardIds;
    }

    public List<List<Integer>> getPengMahjongIs() {
        return pengMahjongIs;
    }

    public void setPengMahjongIs(List<List<Integer>> pengMahjongIs) {
        this.pengMahjongIs = pengMahjongIs;
    }

    public List<GangVo> getGangs() {
        return gangs;
    }

    public void setGangs(List<GangVo> gangs) {
        this.gangs = gangs;
    }

    public Integer getLeftCardCount() {
        return leftCardCount;
    }

    public void setLeftCardCount(Integer leftCardCount) {
        this.leftCardCount = leftCardCount;
    }

    public Integer getPlayedMahjongId() {
        return playedMahjongId;
    }

    public void setPlayedMahjongId(Integer playedMahjongId) {
        this.playedMahjongId = playedMahjongId;
    }

    public Integer getPlayedUId() {
        return playedUId;
    }

    public void setPlayedUId(Integer playedUId) {
        this.playedUId = playedUId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}