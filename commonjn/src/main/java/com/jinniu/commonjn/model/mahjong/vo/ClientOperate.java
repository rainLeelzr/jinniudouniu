package com.jinniu.commonjn.model.mahjong.vo;


import java.util.List;

/**
 * 别人打一张牌时，自己可以的操作
 */
public class ClientOperate {

    // 需要接收本对象的玩家uId
    private Integer uId;

    /**
     * 别人打一张牌后可以的操作，如吃胡、大明杠、碰，包含过
     */
    private List<Integer> operatePids;

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

    /**
     * 别人打出的麻将
     */
    private Integer playedMahjongId;

    /**
     * 打出麻将的人的uid
     */
    private Integer playerUId;

    public Integer getPlayerUId() {
        return playerUId;
    }

    public void setPlayerUId(Integer playerUId) {
        this.playerUId = playerUId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public List<Integer> getOperatePids() {
        return operatePids;
    }

    public void setOperatePids(List<Integer> operatePids) {
        this.operatePids = operatePids;
    }

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

    public Integer getPlayedMahjongId() {
        return playedMahjongId;
    }

    public void setPlayedMahjongId(Integer playedMahjongId) {
        this.playedMahjongId = playedMahjongId;
    }
}
