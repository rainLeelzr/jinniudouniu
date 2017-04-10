package com.jinniu.commonjn.exception;

/**
 * 用户不存在
 * @author Administrator
 *
 */
public class UserNotExistException extends RuntimeException {

	public UserNotExistException() {
		super();
	}

	public UserNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotExistException(String message) {
		super(message);
	}

	public UserNotExistException(Throwable cause) {
		super(cause);
	}


}
