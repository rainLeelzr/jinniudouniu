package com.jinniu.interfaces.monitor.clientTouchMahjong.toucher;

import com.jinniu.commonjn.model.mahjong.Mahjong;

/**
 * 杠摸牌
 */
public class GangToucher extends Toucher {

    @Override
    // 从剩下的牌中抽出第一张牌
    public Mahjong touch() {
        if(leftCards.size() == 0){
            return null;
        }
        Mahjong remove = leftCards.remove(0);
        return remove;
    }
}
