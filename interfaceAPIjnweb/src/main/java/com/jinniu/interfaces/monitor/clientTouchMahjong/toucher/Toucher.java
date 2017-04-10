package com.jinniu.interfaces.monitor.clientTouchMahjong.toucher;

import com.jinniu.commonjn.model.mahjong.Mahjong;

import java.util.List;

/**
 * 正常摸牌
 */
public abstract class Toucher {

    protected List<Mahjong> leftCards;

    public void setLeftCards(List<Mahjong> leftCards) {
        this.leftCards = leftCards;
    }

    // 从剩下的牌中抽出一张牌
    public abstract Mahjong touch();
}
