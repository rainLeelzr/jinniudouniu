package com.jinniu.commonjn.exception;

/**
 * 未完成足够的局数
 *
 * @author Administrator
 */
public class NotEnoughGamesException extends RuntimeException {

    public NotEnoughGamesException() {
        super();
    }

    public NotEnoughGamesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughGamesException(String message) {
        super(message);
    }

    public NotEnoughGamesException(Throwable cause) {
        super(cause);
    }


}
