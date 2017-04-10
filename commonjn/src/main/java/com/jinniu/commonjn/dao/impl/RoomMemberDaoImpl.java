package com.jinniu.commonjn.dao.impl;

import com.jinniu.commonjn.dao.RoomMemberDao;
import com.jinniu.commonjn.model.RoomMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomMemberDaoImpl extends BaseDaoImpl<Integer, RoomMember> implements RoomMemberDao {

    @Override
    public RoomMember selectByUserIdForCheck(RoomMember roomMember) {
        return sqlSessionTemplate.selectOne(
                statement("selectByUserIdForCheck"),
                roomMember
        );
    }

    @Override
    public RoomMember selectByUserIdForReady(RoomMember roomMember) {

        return sqlSessionTemplate.selectOne(
                statement("selectByUserIdForReady"),
                roomMember
        );
    }

    @Override
    public List<RoomMember> selectForStart(RoomMember roomMember) {
        return sqlSessionTemplate.selectList(
                statement("selectForStart"),
                roomMember
        );
    }
    @Override
    public List<RoomMember> selectForDismiss(RoomMember roomMember) {
        return sqlSessionTemplate.selectList(
                statement("selectForDismiss"),
                roomMember
        );
    }
}