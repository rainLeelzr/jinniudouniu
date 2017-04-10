package com.jinniu.commonjn.exception;

/**
 * 已经领取过钻石了
 *
 * @author Administrator
 */
public class AlreadyGetDiamondException extends RuntimeException {

    public AlreadyGetDiamondException() {
        super();
    }

    public AlreadyGetDiamondException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyGetDiamondException(String message) {
        super(message);
    }

    public AlreadyGetDiamondException(Throwable cause) {
        super(cause);
    }


}
