package com.cmos.audiotransfer.common.beans;

import com.google.gson.annotations.Expose;

/*
 * 转写资源实体*/
public class ResourceBean {

    //转写资源类型名称
    @Expose String typeName;

    //转写资源类型编码
    @Expose String typeCode;

    //转写资源内部编号
    @Expose Integer innerCode;

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
}
