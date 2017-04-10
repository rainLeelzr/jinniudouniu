package com.jinniu.commonjn.exception;

/**
 * 今日免费领取金币次数已用完
 *
 * @author Administrator
 */
public class AlreadyGetCoinsException extends RuntimeException {

    public AlreadyGetCoinsException() {
        super();
    }

    public AlreadyGetCoinsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyGetCoinsException(String message) {
        super(message);
    }

    public AlreadyGetCoinsException(Throwable cause) {
        super(cause);
    }


}
