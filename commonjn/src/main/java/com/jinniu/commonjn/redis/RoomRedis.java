package com.jinniu.commonjn.redis;

import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.redis.base.Redis;
import com.jinniu.commonjn.util.CommonError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoomRedis {

    @Autowired
    private Redis redis;

    private static String ROOM_MEMBER_KEY = "roomMember_roomId_%s";

    public static String ROOM_KEY = "roomId_%s";

    public static String ROOM_INFO_FIELD_KEY = "roomInfo";

    /**
     * 创建房间,将房间信息放到redis
     */
    public void createRoom(Room room, RoomMember roomMember) {
        redis.hash.put(
                String.format(ROOM_KEY, room.getId()),
                ROOM_INFO_FIELD_KEY,
                room
        );
        joinRoom(roomMember);
    }
    /**
     * 解散房间,清除redis中的房间信息
     */
    public void dismissRoom(Room room) {
            redis.hash.delete(
                    String.format(ROOM_KEY,room.getId()),
                    ROOM_INFO_FIELD_KEY
            );

    }

    public void updateRoom(Room room) {
        redis.hash.put(
                String.format(ROOM_KEY, room.getId()),
                ROOM_INFO_FIELD_KEY,
                room
        );
    }

    /**
     * 加入房间
     */
    public void joinRoom(RoomMember roomMember) {
        redis.sortedSet.add(
                String.format(ROOM_MEMBER_KEY, roomMember.getRoomId()),
                roomMember,
                roomMember.getSeat());
    }
    /**
     * 退出房间
     */
    public void editRoom(RoomMember roomMember) {
        redis.sortedSet.deleteByScore(
                String.format(ROOM_MEMBER_KEY,roomMember.getRoomId()),
                        (double)roomMember.getSeat(),(double)roomMember.getSeat());

    }


    /**
     * 获取房间里有多少个用户,不包含已退出房间的用户
     */
    public Long getRoomMemberCount(String roomId) {
        return redis.sortedSet.size(String.format(ROOM_MEMBER_KEY, roomId));
    }

    /**
     * 获取房间的玩家
     */
    public Set<RoomMember> getRoomMembers(String roomId) {
        Set<Object> temps = redis.sortedSet.get(
                String.format(ROOM_MEMBER_KEY, roomId),
                RoomMember.class);
        Set<RoomMember> roomMembers = new HashSet<RoomMember>(temps.size());
        for (Object roomMember : temps) {
            roomMembers.add((RoomMember) roomMember);
        }
        return roomMembers;
    }

    /**
     * 判断房间里的用户是否全部都已经准备
     */
    public boolean isAllReady(String roomId) {
        Room room = (Room) redis.hash.get(
                String.format(ROOM_KEY, roomId),
                ROOM_INFO_FIELD_KEY,
                Room.class);
        if (room == null) {
            throw CommonError.ROOM_NOT_EXIST.newException();
        }

        Long roomMemberCount = getRoomMemberCount(roomId);
        if (roomMemberCount > room.getPlayers()) {
            throw new RuntimeException(String.format(
                    "房间[roomId=%s]玩家人数上限为%s,但是redis中房间成员人数为%s",
                    roomId,
                    room.getPlayers(),
                    roomMemberCount)
            );
        }

        if (roomMemberCount < room.getPlayers()) {
            return false;
        }

        Set<RoomMember> roomMembers = getRoomMembers(roomId);
        for (RoomMember roomMember : roomMembers) {
            if (roomMember.getState() != RoomMember.state.READY.getCode()) {
                return false;
            }
        }
        return true;
    }
}
