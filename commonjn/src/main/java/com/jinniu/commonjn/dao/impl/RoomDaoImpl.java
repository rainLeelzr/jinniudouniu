package com.jinniu.commonjn.dao.impl;

import com.jinniu.commonjn.dao.RoomDao;
import com.jinniu.commonjn.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl extends BaseDaoImpl<Integer, Room> implements RoomDao {

    @Override
    public long countForPlayers(Integer multiple) {

        return sqlSessionTemplate.selectOne(
                statement("countForPlayers"),
                multiple);
    }
}