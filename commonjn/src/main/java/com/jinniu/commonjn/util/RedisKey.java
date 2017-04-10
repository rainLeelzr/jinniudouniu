package com.jinniu.commonjn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;

public class RedisKey {
	
	

	/**
	 * hash表
		* "hand_card"			玩家的手牌Map<Integer, List<Integer>> hand_cards
		* "left_card"			剩余的牌库List<Integer> left_card
		* "dice_num"			骰子点数List<Integer> dice_num
		* "ready_'%local_id'"	准备状态
		* "handle_card"			玩家摸到的牌Map<Integer, Integer>
		* "out_card"			玩家出的牌Map<Integer, Integer>
		* "out_cards"			该房间所有玩家出的牌Map<Integer, List<Integer>>
		* "peng_are"			碰杠吃区域的牌Map<Integer, List<List<Integer>>> peng_are
		* "an_gang_are"			暗杠区域的牌Map<Integer, List<List<Integer>>> an_gang_are
		* "already"				已经准备的人数integer
		* "session"				储存玩家的session Map<String, WebSocketSession> userSessions
		* "precious_card"		宝牌List<Integer> preciousCard
		* "precious_card_count"	宝牌出的次数List<Integer> preciousCard
		* "current_innings"		当前的回合数 Integer
		* "innings"				开房的回合数 Integer
		* "player_number"		玩家总数 Integer
		* "message"				客户端发来的消息Map<Integer, String> message
		* "room_member"			房间号对应的成员id,name,img,sex,sit Map<Integer,List<String>> room_member
		* "mark"				房间成员的积分Map<Integer, List<Integer>>
		* "mark_'%local_id'"	房间成员的总积分Integer
		* "turn_id"				轮到的人的位置id integer 位置id 
		* "peng_person"			碰的牌是谁打的 Map<Integer, Integer> peng_person
		* "sit_person"			牌桌的id对应的玩家id  Map<Integer, Integer> sit_person
		* "game_state"			游戏的状态 String  before  during  end   
		* 
		* String  		
		* ""
		* 
		* 
	 */
	public static String ROOM = "room_%s";
	
	/**
	*hash表
	 *local_id 					该玩家对应的房间号
	 * 
	 * 
	 */
	public static String USER = "user_%s";//后面接的是房间号
	
	
	
	/**创建用户时保存用户信息*/
	public static String USER_INFO = "user_info_%s"; //后面接的是pid
	
	/**创建用户时作为存用户的openid*/
	public static String USER_UID = "user_uid_%s"; //后面接的是uid
	
	/**已在房间的用户 key是uid，value是room_id*/
	public static String IN_ROOM_USER = "in_room_user_%s"; //后面接的是uid
	
	
	/**
	 * ROOMOPTTIMEOUT:服务端每发出一个动作，则在该字段标记一下（累加一次，记录响应的记录数），并设置相应的过期时间，用于判断客户端响应是否超时
	 * ROOMOPTRESPONSE : 记录服务端是否响应成功的标识，默认是0，成功响应为1，设置超时时间为一天
	 * ROOMOPTRESPONSEUSER : 记录响应的用户id，服务器发送消息则初始化为null,存储类型为set
	 * ROOMOFFLINEUSER : 记录离线用户id【托管用户】，存储类型为set
	 */
	public static String ROOMOPTTIMEOUT = "room_opt_time_out_%s";
	public static String ROOMOPTRESPONSE = "room_opt_response_%s";
	public static String ROOMOPTRESPONSEUSER = "room_opt_response_user_%s";
	public static String ROOMOFFLINEUSER = "room_off_line_user_%s";
	/**ROOM hash表对应的field集合*/
	public static interface RoomField{
		
		/**
		 * 玩家出的牌Map<Integer, Integer> out_card
		 */
		static String OUT_CARD = "out_card";
		
		/**
		 * 该房间所有玩家出的牌Map<Integer, List<Integer>>
		 */
		static String OUT_CARDS = "out_cards";
		
		/**
		 * 玩家的手牌Map<Integer, List<Integer>> hand_cards
		 */
		static String HAND_CARD = "hand_card";
		
		/**
		 * 剩余的牌库List<Integer> left_card
		 */
		static String LEFT_CARD = "left_card";
		
		/**
		 * 骰子点数List<Integer> dice_num
		 */
		static String DICE_NUM = "dice_num";
		
		/**
		 * 已经准备的人数integer
		 */
		static String ALREADY = "already";
		
		/**
		 * 碰杠吃区域的牌Map<Integer, List<List<Integer>>> peng_are
		 */
		static String PENG_ARE = "peng_are";
		
		/**
		 * 碰杠吃区域的牌Map<Integer, List<List<Integer>>> peng_are
		 */
		static String TRUSTEESHIP_PLAYER = "trusteeship_player";
		
		/**
		 * 暗杠区域的牌Map<Integer, List<List<Integer>>> an_gang_are
		 */
		static String AN_GANG_ARE = "an_gang_are";
		
		/**
		 *  游戏的状态String time_out
		 */
		public static String ROOM_TIME_OUT = "room_time_out_%s";
		
		/**
		 * 宝牌List<Integer> preciousCard
		 */
		static String PRECIOUS_CARD = "precious_card";
		
		/**
		 * 宝牌出的次数List<Integer> preciousCard
		 */
		static String PRECIOUS_CARD_COUNT = "precious_card_count_%s";
		
		/**
		 * 当前的回合数 Integer
		 */
		static String CURRENT_INNINGS = "current_innings";
		
		/**
		 *  开房的回合数 Integer
		 */
		static String INNINGS = "innings";
		
		/**
		 * 玩家总数 Integer
		 */
		static String PLAYER_NUMBER = "player_number";
		
		/**
		 * 客户端发来的消息Map<Integer, String> message
		 */
		static String MESSAGE = "message";
		
		/**
		 * 玩家摸到的牌Map<Integer, Integer>
		 */
		static String HANDLE_CARD = "handle_card";
		
		/**
		 * 房间号对应的成员id,name,img,sex Map<Integer,List<String>> room_member
		 */
		static String ROOM_MEMBER = "room_member";
		
		/**
		 * 房间成员的积分Map<Integer, List<Integer>>
		 */
		static String MARK = "mark";
		
		/**
		 * 	房间成员的总积分Integer
		 */
		public static String TOTAL_MARK = "mark_%s";
		
		/**
		 * 轮到的人的位置id Map<Integer, Integer> integer sit，integer id 
		 */
		static String TURN_ID = "turn_id";
		
		/**
		 * 碰的牌是谁打的 Map<Integer, Integer> peng_person
		 */
		static String PENG_PERSON = "peng_person";
		/**
		 * 牌桌的id对应的玩家id  Map<Integer, Integer> sit_person
		 */
		static String SIT_PERSON = "sit_person";
		/**
		 * 牌桌的id对应的玩家id  Map<Integer, Integer> sit_person
		 */
		static String PERSON_SIT = "uid_sit";
		
		/**
		 * 储存玩家的session Map<String, WebSocketSession> userSessions
		 */
		static String SESSION = "session";
		
		/**
		 *  "ready_"			准备的状态Map<Integer, List<Integer>> hand_cards
		 */
		public static String READY = "ready_%s";//后面拼接uid
		
		/**
		 *  游戏的状态String game_state
		 */
		public static String GAME_STATE = "game_state";
		
		
		/**
		 *  自摸的次数Integer self_drawn
		 */
		public static String SELF_DRAWN = "self_drawn_%s";
		
		/**
		 *  胡牌的次数Integer hu_cards
		 */
		public static String HU_CARDS = "hu_cards_%s";
		
		/**
		 *  点炮的次数Integer fire_hole
		 */
		public static String FIRE_HOLE = "fire_hole_%s";
		
		
		
		
		
		/**
		 * 作用：记录某玩家出牌后，其他玩家的响应信息【用于处理吃、碰、胡的优先级问题】
		 * 存储类型：List<String>
		 * 使用场景：当某玩家出牌后，获取到其他玩家的响应信息，存储到该field里；
		 * 		      同时判断是否已经收到了所有玩家的响应信息，如果是，则开启异步线程处理吃、碰、胡的优先级问题
		 */
		static String OUTCARDRESPONSE = "out_card_response";
		
		/**记录房间玩家个数*/
		static String PLAYERNUMBER = "player_number";
		
		/**
		 * 作用：记录当前房间玩家
		 */
		static String ISRUNNBLE = "is_runnable";
		
	}
	
	
	
	public static String User = "user_%s";
	
	
	
	public static void main(String[] args) {
		String key = String.format(ROOM, "123");
		System.out.println(key);
		
	}
	
}
