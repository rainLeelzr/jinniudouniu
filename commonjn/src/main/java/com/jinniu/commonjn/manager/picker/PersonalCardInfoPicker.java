package com.jinniu.commonjn.manager.picker;

import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.List;

/**
 * 从mahjongGameData中提取需要进行判断的PersonalCardInfo
 */
public interface PersonalCardInfoPicker {

    List<PersonalCardInfo> pick(MahjongGameData mahjongGameData, User user);
}
