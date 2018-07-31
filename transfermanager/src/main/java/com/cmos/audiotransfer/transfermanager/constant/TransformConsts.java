package com.cmos.audiotransfer.transfermanager.constant;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午3:25
 * Description:
 */
public class TransformConsts {
    public static final String KEY_SYMBOL_EQUAL = "=";
    public static final String KEY_SYMBOL_COMMA = ",";
    public static final String KEY_SYMBOL_UNDERLINE = "_";
    public static final String KEY_SYMBOL_WAVELINE = "~";

    public static final String SYMBOL_SUFFIX_TXT = ".txt";
    public static final String SYMBOL_SUFFIX_XML = ".xml";


    public static final String KEY_BEANNAME_ISAENGINEPOOL = "isaEnginePool";
    public static final String KEY_BEANNAME_ISAENGINECONFIGS = "isaEngineConfigs";
    public static final String KEY_BEANNAME_JSONRESULTCONFIGS = "jsonResultConfigs";
    public static final String KEY_BEANNAME_XMLRESULTPARSER = "xmlResultParser";


    public static final String KEY_ISA_CONFIG_APPID = "appId";
    public static final String KEY_ISA_CONFIG_FORMAT = "voiceFormat";
    public static final String KEY_ISA_CONFIG_CHANNELID= "channelId";

    public static final String KEY_ISA_CONFIG_RESOURCE_CODE = "resourceCode";
    public static final String KEY_ISA_CONFIG_MANUFACTURER = "manufacturer";
    public static final String KEY_ISA_CONFIG_LANG= "lang";
    public static final String KEY_ISA_CONFIG_ACOUS= "acous";
    public static final String KEY_ISA_CONFIG_EXTRABUNDLE= "extraBundle";
    public static final String KEY_ISA_CONFIG_HOTWORD= "hotWord";
    public static final String KEY_ISA_PARAMS_RESOURCE_CODE = "area";
    public static final String KEY_ISA_PARAMS_MANUFACTURER = "svc";
    public static final String KEY_ISA_PARAMS_LANG= "lang";
    public static final String KEY_ISA_PARAMS_ACOUS= "acous";
    public static final String KEY_ISA_PARAMS_EXTRABUNDLE= "small_wfst";
    public static final String KEY_ISA_PARAMS_HOTWORD= "hotword";
    public static final String SYMBOL_BUCKET_NAME_RESULT= "zx";

    public static final String LOCAL_VOICE_PATH =
        TransformConsts.class.getResource("/").getPath() + "voice";
}
