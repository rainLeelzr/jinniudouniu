package com.jinniu.commonjn.exception;

/**
 * 房间不存在
 * @author Administrator
 *
 */
public class RoomNotExistException extends RuntimeException {

	public RoomNotExistException() {
		super();
	}

	public RoomNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoomNotExistException(String message) {
		super(message);
	}

	public RoomNotExistException(Throwable cause) {
		super(cause);
	}



}
