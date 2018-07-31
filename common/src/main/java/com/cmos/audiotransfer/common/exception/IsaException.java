package com.cmos.audiotransfer.common.exception;

/**
 * Created by hejie
 * Date: 18-7-27
 * Time: 上午10:58
 * Description:
 */
public class IsaException extends Exception{
    public IsaException(String message) {
        super(message);
    }

    public IsaException(Throwable cause) {
        super(cause);
    }

    public IsaException(String message, Throwable cause) {
        super(message, cause);
    }

}
