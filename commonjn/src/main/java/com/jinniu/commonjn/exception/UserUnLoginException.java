package com.jinniu.commonjn.exception;

/**
 * 用户未登陆
 * @author Administrator
 *
 */
public class UserUnLoginException extends RuntimeException {

	public UserUnLoginException() {
		super();
	}

	public UserUnLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserUnLoginException(String message) {
		super(message);
	}

	public UserUnLoginException(Throwable cause) {
		super(cause);
	}


}
