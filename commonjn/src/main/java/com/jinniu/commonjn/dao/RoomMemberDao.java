package com.jinniu.commonjn.dao;

import com.jinniu.commonjn.model.RoomMember;

import java.util.List;

public interface RoomMemberDao extends BaseDao<Integer, RoomMember> {
    RoomMember selectByUserIdForCheck(RoomMember roomMember);
    RoomMember selectByUserIdForReady(RoomMember roomMember);
    List<RoomMember> selectForStart(RoomMember roomMember);
    List<RoomMember> selectForDismiss(RoomMember roomMember);
}