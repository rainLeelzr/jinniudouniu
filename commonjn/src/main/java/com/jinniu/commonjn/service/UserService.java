package com.jinniu.commonjn.service;

import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.User;
import net.sf.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.util.Map;


public interface UserService extends BaseService<Integer, User> {
	public Map<String, Object> login(JSONObject data, String ip) throws Exception;
	public TextMessage TestConnection() ;

	User logout(JSONObject data);

	Map<String,Object> getUser(JSONObject data,User user);

    Map<String,Object> prizeDraw(JSONObject data,User user);

	Map<String, Object> freeCoins(User user);

	Map<String, Object> buy(JSONObject data, User user);

	Map<String, Object> bindPhone(JSONObject data, User user);

	Map<String, Object> tenWins(JSONObject data, User user);

    Map<String, Object> getStanding(Room room, User user);
}