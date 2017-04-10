package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractGangScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

/**
 * 扫描是否可以硬大明杠
 */
public class YingDaMingGang extends AbstractGangScanTask {

    @Override
    public Operate getOperate() {
        return Operate.YING_DA_MING_GANG;
    }

    // 是否已经有碰扫描出来有人可以硬杠
    private boolean hasGang = false;

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {
        if (hasGang) {
            return false;
        }

        // 判断玩家手牌有没有三只与putOutMahjong相同的牌
        int match = 0;
        for (Mahjong mahjong : personalCardInfo.getHandCards()) {
            if (mahjong.getNumber().equals(specifiedMahjong.getNumber())) {
                match++;
            }
            if (match == 3) {
                hasGang = true;
                return true;
            }
        }
        return false;
    }
}
