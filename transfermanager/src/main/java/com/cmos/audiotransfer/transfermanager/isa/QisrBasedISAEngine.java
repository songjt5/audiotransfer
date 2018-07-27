package com.cmos.audiotransfer.transfermanager.isa;

import com.cmos.audiotransfer.transfermanager.bean.TransformResult;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午5:25
 * Description:
 */
public class QisrBasedISAEngine implements ISAEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(QisrBasedISAEngine.class);

    private String searchParam;

    private Qisr engine = new Qisr();

    private XmlResultParser parser = new XmlResultParser();

    private static int ENGINE_CODE = -10015;

    private boolean isCreateISAResult;

    public QisrBasedISAEngine(boolean isCreateISAResult) {
        this.isCreateISAResult = isCreateISAResult;
    }

    public QisrBasedISAEngine() {

    }

    // 初始化KWS引擎
    public boolean Initialize() {
        // 直接调用QISR接口
        Qisr inst = new Qisr();
        int func_ret = inst.QISRInit("");
        if (func_ret != Qisr.ISA_SUCCESS) {
            // 初始化引擎失败
            LOGGER.info("failed to init isa engine err = " + func_ret);
            return false;
        }

        return true;
    }

    // 逆初始化引擎
    public void Finalize() {
        // 逆初始化引擎
        Qisr inst = new Qisr();
        inst.QISRFini();
    }

    // 处理一个语音
    public boolean ProcessFile(final String uri, final String fmt, final String param) {
        // 调用引擎对一个语音进行建索引
        assert (engine != null);
        int[] status = new int[1];
        int func_ret = engine.QISRWaveformBuild(uri, fmt, param, status);
        // 记录引擎返回码
        ENGINE_CODE = func_ret;
        // 内核会阻塞再这里，直到处理完成
        if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISA_ERROR_ALREADY_EXIST) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        } else if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISPERR_SCI_HANDLE_TIMEOUT) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        } else if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISPERR_ICE_EXCEPTION) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        } else if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISPERR_SCI_CONN) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        } else if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISPERR_SCI_CONN_TIMEOUT) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        } else if (func_ret != Qisr.ISA_SUCCESS && func_ret == Qisr.ISAERR_WAVE_INVALID_TYPE) {
            LOGGER.info("failed to process file " + uri + ",err = " + func_ret);
            return false;
        }
        return true;
    }

    // 获取一个语音的基本信息及1best数据
    @Override public TransformResult FetchOneBest(final String xml_rst) {
        // 调用引擎成功
        // 解析xml以获取语音基本信息及1best数据
        TransformResult flag = null;
        flag = ParseOneBest(xml_rst);
        return flag;
    }

    @Override public String getResult(String uri) {
        assert (engine != null);
        int[] func_ret = new int[1];
        LOGGER.info("searchParam:" + searchParam);
        String xml_rst = engine.QISRWaveformSearch(uri, "", searchParam, func_ret);
        if (func_ret[0] != Qisr.ISA_SUCCESS) {
            LOGGER.error("isa client get result error:" + func_ret[0]);
            return null;
        }
        if (Strings.isNullOrEmpty(xml_rst)) {
            LOGGER.error("isa client get empty result, voice:" + uri);
            return null;
        }
        return xml_rst;
    }

    public boolean DeleteOneBest(final String uri) {
        checkNotNull(uri);
        return engine.QISRIndexMaintain(uri, "delete", "") == 0 ? true : false;
    }


    public Map<String, String> BatchProcessFile(String uriList, String fmt, String param) {
        return null;
    }

    // 解析xml以提取相关信息
    private TransformResult ParseOneBest(String xml) {
        return parser.ParseXML(xml);
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public void setCreateISAResult(boolean createISAResult) {
        isCreateISAResult = createISAResult;
    }


}
