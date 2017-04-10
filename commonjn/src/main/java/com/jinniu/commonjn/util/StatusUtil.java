package com.jinniu.commonjn.util;

/**
 * 常用的常量
 * @author Jan
 *
 */
public class StatusUtil {
	
	// 系统模块
	/**成功*/
	public static final int SYS_SUCCESS = 1;
	
	/**失败*/
	public static final int SYS_FAIL = -1;	
	
	/**参数不完整*/
	public static final int SYS_DATA_INCOMPLETE = 10001;
	
	/**非法数据*/
	public static final int SYS_DATA_INVALID = 10002;
	
	
	// 游戏模块
	/**房卡不足*/
	public static final int CRAD_INVALID = 20001;
	/**房间已存在*/
	public static final int ROOM_EXIST = 20002;
	/**房间不存在*/
	public static final int ROOM_NOT_EXIST = 20003;
	/**房间已满人*/
	public static final int ROOM_PLAYER_FULL = 20004;
	
	
	// 用户模块
	/**非法用户*/
	public static final int USER_ILLEGAL = 30001;
}
