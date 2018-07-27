package com.cmos.audiotransfer.transfermanager.isa;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:42
 * Description:
 */
public class Qisr {
    /**
     * The enumeration ISA_ERROR enumerates possible error codes
     * ISA_SUCCESS function completed successfully
     * ISA_ERROR_OUT_OF_MEMORY out of memory
     * ISA_ERROR_SYSTEM_ERROR general system error
     * ISA_ERROR_UNSUPPORTED operation unsupported
     * ISA_ERROR_BUSY the recognizer is busy
     * ISA_ERROR_INVALID_PARA invalid parameter
     * ISA_ERROR_INVALID_PARA_VALUE invalid parameter value
     * ISA_ERROR_INVALID_DATA input data is not valid
     * ISA_ERROR_INVALID_RULE grammar contains invalid rule
     * ISA_ERROR_INVALID_WORD grammar contains invalid word
     * ISA_ERROR_INVALID_NBEST_INDEX input nbestIndex is out of range
     * ISA_ERROR_URI_NOT_FOUND input URI is not found
     * ISA_ERROR_REC_GRAMMAR_ERROR error during grammar parsing (often ECMAscript
     * bug)
     * ISArec_ERROR_AUDIO_OVERFLOW audio buffer overflow
     * ISA_ERROR_OVERFLOW grammar/string/data buffer overflow
     * ISA_ERROR_REC_NO_ACTIVE_GRAMMARS recognition is started with no grammars
     * active
     * ISA_ERROR_REC_GRAMMAR_NOT_ACTIVATED can't deactivate a grammar that has
     * not been activated
     * ISA_ERROR_REC_NO_SESSION_NAME recognition can't start until session names
     * are defined
     * ISA_ERROR_REC_INACTIVE recognition has not begun
     * ISA_ERROR_NODATA no data is present
     * <p>
     * ISA_ERROR_NO_LICENSE a call to ISArecRecognizerStart() or
     * ISArecParseDTMFResults()was attempted but the recognizer does not have
     * a valid license checked out from the license server -OR- calls to
     * ISArecResourceAllocate/Free() fail because no license server is
     * available
     * <p>
     * ISA_ERROR_INVALID_MEDIA_TYPE an api or server supplied media type is
     * invalid
     * <p>
     * ISA_REC_ERROR_URI_TIMEOUT a uri fetch resulted in a timeout
     * ISA_REC_ERROR_URI_FETCH_ERROR a uri fetch resulted in an error
     * <p>
     * ISA_REC_ERROR_INVALID_LANGUAGE a language is not supported
     * ISA_REC_ERROR_DUPLICATE_GRAMMAR an activate of the same grammar is
     * attempted twice
     * <p>
     * ISA_REC_ERROR_INVALID_CHAR an invalid character is passed in an api call
     * (for example, all uri's must use ascii characters also surrogate pairs
     * are illegal)
     * <p>
     * ISA_ERROR_FAIL generic failure condition, retry is possible
     * ISA_ERROR_GENERAL generic recoverable error occurred
     * ISA_ERROR_GENERIC_FATAL_ERROR generic fatal error occurred
     */
    /* Generic Error defines */
    public static final int ISA_SUCCESS = 0;
    public static final int ISA_ERROR_FAIL = -1;
    public static final int ISA_ERROR_EXCEPTION = -2;

    /* Common errors */
    public static final int ISA_ERROR_GENERAL = 10000;  /* 0x2710 Generic Error */
    public static final int ISA_ERROR_OUT_OF_MEMORY = 10001;  /* 0x2711 */
    public static final int ISA_ERROR_FILE_NOT_FOUND = 10002;  /* 0x2712 */
    public static final int ISA_ERROR_NOT_SUPPORT = 10003;  /* 0x2713 */
    public static final int ISA_ERROR_NOT_IMPLEMENT = 10004;  /* 0x2714 */
    public static final int ISA_ERROR_ACCESS = 10005;  /* 0x2715 */
    public static final int ISA_ERROR_INVALID_PARA = 10006;  /* 0x2716 */
    public static final int ISA_ERROR_INVALID_PARA_VALUE = 10007;  /* 0x2717 */
    public static final int ISA_ERROR_INVALID_HANDLE = 10008;  /* 0x2718 */
    public static final int ISA_ERROR_INVALID_DATA = 10009;  /* 0x2719 */
    public static final int ISA_ERROR_NO_LICENSE = 10010;  /* 0X271A */
    public static final int ISA_ERROR_NOT_INIT = 10011;  /* 0X271B */
    public static final int ISA_ERROR_NULL_HANDLE = 10012;  /* 0X271C */
    public static final int ISA_ERROR_OVERFLOW = 10013;  /* 0X271D */
    public static final int ISA_ERROR_TIME_OUT = 10014;  /* 0X271E */
    public static final int ISA_ERROR_OPEN_FILE = 10015;  /* 0X271F */
    public static final int ISA_ERROR_NOT_FOUND = 10016;  /* 0X2720 */
    public static final int ISA_ERROR_NO_ENOUGH_BUFFER = 10017;  /* 0x2721 */
    public static final int ISA_ERROR_NO_DATA = 10018;  /* 0x2722 */
    public static final int ISA_ERROR_NO_MORE_DATA = 10019;  /* 0x2723 */
    public static final int ISA_ERROR_RES_MISSING = 10020;  /* 0x2724 */
    public static final int ISA_ERROR_SKIPPED = 10021;  /* 0x2725 */
    public static final int ISA_ERROR_ALREADY_EXIST = 10022;  /* 0x2726 */
    public static final int ISA_ERROR_LOAD_MODULE = 10023;  /* 0x2727 */
    public static final int ISA_ERROR_BUSY = 10024;  /* 0x2728 */
    public static final int ISA_ERROR_INVALID_CONFIG = 10025;  /* 0x2729 */
    public static final int ISA_ERROR_VERSION_CHECK = 10026;    /* 0x272A */
    public static final int ISA_ERROR_CANCELED = 10027;    /* 0x272B */
    public static final int ISA_ERROR_INVALID_MEDIA_TYPE = 10028;    /* 0x272C */
    public static final int ISA_ERROR_INPROC = 10029;  /* 0x272D */
    public static final int ISA_ERROR_PRECONDITION = 10030;    /* 0x272E */

    /* Error codes of Recognizer */
    public static final int ISA_ERROR_REC_GENERAL = 10100;  /* 0x2774 */
    public static final int ISA_ERROR_REC_INACTIVE = 10101;  /* 0x2775 */
    public static final int ISA_ERROR_REC_NO_SESSION_NAME = 10102;  /* 0x2776 */
    public static final int ISA_ERROR_REC_GRAMMAR_ERROR = 10103;  /* 0x2777 */
    public static final int ISA_ERROR_REC_NO_ACTIVE_GRAMMARS = 10104;  /* 0x2778 */
    public static final int ISA_ERROR_REC_GRAMMAR_NOT_LOADED = 10105;  /* 0x2779 */
    public static final int ISA_ERROR_REC_GRAMMAR_NOT_ACTIVATED = 10106;  /* 0x277A */
    public static final int ISA_ERROR_REC_GRAMMAR_DUPLICATE = 10107;  /* 0x277B */
    public static final int ISA_ERROR_REC_INVALID_MEDIA_TYPE = 10108;  /* 0x277C */
    public static final int ISA_ERROR_REC_INVALID_LANGUAGE = 10109;  /* 0x277D */
    public static final int ISA_ERROR_REC_INVALID_RULE = 10110;  /* 0x277E */
    public static final int ISA_ERROR_REC_INVALID_WORD = 10111;  /* 0x277F */
    public static final int ISA_ERROR_REC_INVALID_CHAR = 10112;  /* 0x2780 */
    public static final int ISA_ERROR_REC_URI_NOT_FOUND = 10113;  /* 0x2781 */
    public static final int ISA_ERROR_REC_URI_TIMEOUT = 10114;  /* 0x2782 */
    public static final int ISA_ERROR_REC_URI_FETCH_ERROR = 10115;  /* 0x2783 */

    /* Error codes of Speech Detector */
    public static final int ISA_ERROR_EP_GENERAL = 10200;  /* 0x27D8 */
    public static final int ISA_ERROR_EP_NO_SESSION_NAME = 10201;    /* 0x27D9 */
    public static final int ISA_ERROR_EP_INACTIVE = 10202;    /* 0x27DA */
    public static final int ISA_ERROR_EP_INITIALIZED = 10203;    /* 0x27DB */


    /*Exception codes of ISA40 speech analysis*/
    public static final int ISPERR_SCI_HANDLE_TIMEOUT = 888062;    /*处理超时，网络存在延迟或者CPU占用过满*/
    public static final int ISPERR_SCI_CONN = 888111;    /*引擎的某一节点连接不上*/
    public static final int ISPERR_ICE_EXCEPTION = 881411;    /*服务或代理端口问题*/
    public static final int ISPERR_SCI_CONN_TIMEOUT = 888110;    /*网络连接问题*/

    public static final int ISAERR_COM_INVALID_PARAM = 801022;    /*无效参数*/
    public static final int ISAERR_COM_NOT_INIT = 801203;    /*未初始化*/
    public static final int ISAERR_COM_INVALID_PARAM_VALUE = 801220;    /*无效参数值*/
    public static final int ISAERR_LIST_FILE_NOT_EXIST = 802002;    /*建索传入的列表文件不存在*/
    public static final int ISAERR_DIR_NOT_EXIST = 803002;    /*文件夹不存在*/
    public static final int ISAERR_WAVE_INVALID_TYPE = 804008;    /*语音格式无效、不支持*/
    public static final int ISAERR_WAVE_NO_DATA = 804061;    /*语音无有效数据*/
    public static final int ISAERR_WAVE_EMPTY = 804267;    /*语音为空，语音文件大小为0*/
    public static final int ISAERR_INDEX_NOT_EXIST = 805002;    /*索引不存在*/
    public static final int ISAERR_EP_NO_DATA = 806061;    /*Ep无数据*/
    public static final int ISAERR_DONGLE_NO_LIC = 809905;    /*加密狗初始化或逆初始化失败；狗信息无效；相应授权不存在*/
    public static final int ISAERR_DCD_NO_DATA = 810061;    /*解码器解码无数据*/
    public static final int ISPERR_SES_NOT_FOUND_SPX = 884002;    /*代理端或者服务端节点不存在*/
    public static final int ISPERR_SCI_NO_MORE_SVC = 888002;    /*索引不存在*/





    /*
     * QISRInit and QISRFini, these functions is optional.
     * To call them in some cases necessarily.
     */

    /**
     * @fn QISRInit
     * @brief Initialize API
     * <p>
     * Load API module with specified configurations.
     * @return ISRAPI   int   - Return 0 in success, otherwise return error code.
     * @param   configs String	- [in] configurations to initialize
     * @see
     */
    public native int QISRInit(String configs);


    /**
     * @fn QISRFini
     * @brief Uninitialize API
     * <p>
     * Unload API module, the last function to be called.
     * @return int ISRAPI	- Return 0 in success, otherwise return error code.
     * @see
     */
    public native int QISRFini();

    /**
     * @fn QISRWaveformBuild
     * @brief Build index from a Waveform File
     * <p>
     * Build index from a Waveform File
     * @return int            - return 0 on success, otherwise return error code.
     * @param  waveFile  String - [in]  waveform file uri to recognize
     * @param   waveFmt  String	    - [in]  waveform format string
     * @param   params  String	    - [in]  parameters
     * @param   recogStatus int[] 		- [out] recognize status
     * @see
     */
    public native int QISRWaveformBuild(String waveFile, String waveFmt, String params,
        int[] recogStatus);

    /**
     * @fn QISRWaveformSearch
     * @brief Search result from a Waveform File
     * <p>
     * Search result base on a wave file uri
     * @return String          - Return rec result in string format, NULL if failed, result is error code.
     * @param  waveFile  String - [in]  waveform file uri to recognize
     * @param  grammarList   String    - [in]  grammars used by recognizer
     * @param   params  String	    - [in]  parameters
     * @param   result  int[]		- [out] return 0 on success, otherwise return error code.
     * @see
     */
    public native String QISRWaveformSearch(String waveFile, String grammarList, String params,
        int[] result);

    /**
     * @fn QISRIndexMaintain
     * @brief Maintain Index
     * <p>
     * Maintain Index, e.g. "delete", "backup", "release" ...
     * @return int            - return 0 on success, otherwise return error code.
     * @param   idx_id    String	- [in]  index dir ID to store the indexs.
     * @param  action  String - [in]  maintain type. e.g. "delete", "backup", "release" ...
     * @param   act_params  String	- [in]  action params. e.g. "bakup" to dir etc.
     * @see
     */
    public native int QISRIndexMaintain(String idx_id, String action, String act_params);

    /**
     * load library
     **/
    static {
        System.loadLibrary("qisr");
    }

}
