package com.jinniu.commonjn.exception;

/**
 * 玩家已经在房间中,不能再加入房间
 * @author Administrator
 *
 */
public class UserInRoomException extends RuntimeException {

	public UserInRoomException() {
		super();
	}

	public UserInRoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserInRoomException(String message) {
		super(message);
	}

	public UserInRoomException(Throwable cause) {
		super(cause);
	}


}
