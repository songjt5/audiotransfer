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

    public static FileAnalyzeInfo ToDatabaseFormat(TransformResult raw) {
        FileAnalyzeInfo db_item = new FileAnalyzeInfo();
        StringBuilder content = new StringBuilder();
        StringBuilder time = new StringBuilder();
        StringBuilder channel = new StringBuilder();

        // 将1best数据先按照时间顺序保存起来
        TreeMap<Integer, OneBestWordItem> ob_map = new TreeMap<Integer, OneBestWordItem>();
        for (int chan = 0; chan <= 1; ++chan) {
            for (int i = 0; i < raw.oneBest[chan].size(); ++i) {
                if (CollectionUtils.isEmpty(raw.oneBest[chan]))
                    continue;

                List<OneBestWordItem> ob_lst = raw.oneBest[chan];
                ob_map.put(ob_lst.get(i).getBegin(), ob_lst.get(i));
            }
        }

        // 构造整合后的字符串
        for (Iterator<Map.Entry<Integer, OneBestWordItem>> it = ob_map.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, OneBestWordItem> word = it.next();
            // 末尾加上逗号，最后删除即可
            content.append(word.getValue().getWord());
            time.append(String.format("%d,%d", word.getValue().getBegin(), word.getValue().getEnd()));
            channel.append(String.format("%d", word.getValue().getChannel()));
            if (it.hasNext()) {
                content.append(";");
                time.append(";");
                channel.append(";");
            }
        }

        db_item.setContent(content.toString());
        db_item.setTimePosition(time.toString());
        db_item.setChannels(channel.toString());
        if (!Strings.isNullOrEmpty(raw.silences)) {
            db_item.setSilences(raw.silences.substring(0, raw.silences.length() - 1));
        } else {
            db_item.setSilences("");
        }

        db_item.setSilenceLong(raw.silenceLong);

        if (!Strings.isNullOrEmpty(raw.interrupted)) {
            db_item.setInterrupted(raw.interrupted.substring(0, raw.interrupted.length() - 1));
        } else {
            db_item.setInterrupted("");
        }

        if (!Strings.isNullOrEmpty(raw.n0Speeds)) {
            db_item.setN0Speed(raw.n0Speeds.substring(0, raw.n0Speeds.length() - 1));
        } else {
            db_item.setN0Speed("");
        }

        if (!Strings.isNullOrEmpty(raw.n1Speeds)) {
            db_item.setN1Speed(raw.n1Speeds.substring(0, raw.n1Speeds.length() - 1));
        } else {
            db_item.setN1Speed("");
        }

        if (!Strings.isNullOrEmpty(raw.n0Energys)) {
            db_item.setN0Energy(raw.n0Energys.substring(0, raw.n0Energys.length() - 1));
        } else {
            db_item.setN0Energy("");
        }

        if (!Strings.isNullOrEmpty(raw.n1Energys)) {
            db_item.setN1Energy(raw.n1Energys.substring(0, raw.n1Energys.length() - 1));
        } else {
            db_item.setN1Energy("");
        }
        return db_item;
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
