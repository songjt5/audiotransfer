package com.cmos.audiotransfer.transfermanager.bean;

import java.io.Serializable;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:32
 * Description:
 */
public class FileAnalyzeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String content = "";

    private String timePosition = "";

    private String channels = "";

    private String interrupted = "";

    private String silences = "";

    private int silenceLong = 0;

    private String n0Speed = "";

    private String n1Speed = "";

    private String n0Energy = "";

    private String n1Energy = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSilences() {
        return silences;
    }

    public void setSilences(String silences) {
        this.silences = silences;
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

    public String getN1Energy() {
        return n1Energy;
    }

    public void setN1Energy(String n1Energy) {
        this.n1Energy = n1Energy;
    }

    public String getN0Energy() {
        return n0Energy;
    }

    public void setN0Energy(String n0Energy) {
        this.n0Energy = n0Energy;
    }

    public String getInterrupted() {
        return interrupted;
    }

    public void setInterrupted(String interrupted) {
        this.interrupted = interrupted;
    }

    public int getSilenceLong() {
        return silenceLong;
    }

    public void setSilenceLong(int silenceLong) {
        this.silenceLong = silenceLong;
    }

    @Override public String toString() {
        return "FileAnalyzeInfo{" + "id=" + id + ", content='" + content + '\'' + ", timePosition='"
            + timePosition + '\'' + ", channels='" + channels + '\'' + ", interrupted='"
            + interrupted + '\'' + ", silences='" + silences + '\'' + ", silenceLong=" + silenceLong
            + ", n0Speed='" + n0Speed + '\'' + ", n1Speed='" + n1Speed + '\'' + ", n0Energy='"
            + n0Energy + '\'' + ", n1Energy='" + n1Energy + '\'' + '}';
    }

    @Override public int hashCode() {
        int result = id;
        result = 31 * result + content.hashCode();
        result = 31 * result + timePosition.hashCode();
        result = 31 * result + channels.hashCode();
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (FileAnalyzeInfo.class != obj.getClass()) {
            return false;
        }

        FileAnalyzeInfo analyzeInfo = (FileAnalyzeInfo) obj;
        if (this.id == analyzeInfo.getId() && this.content.equals(analyzeInfo.getContent())
            && this.timePosition.equals(analyzeInfo.getTimePosition()) && this.channels
            .equals(analyzeInfo.getChannels())) {
            return true;
        } else {
            return false;
        }
    }

}
