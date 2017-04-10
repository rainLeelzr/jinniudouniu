package com.jinniu.commonjn.exception;

/**
 * 玩家没有在房间中
 * @author Administrator
 *
 */
public class UserNotInRoomException extends RuntimeException {

	public UserNotInRoomException() {
		super();
	}

	public UserNotInRoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotInRoomException(String message) {
		super(message);
	}

	public UserNotInRoomException(Throwable cause) {
		super(cause);
	}


}
