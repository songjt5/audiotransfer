package com.cmos.audiotransfer.resourcemanager.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.List;

/**
 * Created by hejie
 * Date: 18-7-19
 * Time: 上午9:32
 * Description:
 */
public class RedisLockImpl implements RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLockImpl.class);
    private RedisTemplate redisTemplate;
    private String key;
    private StringRedisSerializer serializer;
    private volatile boolean locked = false;
    private String expiresStr;
    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;
    private static final String COMPARE_AND_DELETE =
        "if redis.call('get',KEYS[1]) == ARGV[1]\n" + "then\n"
            + "    return redis.call('del',KEYS[1])\n" + "else\n" + "    return 0\n" + "end";

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int expireMsecs = 5 * 1000;

    /**
     * 锁等待时间，防止线程饥饿
     */
    private int timeoutMsecs = 5 * 1000;

    public RedisLockImpl(RedisTemplate redisTemplate, String key,
        StringRedisSerializer serializer) {
        this.redisTemplate = redisTemplate;
        this.key = key + "_lock";
        this.serializer = serializer;
    }

    @Override public boolean lock() throws InterruptedException {

        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (this.setNX(key, expiresStr)) {
                // lock acquired
                locked = true;
                this.expiresStr = expiresStr;
                return true;
            }

            String currentValueStr = this.get(key); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System
                .currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired
                String oldValueStr = this.getSet(key, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
                    //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    this.expiresStr = expiresStr;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
        }
        return false;
    }

    private String get(final String key) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                    byte[] data = connection.get(serializer.serialize(key));
                    if (data == null) {
                        return null;
                    }
                    return serializer.deserialize(data);
                }
            });
        } catch (Exception e) {
            logger.error("get redis error, key : {}", key);
            return null;

        }
        return obj != null ? obj.toString() : null;
    }

    private Boolean setNX(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute((RedisCallback<Object>) connection -> {
                Boolean success =
                    connection.setNX(serializer.serialize(key), serializer.serialize(value));
                return success;
            });
        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key);
            return false;
        }

        return obj != null ? (Boolean) obj : false;
    }

    private String getSet(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                    byte[] ret =
                        connection.getSet(serializer.serialize(key), serializer.serialize(value));
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key);
            return null;
        }
        return obj != null ? (String) obj : null;
    }


    /**
     * 释放redis分布式锁
     */
    @Override public void unlock() {
        List<String> keys = Collections.singletonList(key);
        redisTemplate
            .execute(new DefaultRedisScript<>(COMPARE_AND_DELETE, String.class), keys, expiresStr);
    }

    @Override public boolean locked() {
        return locked;
    }

    @Override public void close() throws Exception {
        this.unlock();
    }

}
