package com.jinniu.commonjn.model.mahjong.vo;


import java.util.List;

/**
 * 游戏开始第一次发牌广播
 */
public class FirstPutOutCard {

    // 需要接收本对象的玩家uId,先设置为userId，在api层转换为uId
    private Integer uId;

    //个人的手牌id
    private List<Integer> handCardIds;

    /**
     * 剩下可以被摸牌的麻将的数量
     */
    private Integer leftCardCount;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public List<Integer> getHandCardIds() {
        return handCardIds;
    }

    public void setHandCardIds(List<Integer> handCardIds) {
        this.handCardIds = handCardIds;
    }

    public Integer getLeftCardCount() {
        return leftCardCount;
    }

    public void setLeftCardCount(Integer leftCardCount) {
        this.leftCardCount = leftCardCount;
    }
}
