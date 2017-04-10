package com.jinniu.commonjn.manager.picker;

import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 从mahjongGameData中提取出非打出牌的玩家的PersonalCardInfo
 */
public class PutOutCardPicker implements PersonalCardInfoPicker {
    @Override
    public List<PersonalCardInfo> pick(MahjongGameData mahjongGameData, User user) {
        List<PersonalCardInfo> personalCardInfos = mahjongGameData.getPersonalCardInfos();

        List<PersonalCardInfo> toBeScanCardInfos =
                new ArrayList<>(personalCardInfos.size() - 1);

        // 循环除了出牌的玩家，判断能不能有一些操作
        for (PersonalCardInfo personalCardInfo : personalCardInfos) {
            //log.debug("扫描{}前座位{}的手牌：{}{}",
            //        getBaseOperate().getName(),
            //        personalCardInfo.getRoomMember().getSeat(),
            //        personalCardInfo.getHandCards().size(),
            //        personalCardInfo.getHandCards());

            if (!user.getId().equals(
                    personalCardInfo.getRoomMember().getUserId())) {
                toBeScanCardInfos.add(personalCardInfo);
            }
        }

        return toBeScanCardInfos;
    }
}
