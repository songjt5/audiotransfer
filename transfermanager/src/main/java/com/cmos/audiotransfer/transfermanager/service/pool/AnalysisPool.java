package com.cmos.audiotransfer.transfermanager.service.pool;

import com.google.common.cache.Cache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2015/8/5.
 */
public class AnalysisPool {

    /*private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisPool.class);

    // 工作流处理线程池(包含转码、转写、语义)
    private ExecutorService analyzePool = null;

    private ExecutorService ftpPool = null;

    private BlockingQueue<VoiceModel> queue = null;

    // 处理引擎池
    private Pool<ISAEngine> enginePool = null;

    // 缓存任务的父文件夹
    private Cache<String, String> cacheParentDir = null;

    *//**
     *
     * @Description (初始化分批转写需要用到对象)
     * @param threadCount
     *            Java虚拟机的可用的处理器数量
     * @param cacheParentDir
     *//*
    public AnalysisPool(int threadCount, Cache cacheParentDir) {
        init(threadCount, cacheParentDir);
    }

    private void init(int threadCount, Cache cacheParentDir) {
        // 转写线程池初始化
        analyzePool = Executors.newFixedThreadPool(threadCount,
                new ThreadFactoryBuilder().setNameFormat("analysis-thread-%d").build());
        // ftp线程池初始化
        ftpPool = Executors.newFixedThreadPool(Configure.getIntProperty(Constants.FTP_PROCESS_NUM));
        // 队列
        queue = new LinkedBlockingQueue<VoiceModel>(Integer.MAX_VALUE);
        int engineAuthCount = Configure.getIntProperty(Constants.ISA_ENGINE_AUTH_MAX);
        List<ISAEngine> engines = new ArrayList<>();

        if (Configure.getBoolProperty(Constants.USE_ISAENGINE)) {
            for (int i = 0; i < engineAuthCount; ++i) {
                engines.add(ISAFactory.getNewEngine());
            }
        }
        // else {
        // for (int i = 0; i < engineAuthCount; ++i) {
        // engines.add(ISAFactory.getNullEngine());
        // }
        // }
        // 转写引擎线程池初始化
        LOGGER.info("===engines加入队列===" + engines.size());
        enginePool = new Pool<ISAEngine>(engines);
        this.cacheParentDir = cacheParentDir;
    }

    *//**
     *
     * @Description (处理voiceTransModelList中所有的电话录音信息)<br/>
     *              1.根据电话录音信息，通过向ftpPool提交录音文件下载任务，将下载成功的录音文件,放入queue中<br/>
     *              2.根据queue中的录音信息，进行录音转写操作，在cache中存在处理结果信息<br/>
     * @param countDownLatch
     *            同步辅助类
     * @param cache
     *            录音转写结果
     * @param voiceTransModelList
     *            获取电话信息语音列表中电话流水号和文件URI
     * @param voiceFormat
     *            录音转写格式
     * @param taskFile
     *            任务文件
     * @param oneDataSourceCfg
     * @return
     * @throws Exception
     *//*
    public boolean analyze(CountDownLatch countDownLatch, Cache<String, EngineResultModel> cache,
            List<VoiceTransModel> voiceTransModelList, String voiceFormat, File taskFile,
            OneDataSourceCfg oneDataSourceCfg) throws EpcException, InterruptedException {
        try {
            checkNotNull(countDownLatch, "param countDownLatch is null!");
            checkNotNull(voiceTransModelList, "param serialNoUriList is null!");
            checkNotNull(voiceFormat, "param voiceFormat is null!");
            checkNotNull(taskFile, "param taskFile is null!");
            checkNotNull(oneDataSourceCfg, "param oneDataSourceCfg is null!");

            if (oneDataSourceCfg.getDataType() == EnumDataType.ANALYSIS_POST_DIM.getCode()) {
                // wenjianbak
                // for (VoiceTransModel voiceTransModel : voiceTransModelList) {
                // analyzePool.submit(new AnalysisThread_Extract( countDownLatch, enginePool,
                // voiceTransModel,
                // voiceFormat, taskFile, cache, cacheParentDir, oneDataSourceCfg));
                // }
            } else {
                for (VoiceTransModel voiceTransModel : voiceTransModelList) {
                    analyzePool.submit(new AnalysisThreadStandard(countDownLatch, enginePool, voiceTransModel,
                            voiceFormat, taskFile, cache, cacheParentDir, oneDataSourceCfg));
                }
            }
            countDownLatch.await();
            return true;
        } catch (Exception e) {
            LOGGER.error("analysis pool thread error", e);
            throw e;
        }
    }*/
}
