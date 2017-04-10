package com.jinniu.commonjn.manager.operate;

import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.mahjong.Mahjong;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 储存别人出牌后某个玩家可以的操作
 * 如胡、杠、碰
 */
public class CanDoOperate implements Comparable {

    // json转对象时用到的复杂对象转换字段关系映射
    public static Map<String, Class> classMap;

    static {
        classMap = new HashMap<>();
        classMap.put("roomMember", RoomMember.class);
        classMap.put("operates", Operate.class);
        classMap.put("specialUserId", Integer.class);
        classMap.put("specialMahjong", Mahjong.class);
    }

    private RoomMember roomMember;

    private Set<Operate> operates;

    /**
     * 打出牌的或摸到牌的玩家的userId
     */
    private Integer specialUserId;

    /**
     * 打出牌的或摸到牌的麻将
     */
    private Mahjong specialMahjong;

    public Integer getSpecialUserId() {
        return specialUserId;
    }

    public void setSpecialUserId(Integer specialUserId) {
        this.specialUserId = specialUserId;
    }

    public Mahjong getSpecialMahjong() {
        return specialMahjong;
    }

    public void setSpecialMahjong(Mahjong specialMahjong) {
        this.specialMahjong = specialMahjong;
    }

    public RoomMember getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(RoomMember roomMember) {
        this.roomMember = roomMember;
    }

    public Set<Operate> getOperates() {
        return operates;
    }

    public void setOperates(Set<Operate> operates) {
        this.operates = operates;
    }

    @Override
    public String toString() {
        String o = "";
        for (Operate operate : operates) {
            o += operate.getName() + "、";
        }
        return String.format("{座位%s,可以%s}", roomMember.getSeat(), o);
        //return "{\"CanDoOperate\":{"
        //        + "\"roomMember\":" + roomMember
        //        + ", \"operates\":" + operates
        //        + "}}";
    }

    @Override
    public int compareTo(Object o) {
        Operate first = null;
        for (Operate operate : operates) {
            first = operate;
            break;
        }

        Operate second = null;
        for (Operate operate : ((CanDoOperate) o).getOperates()) {
            second = operate;
            break;
        }

        return first.ordinal() - second.ordinal();

    }
}