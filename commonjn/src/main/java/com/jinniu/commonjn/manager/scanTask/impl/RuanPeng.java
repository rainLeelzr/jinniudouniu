package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.BaseOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractPengScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 扫描是否可以软碰
 */
public class RuanPeng extends AbstractPengScanTask {

    @Override
    public Operate getOperate() {
        return Operate.RUAN_PENG;
    }

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {
        Set<Operate> myOperates = getMyOperates(
                personalCardInfo.getRoomMember().getUserId());
        // 有软大明杠肯定有软碰
        for (Operate myOperate : myOperates) {
            if (myOperate.getBaseOperate() == BaseOperate.RUAN_DA_MING_GANG) {
                return true;
            }
        }

        List<Mahjong> handCards = new ArrayList<>(personalCardInfo.getHandCards());
        List<Mahjong> myBaoMahjongs = getMyBaoMahjongs(handCards);
        // 如果没有宝牌，则不能软大明杠
        if (myBaoMahjongs.size() == 0) {
            return false;
        }

        // 把宝牌全部变成打出的牌，判断玩家手牌有没有两只与putOutMahjong相同的牌
        for (Mahjong myBaoMahjong : myBaoMahjongs) {
            handCards.remove(myBaoMahjong);
            handCards.add(specifiedMahjong);
        }

        int match = 0;
        for (Mahjong mahjong : handCards) {
            if (mahjong.getNumber().equals(specifiedMahjong.getNumber())) {
                match++;
            }
            if (match == 2) {
                return true;
            }
        }
        return false;
    }
}
