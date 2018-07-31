package com.cmos.audiotransfer.transfermanager.bean;

import com.google.common.base.Strings;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:31
 * Description:
 */
public class TransformResult {

    private String uri = "";

    private String format = "";

    private int sampleRate = 8000;

    private int bitRate = 16;

    private String channel = "mono";

    private int duration = 0;

    private String comment = "";

    private List[] oneBest = new Vector[2];

    private String silences = "";

    private int silenceLong = 0;

    private String interrupted = "";

    private int n0VadDuration = 0;

    private int n1VadDuration = 0;

    private int vadDuration = 0;

    private String n0Speeds = "";

    private String n1Speeds = "";

    private String n0Energys = "";

    private String n1Energys = "";

    @Override
    public String toString() {
        return "TransformResult{" + "URI='" + uri + '\'' + ", Format='" + format + '\'' + ", SampleRate="
            + sampleRate + ", BitRate=" + bitRate + ", Channel='" + channel + '\'' + ", Duration=" + duration
            + ", Comment='" + comment + '\'' + ", OneBest=" + Arrays.toString(oneBest) + ", silences='" + silences
            + '\'' + ", silenceLong=" + silenceLong + ", interrupted='" + interrupted + '\'' + ", N0VadDuration="
            + n0VadDuration + ", N1VadDuration=" + n1VadDuration + ", VadDuration=" + vadDuration + ", n0Speeds='"
            + n0Speeds + '\'' + ", n1Speeds='" + n1Speeds + '\'' + ", n0Energys='" + n0Energys + '\''
            + ", n1Energys='" + n1Energys + '\'' + '}';
    }


    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getFormat() {
        return format;
    }


    public void setFormat(String format) {
        this.format = format;
    }


    public int getSampleRate() {
        return sampleRate;
    }


    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }


    public int getBitRate() {
        return bitRate;
    }


    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }


    public String getChannel() {
        return channel;
    }


    public void setChannel(String channel) {
        this.channel = channel;
    }


    public int getDuration() {
        return duration;
    }


    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public List[] getOneBest() {
        return oneBest;
    }


    public void setOneBest(int chan, List list) {
        this.oneBest[chan] = list;
    }


    public String getSilences() {
        return silences;
    }


    public void setSilences(String silences) {
        this.silences = silences;
    }


    public int getSilenceLong() {
        return silenceLong;
    }


    public void setSilenceLong(int silenceLong) {
        this.silenceLong = silenceLong;
    }


    public String getInterrupted() {
        return interrupted;
    }


    public void setInterrupted(String interrupted) {
        this.interrupted = interrupted;
    }


    public int getN0VadDuration() {
        return n0VadDuration;
    }


    public void setN0VadDuration(int n0VadDuration) {
        this.n0VadDuration = n0VadDuration;
    }


    public int getN1VadDuration() {
        return n1VadDuration;
    }


    public void setN1VadDuration(int n1VadDuration) {
        this.n1VadDuration = n1VadDuration;
    }


    public int getVadDuration() {
        return vadDuration;
    }


    public void setVadDuration(int vadDuration) {
        this.vadDuration = vadDuration;
    }


    public String getN0Speeds() {
        return n0Speeds;
    }


    public void setN0Speeds(String n0Speeds) {
        this.n0Speeds = n0Speeds;
    }


    public String getN1Speeds() {
        return n1Speeds;
    }


    public void setN1Speeds(String n1Speeds) {
        this.n1Speeds = n1Speeds;
    }


    public String getN0Energys() {
        return n0Energys;
    }


    public void setN0Energys(String n0Energys) {
        this.n0Energys = n0Energys;
    }


    public String getN1Energys() {
        return n1Energys;
    }


    public void setN1Energys(String n1Energys) {
        this.n1Energys = n1Energys;
    }
}
