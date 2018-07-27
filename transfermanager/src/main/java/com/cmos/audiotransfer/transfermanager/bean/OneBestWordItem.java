package com.cmos.audiotransfer.transfermanager.bean;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:33
 * Description:
 */
public class OneBestWordItem {
    private String word;

    private int begin;

    private int end;

    private int channel;

    @Override public String toString() {
        return "OneBestWordItem{" + "Word='" + word + '\'' + ", Begin=" + begin + ", End=" + end
            + ", Channel=" + channel + '}';
    }


    public String getWord() {
        return word;
    }


    public void setWord(String word) {
        this.word = word;
    }


    public int getBegin() {
        return begin;
    }


    public void setBegin(int begin) {
        this.begin = begin;
    }


    public int getEnd() {
        return end;
    }


    public void setEnd(int end) {
        this.end = end;
    }


    public int getChannel() {
        return channel;
    }


    public void setChannel(int channel) {
        this.channel = channel;
    }


}
