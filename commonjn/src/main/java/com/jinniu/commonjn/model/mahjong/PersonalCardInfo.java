package com.jinniu.commonjn.model.mahjong;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.util.CommonError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单个玩家拥有的牌信息
 */
public class PersonalCardInfo {

    /**
     * 此副手牌对应的roomMember对象
     */
    private RoomMember roomMember;

    /**
     * 手牌枚举对象
     */
    private Set<Mahjong> handCards;


    /**
     * 客户端可以选择的操作，例如胡、碰、杠，不包含过
     */
    private List<Operate> operates;

    /**
     * 在剩下的牌中，摸一张牌
     */
    private Mahjong touchMahjong;

    /**
     * 已经碰的牌
     */
    private List<Combo> pengs;

    /**
     * 已经杠的牌
     */
    private List<Combo> gangs;

    /**
     * 获取指定user的PersonalCardInfo
     */
    public static PersonalCardInfo getPersonalCardInfo(List<PersonalCardInfo> personalCardInfos, User user) {
        for (PersonalCardInfo personalCardInfo : personalCardInfos) {
            if (user.getId().equals(personalCardInfo.getRoomMember().getUserId())) {
                return personalCardInfo;
            }
        }
        throw CommonError.REDIS_GAME_DATA_ERROR.newException();
    }

    /**
     * 获取指定userId的PersonalCardInfo
     */
    public static PersonalCardInfo getPersonalCardInfo(List<PersonalCardInfo> personalCardInfos, Integer userId) {
        for (PersonalCardInfo personalCardInfo : personalCardInfos) {
            if (userId.equals(personalCardInfo.getRoomMember().getUserId())) {
                return personalCardInfo;
            }
        }
        throw CommonError.REDIS_GAME_DATA_ERROR.newException();
    }

    /**
     * 判断玩家有没有指定的麻将,包括刚摸上的麻将
     *
     * @param personalCardInfo 玩家个人卡信息
     * @param toBeGangMahjongs 判断是否含有此麻将
     */
    public static boolean hasMahjongsWithTouchMahjong(PersonalCardInfo personalCardInfo, List<Mahjong> toBeGangMahjongs) {
        Set<Mahjong> handCards = new HashSet<>(personalCardInfo.getHandCards());
        if (personalCardInfo.getTouchMahjong() != null) {
            handCards.add(personalCardInfo.getTouchMahjong());
        }
        return handCards.containsAll(toBeGangMahjongs);
    }

    public List<Combo> getPengs() {
        return pengs;
    }

    public void setPengs(List<Combo> pengs) {
        this.pengs = pengs;
    }

    public List<Combo> getGangs() {
        return gangs;
    }

    public void setGangs(List<Combo> gangs) {
        this.gangs = gangs;
    }

    public List<Operate> getOperates() {
        return operates;
    }

    public void setOperates(List<Operate> operates) {
        this.operates = operates;
    }

    public Mahjong getTouchMahjong() {
        return touchMahjong;
    }

    public void setTouchMahjong(Mahjong touchMahjong) {
        this.touchMahjong = touchMahjong;
    }

    @Override
    public String toString() {
        return "{\"PersonalCardInfo\":{"
                + "\"roomMember\":" + roomMember
                + ", \"handCards\":" + handCards
                + ", \"touchMahjong\":" + touchMahjong + ""
                + "}}";
    }

    public RoomMember getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(RoomMember roomMember) {
        this.roomMember = roomMember;
    }

    public Set<Mahjong> getHandCards() {
        return handCards;
    }

    public void setHandCards(Set<Mahjong> handCards) {
        this.handCards = handCards;
    }
}
