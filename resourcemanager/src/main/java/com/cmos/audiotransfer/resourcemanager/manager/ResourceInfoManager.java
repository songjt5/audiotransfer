package com.cmos.audiotransfer.resourcemanager.manager;

import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.resourcemanager.handler.ResourceProducer;
import com.cmos.audiotransfer.resourcemanager.lock.RedisLock;
import com.cmos.audiotransfer.resourcemanager.lock.RedisLockImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by hejie
 * Date: 18-7-16
 * Time: 下午7:24
 * Description:
 */
@Component public class ResourceInfoManager {

    Logger logger = LoggerFactory.getLogger(ResourceInfoManager.class);
    @Autowired RedisTemplate redisTemplate;
    @Autowired ResourceProducer resourceProducer;
    private StringRedisSerializer serializer = new StringRedisSerializer();


    private static final String COMPARE_AND_DELETE =
        "if redis.call('get',KEYS[1]) == ARGV[1]\n" + "then\n"
            + "    return redis.call('del',KEYS[1])\n" + "else\n" + "    return 0\n" + "end";


    public boolean recoverResource(TransformResource resource, String dataStr, String tags) {
        String resourceId = resource.getResourceId();
        String freshIndex = resource.getLastRecoverTime();
        RedisLock lock = new RedisLockImpl(redisTemplate, resourceId, serializer);

        try {
            String result = updateRecoveryTime(resourceId, freshIndex, lock);
            if (result == null) {
                return true;
            } else if (result.equals(freshIndex)) {
                return false;
            } else {
                resource.setLastRecoverTime(result);
                if (resourceProducer.sendResourceMsg(resource)) {
                    return true;
                } else {
                    try {
                        redisTemplate.opsForHash()
                            .put(ConfigConsts.RESOURCE_FRESH_INDEXES_KEY, resourceId, freshIndex);
                    } catch (Exception e) {
                        /*fresh index更新成功,消息放入资源队列失败*/
                        logger.error("redis index resume failed! resourceId:" + resourceId, e);
                        resourceProducer.resumeToStatusTopic(JSONUtil.toJSON(resource),
                            MQTagConsts.TAG_RESOURCE_NO_TASK);
                        return true;
                    }
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error("resource recovery error", e);
            return false;
        } finally {
            if (lock.locked()) {
                lock.unlock();
            }
        }

    }

    public String updateRecoveryTime(String resourceId, String freshIndex, RedisLock lock)
        throws Exception {

        if (lock.lock()) {
            Object obj =
                redisTemplate.opsForHash().get(ConfigConsts.RESOURCE_FRESH_INDEXES_KEY, resourceId);
            if (obj == null) {
                Long timestamp = System.currentTimeMillis();
                redisTemplate.opsForHash()
                    .put(ConfigConsts.RESOURCE_FRESH_INDEXES_KEY, resourceId, timestamp.toString());
                return timestamp.toString();
            } else {
                String latestFreshIndex = (String) obj;
                if (latestFreshIndex.equals(freshIndex)) {
                    Long timestamp = System.currentTimeMillis();
                    redisTemplate.opsForHash()
                        .put(ConfigConsts.RESOURCE_FRESH_INDEXES_KEY, resourceId,
                            timestamp.toString());
                    return timestamp.toString();
                } else {
                    return null;
                }
            }
        }
        return freshIndex;
    }


    public Set<String> resetResources(String resourceType) {
        Set<String> freshIndexKeys =
            redisTemplate.opsForHash().keys(ConfigConsts.RESOURCE_FRESH_INDEXES_KEY);
        Set<String> failedSet = new HashSet<>();
        if (StringUtils.isEmpty(resourceType)) {
            for (String key : freshIndexKeys) {
                resetFreshIndex(failedSet, key);
            }
        } else {
            for (String key : freshIndexKeys) {
                if (resourceType.equals(key.split("_")[0])) {
                    resetFreshIndex(failedSet, key);
                }
            }
        }

        return failedSet;
    }

    private void resetFreshIndex(Set<String> failedSet, String key) {
        RedisLock lock = new RedisLockImpl(redisTemplate, key, serializer);
        try {
            if (lock.lock()) {
                redisTemplate.opsForHash().delete(key);
            } else {
                failedSet.add(key);
                logger.error("resource fresh failed,because lock failed, Id：" + key);
            }
        } catch (InterruptedException e) {
            failedSet.add(key);
            logger.error("resource fresh failed Id：" + key, e);
        } finally {
            lock.unlock();
        }
    }
}
