package com.jinniu.commonjn.model.mahjong;

import com.jinniu.commonjn.model.RoomMember;

/**
 * 玩家打出的牌
 */
public class OutCard {
    /**
     * 打出的牌的麻将对象
     */
    private Mahjong mahjong;
    /**
     * 打出这张牌的人
     */
    private RoomMember roomMember;

    public OutCard() {
    }

    public OutCard(Mahjong mahjong, RoomMember roomMember) {

        this.mahjong = mahjong;
        this.roomMember = roomMember;
    }

    public Mahjong getMahjong() {
        return mahjong;
    }

    public void setMahjong(Mahjong mahjong) {
        this.mahjong = mahjong;
    }

    public RoomMember getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(RoomMember roomMember) {
        this.roomMember = roomMember;
    }
}
