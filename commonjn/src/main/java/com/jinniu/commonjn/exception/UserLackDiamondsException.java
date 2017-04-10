package com.jinniu.commonjn.exception;

/**
 * 钻石不足
 * @author Administrator
 *
 */
public class UserLackDiamondsException extends RuntimeException {

	public UserLackDiamondsException() {
		super();
	}

	public UserLackDiamondsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserLackDiamondsException(String message) {
		super(message);
	}

	public UserLackDiamondsException(Throwable cause) {
		super(cause);
	}


}
