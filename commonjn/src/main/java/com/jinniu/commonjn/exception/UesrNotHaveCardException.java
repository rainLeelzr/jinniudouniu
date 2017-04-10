package com.jinniu.commonjn.exception;

/**
 * 用户没有这张牌
 *
 * @author Administrator
 */
public class UesrNotHaveCardException extends RuntimeException {

    public UesrNotHaveCardException() {
        super();
    }

    public UesrNotHaveCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public UesrNotHaveCardException(String message) {
        super(message);
    }

    public UesrNotHaveCardException(Throwable cause) {
        super(cause);
    }


}
