package com.jinniu.commonjn.exception;

/**
 * 版本号已过期
 * @author Administrator
 *
 */
public class VersionTimeoutException extends RuntimeException {

	public VersionTimeoutException() {
		super();
	}

	public VersionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionTimeoutException(String message) {
		super(message);
	}

	public VersionTimeoutException(Throwable cause) {
		super(cause);
	}


}
