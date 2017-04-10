package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractGangScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;
import com.jinniu.commonjn.model.mahjong.Combo;

import java.util.List;

/**
 * 扫描是否可以硬加杠
 */
public class YingJiaGang extends AbstractGangScanTask {

    @Override
    public Operate getOperate() {
        return Operate.YING_JIA_GANG;
    }

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {

        // 判断玩家有没有已经碰了的牌
        List<Combo> pengs = personalCardInfo.getPengs();
        if (pengs.size() == 0) {
            return false;
        }

        Integer baoMahjongNumber = mahjongGameData.getBaoMahjongs().get(0).getNumber();

        // 考虑宝牌归位的情况
        if (baoMahjongNumber.equals(specifiedMahjong.getNumber())) {
            // 如果摸到的是宝牌，则已碰列表中是归位碰才能硬加杠
            int pengBaoMahjongCount;
            for (Combo peng : pengs) {
                pengBaoMahjongCount = 0;
                for (Mahjong mahjong : peng.getMahjongs()) {
                    if (mahjong.getNumber().equals(baoMahjongNumber)) {
                        pengBaoMahjongCount++;
                    }
                }
                if (pengBaoMahjongCount == peng.getMahjongs().size()) {
                    return true;
                }
            }
            return false;
        } else {
            // 如果有宝牌，或则不是摸到的麻将，则不是硬加杠
            boolean match = true;// 某个Combo是否硬加杠
            for (Combo peng : pengs) {
                for (Mahjong mahjong : peng.getMahjongs()) {
                    if (mahjong.getNumber().equals(baoMahjongNumber)) {
                        match = false;
                        break;
                    } else if (!mahjong.getNumber().equals(specifiedMahjong.getNumber())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;
                }
            }
            // 循环pengs结束，都没有match=true，即没有硬加杠
            return false;
        }
    }

}
