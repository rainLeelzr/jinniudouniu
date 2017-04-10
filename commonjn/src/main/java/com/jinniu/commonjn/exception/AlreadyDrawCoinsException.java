package com.jinniu.commonjn.exception;

/**
 * 今日金币抽奖次数已用完
 * @author Administrator
 *
 */
public class AlreadyDrawCoinsException extends RuntimeException {

	public AlreadyDrawCoinsException() {
		super();
	}

	public AlreadyDrawCoinsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyDrawCoinsException(String message) {
		super(message);
	}

	public AlreadyDrawCoinsException(Throwable cause) {
		super(cause);
	}


}
