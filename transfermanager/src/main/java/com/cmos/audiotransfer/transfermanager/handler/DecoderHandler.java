package com.cmos.audiotransfer.transfermanager.handler;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.transfermanager.constant.TransformStatus;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.audiotransfer.transfermanager.util.VoiceDecodeUtils;
import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:35
 * Description:
 */
public class DecoderHandler implements EventHandler<TransformEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DecoderHandler.class);

    @Override public void onEvent(TransformEvent event, long l, boolean b) {

        TaskBean task = event.getTask();
        if (StringUtils.isEmpty(event.getLocalPath()) || StringUtils
            .isEmpty(task.getTargetCodeType())) {
            return;
        }
        String localPath = event.getLocalPath();
        int index = localPath.lastIndexOf(".");
        if (index == -1) {
            event.setLocalPath(null);
            task.setStatus(TransformStatus.TASK_STATUS_INVALID_PATH_LOCAL);
            return;
        }
        String targetPath =
            new StringBuilder(localPath.substring(0, index)).append(task.getTargetCodeType())
                .toString();
        try {
            boolean success =
                VoiceDecodeUtils.transVoice(false, event.getLocalPath(), targetPath, "", "");
            if (success) {
                event.setLocalPath(targetPath);
            } else {
                event.setLocalPath(null);
                task.setStatus(TransformStatus.TASK_STATUS_DECODE_FAILED);
            }
        } catch (Exception e) {
            event.setLocalPath(null);
            task.setStatus(TransformStatus.TASK_STATUS_DECODE_FAILED);
            logger.error("file decode failed", e);
        } finally {
            File file = new File(localPath);
            if (file.exists() && file.isFile() && file.delete()) {
            } else {
                logger.error(localPath + " delete failed!");
            }
        }
    }
}
