package com.jinniu.commonjn.exception;

/**
 * 未绑定手机
 *
 * @author Administrator
 */
public class UnBindPhoneException extends RuntimeException {

    public UnBindPhoneException() {
        super();
    }

    public UnBindPhoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnBindPhoneException(String message) {
        super(message);
    }

    public UnBindPhoneException(Throwable cause) {
        super(cause);
    }


}
