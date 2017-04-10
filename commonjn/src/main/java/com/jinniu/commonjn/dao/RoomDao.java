package com.jinniu.commonjn.dao;

import com.jinniu.commonjn.model.Room;

public interface RoomDao extends BaseDao<Integer, Room> {
    long countForPlayers(Integer multiple);
}