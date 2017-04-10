package com.jinniu.commonjn.model.mahjong.vo;


import com.jinniu.commonjn.model.mahjong.MahjongGameData;

import java.util.Collections;
import java.util.List;

/**
 * 发一张牌给客户端
 */
public class ClientTouchMahjong {

    // 需要接收本对象的玩家uId
    private Integer uId;

    /**
     * 剩下可以被摸牌的麻将的数量
     */
    private Integer leftCardCount;

    /**
     * 摸牌后可以的操作，如胡、杠、碰，不包含过
     */
    private List<Integer> operatePids;

    /**
     * 个人的手牌id
     */
    private List<Integer> handCardIds;

    /**
     * 碰了的牌
     */
    private List<List<Integer>> pengMahjongIds;

    /**
     * 杠了的牌
     */
    private List<GangVo> gangs;

    /**
     * 摸到的麻将
     */
    private Integer touchMahjongId;

    /**
     * 摸到麻将的人的uid
     */
    private Integer touchMahjongUId;

    /**
     * 消息版本号
     */
    private Long version;

    /**
     * 杠操作时，摸一张牌
     */
    public static ClientTouchMahjong newGangClientTouchMahjong(MahjongGameData mahjongGameData,
                                                               Integer touchMahjongId,
                                                               Integer touchMahjongUId,
                                                               List<Integer> operatePids,
                                                               List<Integer> handCardIds,
                                                               List<List<Integer>> pengMahjongIds,
                                                               List<GangVo> gangs,
                                                               Integer uId
    ) {
        ClientTouchMahjong clientTouchMahjong = new ClientTouchMahjong();
        clientTouchMahjong.setVersion(mahjongGameData.getVersion());
        clientTouchMahjong.setLeftCardCount(mahjongGameData.getLeftCards().size());
        clientTouchMahjong.setuId(uId);
        clientTouchMahjong.setHandCardIds(handCardIds);
        clientTouchMahjong.setTouchMahjongId(touchMahjongId);
        clientTouchMahjong.setTouchMahjongUId(touchMahjongUId);

        clientTouchMahjong.setPengMahjongIds(pengMahjongIds);
        clientTouchMahjong.setGangs(gangs);
        clientTouchMahjong.setOperatePids(operatePids);
        clientTouchMahjong.setOperatePids(Collections.<Integer>emptyList());
        return clientTouchMahjong;
    }

    public List<GangVo> getGangs() {
        return gangs;
    }

    public void setGangs(List<GangVo> gangs) {
        this.gangs = gangs;
    }

    public List<List<Integer>> getPengMahjongIds() {
        return pengMahjongIds;
    }

    public void setPengMahjongIds(List<List<Integer>> pengMahjongIds) {
        this.pengMahjongIds = pengMahjongIds;
    }

    public Integer getTouchMahjongUId() {
        return touchMahjongUId;
    }

    public void setTouchMahjongUId(Integer touchMahjongUId) {
        this.touchMahjongUId = touchMahjongUId;
    }

    public List<Integer> getHandCardIds() {
        return handCardIds;
    }

    public void setHandCardIds(List<Integer> handCardIds) {
        this.handCardIds = handCardIds;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getLeftCardCount() {
        return leftCardCount;
    }

    public void setLeftCardCount(Integer leftCardCount) {
        this.leftCardCount = leftCardCount;
    }

    public List<Integer> getOperatePids() {
        return operatePids;
    }

    public void setOperatePids(List<Integer> operatePids) {
        this.operatePids = operatePids;
    }

    public Integer getTouchMahjongId() {
        return touchMahjongId;
    }

    public void setTouchMahjongId(Integer touchMahjongId) {
        this.touchMahjongId = touchMahjongId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
