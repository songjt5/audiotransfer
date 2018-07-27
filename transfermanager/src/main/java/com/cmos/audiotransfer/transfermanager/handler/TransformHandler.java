package com.cmos.audiotransfer.transfermanager.handler;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.util.ApplicationContextUtil;
import com.cmos.audiotransfer.transfermanager.config.IsaEngineConfigs;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.audiotransfer.transfermanager.isa.ISAEngine;
import com.cmos.audiotransfer.transfermanager.isa.IsaEnginePool;
import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:36
 * Description:
 */
public class TransformHandler implements EventHandler<TransformEvent> {
    private static final Logger logger = LoggerFactory.getLogger(TransformHandler.class);

    @Override public void onEvent(TransformEvent event, long l, boolean b) throws Exception {

        TaskBean task = event.getTask();
        if (StringUtils.isEmpty(event.getLocalPath())) {
            return;
        }
        IsaEnginePool<ISAEngine> pool = (IsaEnginePool<ISAEngine>) ApplicationContextUtil
            .getBean(TransformConsts.KEY_BEANNAME_ISAENGINEPOOL);
        IsaEngineConfigs configs = (IsaEngineConfigs) ApplicationContextUtil
            .getBean(TransformConsts.KEY_BEANNAME_ISAENGINECONFIGS);
        String resourceCode = configs.getVoiceFormat(task.getResourceId().split("_")[0]);
        if (StringUtils.isEmpty(resourceCode)) {
            logger.error("resourceCode is empty!", task);
            return;
        }
        String paramsStr = configs.getIsaParamStr(resourceCode);
        if (StringUtils.isEmpty(paramsStr)) {
            logger.error("paramsStr is empty!", task);
            return;
        }
        String voiceFormat = configs.getVoiceFormat(resourceCode);
        if (StringUtils.isEmpty(voiceFormat)) {
            logger.error("voiceFormat is empty!", task);
            return;
        }
        ISAEngine engine = pool.borrow();
        boolean ret = engine.ProcessFile(event.getLocalPath(), voiceFormat, paramsStr);

        if (ret) {
            String result = engine.getResult(event.getLocalPath());
            event.setXml(result);
        }
    }

}
