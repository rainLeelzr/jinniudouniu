package com.jinniu.commonjn.exception;

/**
 * 房间成员已满
 * @author Administrator
 *
 */
public class RoomMemberFullException extends RuntimeException {

	public RoomMemberFullException() {
		super();
	}

	public RoomMemberFullException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoomMemberFullException(String message) {
		super(message);
	}

	public RoomMemberFullException(Throwable cause) {
		super(cause);
	}


}
