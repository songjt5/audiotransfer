package com.cmos.audiotransfer.transfermanager.isa;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:58
 * Description:
 */
public class IsaEnginePool<T> {
    private static Logger logger = LoggerFactory.getLogger(IsaEnginePool.class);


    private BlockingQueue<T> objects;

    public IsaEnginePool() {

    }

    public IsaEnginePool(Collection<? extends T> objects) {
        this.objects = new ArrayBlockingQueue<T>(objects.size(), false, objects);
    }

    public T borrow() throws InterruptedException {
        logger.info("========[begin get pool]---->[queue size:" + this.objects.size()
            + "]=====================" + objects);
        return this.objects.take();
    }

    public void giveBack(T object) throws InterruptedException {
        logger.info("========[give back pool]---->[queue size:" + this.objects.size()
            + "]=====================" + objects);
        this.objects.put(object);
    }

    public void clear() {
        this.objects.clear();
    }


    public BlockingQueue<T> getObjects() {
        return objects;
    }

    public void setObjects(BlockingQueue<T> objects) {
        this.objects = objects;
    }


}
