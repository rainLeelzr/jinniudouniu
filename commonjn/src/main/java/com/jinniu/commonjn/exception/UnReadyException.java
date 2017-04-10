package com.jinniu.commonjn.exception;

/**
 * 开始游戏失败,还有玩家没有准备
 * @author Administrator
 *
 */
public class UnReadyException extends RuntimeException {

	public UnReadyException() {
		super();
	}

	public UnReadyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnReadyException(String message) {
		super(message);
	}

	public UnReadyException(Throwable cause) {
		super(cause);
	}


}
