package com.jinniu.commonjn.exception;

/**
 * 玩家在此房间还没有战绩
 *
 * @author Administrator
 */
public class NotStandsException extends RuntimeException {

    public NotStandsException() {
        super();
    }

    public NotStandsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotStandsException(String message) {
        super(message);
    }

    public NotStandsException(Throwable cause) {
        super(cause);
    }


}
