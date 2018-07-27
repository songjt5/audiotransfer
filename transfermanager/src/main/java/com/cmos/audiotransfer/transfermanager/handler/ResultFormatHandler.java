package com.cmos.audiotransfer.transfermanager.handler;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.util.ApplicationContextUtil;
import com.cmos.audiotransfer.transfermanager.bean.EngineResultModel;
import com.cmos.audiotransfer.transfermanager.bean.FileAnalyzeInfo;
import com.cmos.audiotransfer.transfermanager.bean.ProcessStatusModel;
import com.cmos.audiotransfer.transfermanager.bean.TransformResult;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.audiotransfer.transfermanager.isa.ISAEngine;
import com.cmos.audiotransfer.transfermanager.isa.IsaEnginePool;
import com.cmos.audiotransfer.transfermanager.isa.XmlResultParser;
import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by hejie
 * Date: 18-7-26
 * Time: 下午3:22
 * Description:
 */
public class ResultFormatHandler implements EventHandler<TransformEvent> {


    @Override public void onEvent(TransformEvent event, long l, boolean b) throws Exception {
        if (StringUtils.isEmpty(event.getXml())) {
            return;
        }
        TaskBean task = event.getTask();
        XmlResultParser parser = (XmlResultParser) ApplicationContextUtil
            .getBean(TransformConsts.KEY_BEANNAME_XMLRESULTPARSER);
        TransformResult result = parser.ParseXML(event.getXml());
        EngineResultModel model = fetchISAResult(result, task.getId(), event.getLocalPath());
    }

    private EngineResultModel fetchISAResult(TransformResult result, String serialNumber,
        String voiceUri) {
        // 获取转写结果
        if (null != result) {
            FileAnalyzeInfo fileAnalyzeInfo = TransformResult.ToDatabaseFormat(result);
            return new EngineResultModel(serialNumber, voiceUri, result.getDuration(),
                result.getFormat(), fileAnalyzeInfo.getContent(), fileAnalyzeInfo.getTimePosition(),
                fileAnalyzeInfo.getChannels(),
                fileAnalyzeInfo.getContent().replace(";", "").length(),
                fileAnalyzeInfo.getSilences(), fileAnalyzeInfo.getSilenceLong(),
                fileAnalyzeInfo.getInterrupted(), fileAnalyzeInfo.getN0Speed(),
                fileAnalyzeInfo.getN1Speed(), fileAnalyzeInfo.getN0Energy(),
                fileAnalyzeInfo.getN1Energy(), result.getVadDuration(), ProcessStatusModel.SUCCESS,
                result.getN1VadDuration(), result.getN0VadDuration());
        }
        return null;
    }
}
