package com.jinniu.commonjn.exception;

/**
 * 没有轮到该用户操作
 *
 * @author Administrator
 */
public class NotYourTurnException extends RuntimeException {

    public NotYourTurnException() {
        super();
    }

    public NotYourTurnException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotYourTurnException(String message) {
        super(message);
    }

    public NotYourTurnException(Throwable cause) {
        super(cause);
    }


}
