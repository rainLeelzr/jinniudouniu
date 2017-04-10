package com.jinniu.commonjn.exception;

/**
 * redis中的游戏数据异常
 * @author Administrator
 *
 */
public class RedisGameDataException extends RuntimeException {

	public RedisGameDataException() {
		super();
	}

	public RedisGameDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisGameDataException(String message) {
		super(message);
	}

	public RedisGameDataException(Throwable cause) {
		super(cause);
	}


}
