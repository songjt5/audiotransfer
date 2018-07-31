package com.cmos.audiotransfer.transfermanager.handler;

import com.alibaba.fastjson.JSON;
import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.util.ApplicationContextUtil;
import com.cmos.audiotransfer.common.util.DateUtils;
import com.cmos.audiotransfer.transfermanager.config.IsaEngineConfigs;
import com.cmos.audiotransfer.transfermanager.config.JsonResultConfigs;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.onest.client.ONestClient;
import com.cmos.onest.client.ONestException;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by hejie
 * Date: 18-7-27
 * Time: 上午9:51
 * Description:
 */
public class UploadHandler implements EventHandler<TransformEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UploadHandler.class);

    @Override public void onEvent(TransformEvent transformEvent, long l, boolean b) {

        TaskBean task = transformEvent.getTask();
        String voiceId = task.getId();
        try {
            IsaEngineConfigs configs = (IsaEngineConfigs) ApplicationContextUtil
                .getBean(TransformConsts.KEY_BEANNAME_ISAENGINECONFIGS);
            StringBuilder bucketPrefix = new StringBuilder(configs.getBucketPrefix())
                .append(DateUtils.dateToString(task.getBeginTime()), 0, 6)
                .append(TransformConsts.KEY_SYMBOL_UNDERLINE).append(task.getChannelId())
                .append(TransformConsts.KEY_SYMBOL_UNDERLINE)
                .append(TransformConsts.SYMBOL_BUCKET_NAME_RESULT)
                .append(TransformConsts.KEY_SYMBOL_UNDERLINE);
            JsonResultConfigs resultConfigs = (JsonResultConfigs) ApplicationContextUtil
                .getBean(TransformConsts.KEY_BEANNAME_JSONRESULTCONFIGS);
            StringBuilder fileNamePrefixWithoutExt =
                new StringBuilder(DateUtils.dateToString(task.getBeginTime()).substring(0, 8))
                    .append(File.separator).append(resultConfigs.getDataSource())
                    .append(TransformConsts.KEY_SYMBOL_UNDERLINE)
                    .append(resultConfigs.getDataType())
                    .append(TransformConsts.KEY_SYMBOL_UNDERLINE)
                    .append(resultConfigs.getDimensionType())
                    .append(TransformConsts.KEY_SYMBOL_UNDERLINE).append(voiceId);

            String txtPath = uploadFile(transformEvent.getJson(), TransformConsts.SYMBOL_SUFFIX_TXT,
                bucketPrefix, fileNamePrefixWithoutExt);
            String xmlPath =
                uploadFile(transformEvent.getXml(), TransformConsts.SYMBOL_SUFFIX_XML, bucketPrefix,
                    fileNamePrefixWithoutExt);
            task.setXmlResult(xmlPath);
            task.setJsonResult(txtPath);

        } catch (Exception e) {
            logger.error("upload split file error=>" + voiceId, e);
        }


    }

    private String uploadFile(String content, String suffix, StringBuilder bucketPrefix,
        StringBuilder filePrefix) throws ONestException {
        String bucketName = bucketPrefix.append(suffix.substring(1)).toString();
        String fileName = filePrefix.append(suffix).toString();
        ONestClient.getClient().uploadAndGetPublicUrl(bucketName, fileName,
            new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        return new StringBuilder(bucketName).append(TransformConsts.KEY_SYMBOL_WAVELINE)
            .append(fileName).toString();
    }
}
