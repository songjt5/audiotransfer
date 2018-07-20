package com.cmos.audiotransfer.taskgroup.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-20
 * Time: 上午9:37
 * Description:
 */
@Component @ConfigurationProperties(prefix = "configs") public class ChannelManager {
    public static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    public Map<String, String> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, String> channels) {
        this.channels = channels;
    }

    Map<String, String> channels;


    public boolean invalidChannelId(String channelId) {
        if (channels.containsKey(channelId.trim())) {
            return false;
        } else {
            logger.error("invalid channelId: " + channelId);
            return true;
        }
    }

    public void addChannel(String channelId, String channelName) {
        this.channels.put(channelId, channelName);
    }

    public void deleteChannel(String channelId) {
        this.channels.remove(channelId);
    }

    public Map<String, String> queryChannels() {
        return this.getChannels();
    }



}
