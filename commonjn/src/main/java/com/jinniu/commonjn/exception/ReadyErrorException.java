package com.jinniu.commonjn.exception;

/**
 * 玩家准备动作失败
 * @author Administrator
 *
 */
public class ReadyErrorException extends RuntimeException {

	public ReadyErrorException() {
		super();
	}

	public ReadyErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadyErrorException(String message) {
		super(message);
	}

	public ReadyErrorException(Throwable cause) {
		super(cause);
	}


}
