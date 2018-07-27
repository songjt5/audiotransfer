package com.cmos.audiotransfer.common.config;

import com.cmos.audiotransfer.common.util.ApplicationContextUtil;
import com.cmos.audiotransfer.common.util.LocalHostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * Created by hejie
 * Date: 18-7-13
 * Time: 上午10:11
 * Description:
 */
@Configuration public class CommonConfig {
    Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    @Bean public LocalHostInfo localHost() {
        try {
            LocalHostInfo hostInfo = new LocalHostInfo();
            hostInfo.init();
            return hostInfo;
        } catch (UnknownHostException e) {
            logger.error("there is no host info!");
            System.exit(1);
            return null;
        }
    }

    @Bean public ApplicationContextUtil applicationContextUtil(ApplicationContext context) {
        ApplicationContextUtil contextUtil = new ApplicationContextUtil();
        contextUtil.setApplicationContext(context);
        return contextUtil;
    }
}
