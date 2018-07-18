package com.cmos.audiotransfer.resourcemanager.lock;

/**
 * Created by hejie
 * Date: 18-7-16
 * Time: 下午7:53
 * Description:
 */
public interface RedisLock extends AutoCloseable {
    public boolean lock() throws InterruptedException;

    public void unlock();
}
