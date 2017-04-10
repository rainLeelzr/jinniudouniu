package com.jinniu.interfaces.monitor.clientTouchMahjong.toucher;

import com.jinniu.commonjn.model.mahjong.Mahjong;

/**
 * 正常摸牌
 */
public class CommonToucher extends Toucher {

    @Override
    // 从剩下的牌中抽出最后一张牌
    public Mahjong touch() {
        Mahjong remove = leftCards.remove(leftCards.size() - 1);
        return remove;
    }
}
