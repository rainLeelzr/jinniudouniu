package com.jinniu.commonjn.exception;

/**
 * 已经绑定手机
 *
 * @author Administrator
 */
public class AlreadyBindPhoneException extends RuntimeException {

    public AlreadyBindPhoneException() {
        super();
    }

    public AlreadyBindPhoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyBindPhoneException(String message) {
        super(message);
    }

    public AlreadyBindPhoneException(Throwable cause) {
        super(cause);
    }


}
