package com.cmos.audiotransfer.common.constant;

public enum TaskPriority {
    THREE("3"), TWO("2"), ONE("1"), DEFAULT("default"), ZERO("0");

    private String value;

    public String getValue() {
        return value;
    }

    TaskPriority(String value) {
        this.value = value;
    }


}
