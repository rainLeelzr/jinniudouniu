package com.jinniu.commonjn.exception;

/**
 * 金币不足
 * @author Administrator
 *
 */
public class UserLackCoinsException extends RuntimeException {

	public UserLackCoinsException() {
		super();
	}

	public UserLackCoinsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserLackCoinsException(String message) {
		super(message);
	}

	public UserLackCoinsException(Throwable cause) {
		super(cause);
	}


}
