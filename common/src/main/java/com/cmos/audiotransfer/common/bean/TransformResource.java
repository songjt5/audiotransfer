package com.cmos.audiotransfer.common.bean;

import com.google.gson.annotations.Expose;

import java.util.Date;

/*
 * 转写资源实体*/
public class TransformResource {

    //转写资源类型名称
    @Expose String typeName;

    //转写资源类型编码
    @Expose String typeCode;

    //转写资源内部编号
    @Expose Integer innerCode;
    /*资源的上次回收时间戳*/
    @Expose String lastRecoverTime;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(Integer innerCode) {
        this.innerCode = innerCode;
    }

    public String getLastRecoverTime() {
        return lastRecoverTime;
    }

    public void setLastRecoverTime(String lastRecoverTime) {
        this.lastRecoverTime = lastRecoverTime;
    }

    public String getResourceId() {
        return new StringBuilder(typeCode).append("_").append(innerCode).toString();
    }
}
