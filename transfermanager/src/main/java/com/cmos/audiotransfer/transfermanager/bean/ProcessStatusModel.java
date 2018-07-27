package com.cmos.audiotransfer.transfermanager.bean;

/**
 * Created by hejie
 * Date: 18-7-26
 * Time: 下午12:30
 * Description:
 */
public enum ProcessStatusModel {
    SUCCESS(0, "处理成功"), DOWN_LOAD_FAIL(1, "音频下载失败"), TRANSCODING_FAIL(2,
        "转码失败"), TRANSLITERATION_FAIL(3, "转写失败"), LOCAL_VOICE_NOT_EXIST(4,
        "本地音频不存在"), VOICE_URI_EMPTY(5, "音频路径为空"), VOICE_PLATFORM_INFO_ERROR(6,
        "平台信息错误"), FTP_CONFIG_ERROR(7, "FTP信息配置错误"), VOICE_NOT_EXIST(8, "录音不存在");

    private int code;

    private String description;

    private ProcessStatusModel(int code, String description) {
        this.code = code;
        this.description = description;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
