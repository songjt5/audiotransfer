package com.cmos.audiotransfer.taskgroup.utils;

public enum TaskPriority {
    FIVE(5), FOUR(4), THREE(3), TWO(2), ONE(1), DEFAULT(0);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    TaskPriority(int value) {
        this.value = value;
    }


}
