package com.jinniu.commonjn.service;

import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.User;
import net.sf.json.JSONObject;

import java.util.Map;

public interface RoomService extends BaseService<Integer, Room> {
    //public Map<String,Object> createRoom(JSONObject data,User user);
    public Map<String,Object> createRoom(JSONObject data);

    Map<String,Object> joinRoom(JSONObject data,User user);

    Map<String,Object> outRoom(JSONObject data,User user);

    Map<String, Object> ready(User user) throws IllegalAccessException, InstantiationException;


    Map<String, Object> dismissRoom(JSONObject data,User user);

    Map<String, Object> agreeDismiss(JSONObject data,User user);

    Map<String, Object> numberOfPlayers(JSONObject data);
}