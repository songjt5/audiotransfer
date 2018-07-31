package com.cmos.audiotransfer.transfermanager.handler;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.util.ApplicationContextUtil;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.transfermanager.bean.AnalysisFileModel;
import com.cmos.audiotransfer.transfermanager.bean.OneBestWordItem;
import com.cmos.audiotransfer.transfermanager.bean.TransformResult;
import com.cmos.audiotransfer.transfermanager.config.JsonResultConfigs;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.audiotransfer.transfermanager.isa.XmlResultParser;
import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-26
 * Time: 下午3:22
 * Description:
 */
public class ResultFormatHandler implements EventHandler<TransformEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ResultFormatHandler.class);


    @Override public void onEvent(TransformEvent event, long l, boolean b) {
        if (StringUtils.isEmpty(event.getXml())) {
            return;
        }
        Map<String, String> resultInfo = JSONUtil.toMap(event.getTask().getContent());
        XmlResultParser parser = (XmlResultParser) ApplicationContextUtil
            .getBean(TransformConsts.KEY_BEANNAME_XMLRESULTPARSER);
        TransformResult result = parser.ParseXML(event.getXml());
        parse(result, resultInfo);
        event.setJson(JSONUtil.toJSON(packageResultInfo(resultInfo)));
    }

    private AnalysisFileModel packageResultInfo(Map<String, String> resultInfo) {
        JsonResultConfigs resultConfigs = (JsonResultConfigs) ApplicationContextUtil
            .getBean(TransformConsts.KEY_BEANNAME_JSONRESULTCONFIGS);
        AnalysisFileModel analysisFileModel = new AnalysisFileModel();
        analysisFileModel.setInsightType(resultConfigs.getInsightType());
        analysisFileModel.setTaskInsightField(resultConfigs.getTaskInsightField());
        analysisFileModel.setDimensionFieldIndexInfo(resultConfigs.getDimensionFieldIndexInfo());
        analysisFileModel.setDataSource(resultConfigs.getDataSource());
        analysisFileModel.setDataType(resultConfigs.getDataType());
        analysisFileModel.setDimensionType(resultConfigs.getDimensionType());
        analysisFileModel.setSpeechPlatform(resultConfigs.getSpeechPlatform());
        List<Map<String, String>> newDimMapValue = new ArrayList<>();
        newDimMapValue.add(resultInfo);
        analysisFileModel.setDimValueList(newDimMapValue);
        analysisFileModel.setVoiceSize(newDimMapValue.size());
        return analysisFileModel;
    }


    private void parse(TransformResult result, Map<String, String> resultInfo) {
        List<OneBestWordItem> channel0 = result.getOneBest()[0];
        List<OneBestWordItem> channel1 = result.getOneBest()[1];
        int i = 0;
        int j = 0;
        int contextLen = 0;
        int n0position = 0;
        int n1position = 0;
        StringBuilder noGapsContent = new StringBuilder();
        StringBuilder channels = new StringBuilder();
        StringBuilder timePosition = new StringBuilder();
        StringBuilder n0content = new StringBuilder();
        StringBuilder n1content = new StringBuilder();
        StringBuilder n0charPosition = new StringBuilder();
        StringBuilder n1charPosition = new StringBuilder();
        while (true) {
            OneBestWordItem currentItem = null;
            if (i < channel0.size() && j < channel1.size()) {
                if (channel0.get(i).getBegin() < channel1.get(j).getBegin()) {
                    currentItem = channel0.get(i);
                    i++;
                } else {
                    currentItem = channel1.get(j);
                    j++;
                }
            } else if (i < channel0.size()) {
                currentItem = channel0.get(i);
                i++;
            } else if (j < channel1.size()) {
                currentItem = channel1.get(j);
                j++;
            }
            if (currentItem == null) {
                break;
            }
            noGapsContent.append(currentItem.getWord());
            channels.append(String.format("%d", currentItem.getChannel()));
            timePosition
                .append(String.format("%d,%d", currentItem.getBegin(), currentItem.getEnd()));
            if (i < channel0.size() || j < channel1.size()) {
                noGapsContent.append(";");
                channels.append(";");
                timePosition.append(";");
            }
            int gap = 0;
            if (currentItem.getBegin() > contextLen * 100) {
                gap = currentItem.getBegin() / 100 - contextLen;
            }
            String contentWord = currentItem.getWord();
            if ("0".equals(currentItem.getChannel())) {
                for (int m = 1; m <= contentWord.length(); m++) {
                    appendPositionInfo(n0charPosition, n0position + m, contextLen + gap + m);
                }
                n0position += contentWord.length();
                n0content.append(contentWord);
            } else if ("1".equals(currentItem.getChannel())) {
                for (int m = 1; m <= contentWord.length(); m++) {
                    appendPositionInfo(n1charPosition, n1position + m, contextLen + gap + m);
                }
                n1position += contentWord.length();
                n1content.append(contentWord);
            }
            String gapsWord = addGapsChar(TransformConsts.KEY_SYMBOL_UNDERLINE, gap, contentWord);
            contextLen = contextLen + gapsWord.length();
        }

        addResultInfo(result, resultInfo, n0position, n1position, noGapsContent, channels,
            timePosition, n0content, n1content, n0charPosition, n1charPosition);


    }

    private void addResultInfo(TransformResult result, Map<String, String> resultInfo,
        int n0position, int n1position, StringBuilder noGapsContent, StringBuilder channels,
        StringBuilder timePosition, StringBuilder n0content, StringBuilder n1content,
        StringBuilder n0charPosition, StringBuilder n1charPosition) {
        resultInfo.put("content", noGapsContent.substring(0, noGapsContent.length() - 1));
        resultInfo.put("contentN0", n0content.substring(0, n0content.length() - 1));
        resultInfo.put("contentN1", n1content.substring(0, n1content.length() - 1));
        resultInfo.put("timePosition", timePosition.substring(0, timePosition.length() - 1));
        if (n0position > 0) {
            resultInfo.put("n0TimePosition",
                n0position + " " + n0charPosition.substring(0, n0charPosition.length() - 1));
        } else {
            resultInfo.put("n0TimePosition", null);
        }
        if (n1position > 0) {
            resultInfo.put("n1TimePosition",
                n1position + " " + n1charPosition.substring(0, n1charPosition.length() - 1));
        } else {
            resultInfo.put("n1TimePosition", null);
        }
        resultInfo.put("voiceUri", result.getUri());
        resultInfo.put("channelSeq", channels.substring(0, channels.length() - 1));
        resultInfo.put("silences", result.getSilences());
        resultInfo.put("silenceLong", Integer.toString(result.getSilenceLong()));
        resultInfo.put("duration", Integer.toString(result.getDuration()));
        resultInfo.put("vadDuration", Integer.toString(result.getVadDuration()));
        resultInfo
            .put("inVadDuration", Integer.toString(result.getDuration() - result.getVadDuration()));
        resultInfo.put("n0Speeds", result.getN0Speeds());
        resultInfo.put("n1Speeds", result.getN1Speeds());
        resultInfo.put("n0Energys", result.getN0Energys());
        resultInfo.put("n1Energys", result.getN1Energys());
        resultInfo.put("interrupted", result.getInterrupted());
        resultInfo.put("customDuration", Integer.toString(result.getN1VadDuration()));
        resultInfo.put("seatDuration", Integer.toString(result.getN0VadDuration()));
        resultInfo.put("voiceId", resultInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        resultInfo.put("timeFormat", resultInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        resultInfo.put("processed", "0");
    }



    private String addGapsChar(String addChar, int count, String target) {
        if (count == 0 || target == null)
            return target;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(addChar);
        }
        return result.append(target).toString();
    }

    private void appendPositionInfo(StringBuilder charPosition, int noGapsPosition,
        int gapsPosition) {
        charPosition.append(noGapsPosition);
        charPosition.append(",");
        charPosition.append(gapsPosition);
        charPosition.append(" ");
    }


}
