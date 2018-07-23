package com.cmos.audiotransfer.transfermanager.service;

import com.cmos.audiotransfer.transfermanager.service.pool.AnalysisPool;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午3:57
 * Description:
 */
public class EPCService extends Service{

    private static final Logger LOGGER = LoggerFactory.getLogger(EPCService.class);

    private static ConcurrentHashMap<String, AtomicBoolean> atomicMap = new ConcurrentHashMap();

    // 缓存任务的父文件夹
    // Guava Cache是一个全内存的本地缓存实现，它提供了线程安全的实现机制
    private Cache<String, String> cacheParentDir = CacheBuilder.newBuilder().build();

    // 探测下载线程池
    private ExecutorService probePool = null;

    // 分批线程池
    private ExecutorService batchPool = null;

    // 失败任务移出线程
    private ExecutorService failTaskMovePool = null;

    // 任务处理线程池
    private ExecutorService taskProcessPool = null;

    // 批次处理对象
    private AnalysisPool analysisPool = null;

    private ExecutorService uploadPool = null;

    public EPCService(String serviceName) {
        super(checkNotNull(serviceName));
    }

    @Override
    protected void startUp() {

        // 初始化各线程池
       /* probePool = Executors.newFixedThreadPool(1,
            new ThreadFactoryBuilder().setNameFormat("probe#thread#%d").build());
        batchPool = Executors.newFixedThreadPool(1,
            new ThreadFactoryBuilder().setNameFormat("batch#thread#%d").build());
        failTaskMovePool = Executors.newFixedThreadPool(1,
            new ThreadFactoryBuilder().setNameFormat("failTaskMove#thread#%d").build());
        analysisPool = new AnalysisPool(threadCount, cacheParentDir);
        taskProcessPool = Executors.newFixedThreadPool(Configure.getIntProperty(Constants.TASK_PROCESS_NUM),
            new ThreadFactoryBuilder().setNameFormat("taskProcess#thread#%d").build());
        uploadPool = Executors.newFixedThreadPool(Configure.getIntProperty(Constants.UPLOAD_PROCESS_NUM));
        LOGGER.info("init all pools success!");
        LOGGER.info("Engine Process Component start up success!");*/

    }

    @Override
    protected void shutDown() throws Exception {
        super.shutDown();
    }

    @Override
    protected void run() throws Exception {

       /* // 启动本地探测下载线程，本地探测
        // 在这里cacheParentDir的作用是，存放已经处理的任务文件信息（文件名称，父级目录）
        probePool.submit(new ProbeThread(cacheParentDir));

        // 启动本地文件分批线程
        batchPool.submit(new BatchThread());

        // 启动失败任务移除重跑
        failTaskMovePool.submit(new FailTaskMoveThread());

        ResultSender resultSender;

        try {
            AbstractFtp ftp = FtpFactory.getInstance(Configure.getProperty(Constants.FILE_TRANS_PROTOL));
            resultSender = new ResultSender(ftp);
        } catch (Exception e) {
            LOGGER.error("init ftp send param error![发送集成组件ftp参数错误，请检查参数！]", e);
            stopAsync();
            return;
        }

        FileLocker newFileLocker = null;

        Cache<String, String> taskFileCache = CacheBuilder.newBuilder().build();

        int activeCount = 0;
        int configTaskCount = Configure.getIntProperty(Constants.TASK_PROCESS_NUM);
        // 活动进程数小于总进程数，表示有任务已经执行完成可以执行下一步
        while (activeCount>=0) {
            try {
                // 获取文件任务队列taskFileQueue中的头文件
                activeCount = ((ThreadPoolExecutor) taskProcessPool).getActiveCount();
                if(activeCount >= configTaskCount) {
                    try {
                        Thread.sleep(Configure.getIntProperty(Constants.LOOP_PROBE_INTERVAL) * 1000);
                    } catch (InterruptedException e) {
                        LOGGER.error("LOOP_PROBE_INTERVAL thread excetion", e);
                    }
                    continue;
                }
                LOGGER.info("[###########开始提取任务begin taskFetcher.fetcher###########]");
                File taskFile = TaskFetcher.fetcher(taskFileCache);

                if (null != taskFile && taskFile.exists()) {
                    File newFile = DhxxFileUtil.getNewFileFromTaskFile(taskFile);
                    if (!newFile.exists()) {
                        continue;
                    }
                    // 判断atom是否存在当前任务，然后初始化当前任务的并标记为true
                    AtomicBoolean atom = atomicMap.get(taskFile.getAbsolutePath());
                    if (atom == null) {
                        atom = new AtomicBoolean(true);
                        atomicMap.put(taskFile.getAbsolutePath(), atom);
                    }

                    LOGGER.info("main thread try locked file[尝试加锁]:" + newFile.getAbsoluteFile());

                    // compareAndSet(expect , update) expect=true,执行方法体代码，然后根据update的值更新expect的值
                    // 当一个线程正在执行方法体内容时，其他线程需要等待其执行完毕，保持操作的原子性
                    if (atom.compareAndSet(true, false)) {
                        newFileLocker = new FileLocker(newFile);
                        if (taskFile.exists() && newFile.exists() && newFileLocker.isLocked()) {
                            LOGGER.info("main thread locked file[加锁成功]:" + newFile.getAbsoluteFile());
                            // 向任务处理线程池，提交执行任务文件信息
                            taskProcessPool.submit(new TaskProcessThread(analysisPool, resultSender, cacheParentDir,
                                taskFile, newFile, newFileLocker, atom, atomicMap, taskFileCache, uploadPool));
                        }
                    }
                }


            } catch (Exception e) {
                LOGGER.info("main thread error", e);
                try {
                    if (newFileLocker != null) {
                        newFileLocker.releaseLock();
                    }
                } catch (Exception e1) {
                    LOGGER.error("newFileLocker  excetion", e1);
                }
                LOGGER.error("main thread error", e);
            }
            try {
                Thread.sleep(Configure.getIntProperty(Constants.LOOP_PROBE_INTERVAL) * 1000);
            } catch (InterruptedException e) {
                LOGGER.error("LOOP_PROBE_INTERVAL thread excetion", e);
            }
        }*/
    }
}
