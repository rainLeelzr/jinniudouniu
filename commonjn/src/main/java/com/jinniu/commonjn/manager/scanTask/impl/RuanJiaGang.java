package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractGangScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;
import com.jinniu.commonjn.model.mahjong.Combo;
import com.jinniu.commonjn.model.mahjong.YingRuan;

import java.util.List;

/**
 * 扫描是否可以软加杠
 */
public class RuanJiaGang extends AbstractGangScanTask {

    @Override
    public Operate getOperate() {
        return Operate.RUAN_JIA_GANG;
    }

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {

        // 判断玩家有没有已经碰了的牌并且碰牌中有宝牌
        List<Combo> pengs = personalCardInfo.getPengs();
        if (pengs.size() == 0) {
            return false;
        }

        Integer baoMahjongNumber = this.mahjongGameData.getBaoMahjongs().get(0).getNumber();
        for (Combo peng : pengs) {
            if (peng.getYingRuan() == YingRuan.YING) {
                // 硬碰组合，则摸到的必须是宝牌
                if (specifiedMahjong.getNumber().equals(baoMahjongNumber)) {
                    return true;
                }
            } else {
                // 如果摸到的是宝牌，则直接可以软加杠
                if (specifiedMahjong.getNumber().equals(baoMahjongNumber)) {
                    return true;
                } else {
                    // 找到真正碰的麻将（非宝牌麻将）
                    Mahjong pengMahjong = null;
                    for (Mahjong mahjong : peng.getMahjongs()) {
                        if (!mahjong.getNumber().equals(baoMahjongNumber)) {
                            pengMahjong = mahjong;
                            break;
                        }
                    }
                    if (pengMahjong.getNumber().equals(specifiedMahjong.getNumber())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
