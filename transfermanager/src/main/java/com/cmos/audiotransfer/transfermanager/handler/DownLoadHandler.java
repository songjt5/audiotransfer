package com.cmos.audiotransfer.transfermanager.handler;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.util.LocalHostInfo;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import com.cmos.audiotransfer.transfermanager.constant.TransformStatus;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.onest.client.ONestClient;
import com.cmos.onest.client.ONestException;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:35
 * Description:
 */
public class DownLoadHandler implements EventHandler<TransformEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DownLoadHandler.class);

    @Override public void onEvent(TransformEvent event, long l, boolean b) throws Exception {

        TaskBean task = event.getTask();
        if (task.getPath().contains("~")) {
            String[] pathArray = task.getPath().split("~");
            String localPath = new StringBuilder(TransformConsts.LOCAL_VOICE_PATH).append("_")
                .append(task.getChannelId()).toString();
            try {
                ONestClient.getClient("cluster1").download(pathArray[0], pathArray[1], localPath);
                event.setLocalPath(localPath);
            } catch (ONestException e) {
                task.setStatus(TransformStatus.TASK_STATUS_ONEST_EXCEPTION);
                task.setDetail(new StringBuilder(LocalHostInfo.getLocalInfo()).append(e.toString())
                    .toString());
                logger.error("download voice failed!", e);
            }
        } else {
            task.setStatus(TransformStatus.TASK_STATUS_INVALID_PATH_REMOTE);
            logger.error("download voice failed!because the path is invalid" + task.getPath());
        }

    }
}
