package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractHuScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 扫描是否吃软七对
 */
public class ChiRuanQiDuiHu extends AbstractHuScanTask {

    @Override
    public Operate getOperate() {
        return Operate.CHI_RUAN_QI_DUI_HU;
    }

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {
        // todome 判断是否已经有碰，是则肯定不是七对

        // todome 判断是否已经有杠，是则肯定不是七对

        List<Mahjong> handCards = new ArrayList<>(personalCardInfo.getHandCards());
        handCards.add(specifiedMahjong);

        List<Mahjong> myBaoMahjongs = getMyBaoMahjongs(handCards);

        // 如果没有宝牌，则不能软七对
        if (myBaoMahjongs.size() == 0) {
            return false;
        }

        // 创建用于笛卡尔的集合
        List<List<Mahjong>> baoMahjongs = new ArrayList<>(myBaoMahjongs.size());
        for (Mahjong myBaoMahjong : myBaoMahjongs) {
            List<Mahjong> m = new ArrayList<>(
                    getMahjongGameData().getBaoMahjongMakeUpMahjongs());
            m.add(myBaoMahjong);
            baoMahjongs.add(m);
        }

        // 用到笛卡尔积
        List<List<Mahjong>> circulateResult = circulate(baoMahjongs);
        for (List<Mahjong> mahjongs : circulateResult) {
            Collections.sort(handCards);
            //log.debug("座位{}进行软七对扫描宝牌变换前：{}",
            //        personalCardInfo.getRoomMember().getSeat(),
            //        handCards
            //);

            for (int i = 0; i < myBaoMahjongs.size(); i++) {
                handCards.remove(myBaoMahjongs.get(i));
                handCards.add(mahjongs.get(i));
            }
            Collections.sort(handCards);
            //log.debug("座位{}进行软七对扫描宝牌变换后：{}",
            //        personalCardInfo.getRoomMember().getSeat(),
            //        handCards
            //);
            if (isQiDui(handCards)) {
                return true;
            }

            for (int i = 0; i < myBaoMahjongs.size(); i++) {
                handCards.remove(mahjongs.get(i));
                handCards.add(myBaoMahjongs.get(i));
            }
        }

        return false;
    }


}
