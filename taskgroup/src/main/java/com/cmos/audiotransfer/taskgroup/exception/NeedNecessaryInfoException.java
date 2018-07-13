package com.cmos.audiotransfer.taskgroup.exception;

public class NeedNecessaryInfoException extends RuntimeException {

    private String key;

    private String code = "101";

    public NeedNecessaryInfoException(String key){
        super(new StringBuilder(key).append(" is miss").toString());
        this.key = key;
    }

}
