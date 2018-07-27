package com.cmos.audiotransfer.transfermanager.bean;

/**
 * Created by hejie
 * Date: 18-7-26
 * Time: 下午12:29
 * Description:
 */
public class EngineResultModel {

    private String serialNumber;

    private String voiceUri;

    private int duration;

    private String voiceFormat;

    private String content;

    private String timePosition;

    private String channels;

    private int contentLength;

    private String slience;

    private int slienceLong;

    private String interrupted;

    private String n0Speed;

    private String n1Speed;

    private String n0Energy;

    private String n1Energy;

    private int vadDuration;

    private ProcessStatusModel processStatusModel;

    // 2017-2-8 增加客户通话时长和坐席通话时长字段 by jsliu2
    private int customDuration;

    private int seatDuration;

    public EngineResultModel(ProcessStatusModel processStatusModel) {
        this.processStatusModel = processStatusModel;
    }

    public EngineResultModel(String serialNumber, String voiceUri, int duration, String voiceFormat, String content,
        String timePosition, String channels, int contentLength, String slience, int slienceLong,
        String interrupted, String n0Speed, String n1Speed, String n0Energy, String n1Energy, int vadDuration,
        ProcessStatusModel processStatusModel, int customDuration, int seatDuration) {
        this.serialNumber = serialNumber;
        this.voiceUri = voiceUri;
        this.duration = duration;
        this.voiceFormat = voiceFormat;
        this.content = content;
        this.timePosition = timePosition;
        this.channels = channels;
        this.contentLength = contentLength;
        this.slience = slience;
        this.slienceLong = slienceLong;
        this.interrupted = interrupted;
        this.n0Speed = n0Speed;
        this.n1Speed = n1Speed;
        this.n0Energy = n0Energy;
        this.n1Energy = n1Energy;
        this.vadDuration = vadDuration;
        this.processStatusModel = processStatusModel;
        this.customDuration = customDuration;
        this.seatDuration = seatDuration;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getVoiceUri() {
        return voiceUri;
    }

    public void setVoiceUri(String voiceUri) {
        this.voiceUri = voiceUri;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getVoiceFormat() {
        return voiceFormat;
    }

    public void setVoiceFormat(String voiceFormat) {
        this.voiceFormat = voiceFormat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getSlience() {
        return slience;
    }

    public void setSlience(String slience) {
        this.slience = slience;
    }

    public int getVadDuration() {
        return vadDuration;
    }

    public void setVadDuration(int vadDuration) {
        this.vadDuration = vadDuration;
    }

    public ProcessStatusModel getProcessStatusModel() {
        return processStatusModel;
    }

    public void setProcessStatusModel(ProcessStatusModel processStatusModel) {
        this.processStatusModel = processStatusModel;
    }

    public String getN0Speed() {
        return n0Speed;
    }

    public void setN0Speed(String n0Speed) {
        this.n0Speed = n0Speed;
    }

    public String getN1Speed() {
        return n1Speed;
    }

    public void setN1Speed(String n1Speed) {
        this.n1Speed = n1Speed;
    }

    public String getN0Energy() {
        return n0Energy;
    }

    public void setN0Energy(String n0Energy) {
        this.n0Energy = n0Energy;
    }

    public String getN1Energy() {
        return n1Energy;
    }

    public void setN1Energy(String n1Energy) {
        this.n1Energy = n1Energy;
    }

    public String getInterrupted() {
        return interrupted;
    }

    public void setInterrupted(String interrupted) {
        this.interrupted = interrupted;
    }

    public int getSlienceLong() {
        return slienceLong;
    }

    public void setSlienceLong(int slienceLong) {
        this.slienceLong = slienceLong;
    }

    public int getCustomDuration() {
        return customDuration;
    }

    public void setCustomDuration(int customDuration) {
        this.customDuration = customDuration;
    }

    public int getSeatDuration() {
        return seatDuration;
    }

    public void setSeatDuration(int seatDuration) {
        this.seatDuration = seatDuration;
    }

    @Override
    public String toString() {
        return "EngineResultModel{" + "serialNumber='" + serialNumber + '\'' + ", voiceUri='" + voiceUri + '\''
            + ", duration=" + duration + ", customDuration=" + customDuration + ", seatDuration=" + seatDuration
            + ", voiceFormat='" + voiceFormat + '\'' + ", content='" + content + '\'' + ", timePosition='"
            + timePosition + '\'' + ", channels='" + channels + '\'' + ", contentLength=" + contentLength
            + ", slience='" + slience + '\'' + ", slienceLong=" + slienceLong + ", interrupted='" + interrupted
            + '\'' + ", n0Speed='" + n0Speed + '\'' + ", n1Speed='" + n1Speed + '\'' + ", n0Energy='" + n0Energy
            + '\'' + ", n1Energy='" + n1Energy + '\'' + ", vadDuration=" + vadDuration + ", processStatusModel="
            + processStatusModel + '}';
    }
}
