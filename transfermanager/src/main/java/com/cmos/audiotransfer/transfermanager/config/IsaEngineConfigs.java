package com.cmos.audiotransfer.transfermanager.config;

import com.cmos.audiotransfer.common.exception.NeedNecessaryInfoException;
import com.cmos.audiotransfer.transfermanager.constant.TransformConsts;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午5:29
 * Description:
 */
@Component @ConfigurationProperties(prefix = "isa") public class IsaEngineConfigs {

    private static final Logger logger = LoggerFactory.getLogger(IsaEngineConfigs.class);
    private String searchParams;

    private String xmlOption;

    private String maxEngine;

    private String appId;

    private List<Map<String, String>> configs;

    private Map<String, String> isaParams = new HashMap<>();
    private Map<String, String> voiceFormat = new HashMap<>();



    @PostConstruct public void init() {
        try {
            checkSettings();
            configs.forEach(p -> {
                StringBuilder isaParamsBuilder =
                    new StringBuilder(TransformConsts.KEY_ISA_CONFIG_APPID)
                        .append(TransformConsts.KEY_SYMBOL_EQUAL).append(appId);
                appendManufacturer(isaParamsBuilder, p);
                appendResourceCode(isaParamsBuilder, p);
                appendLang(isaParamsBuilder, p);
                appendAcous(isaParamsBuilder, p);
                appendExtraBundle(isaParamsBuilder, p);
                appendHotWord(isaParamsBuilder, p);
                this.isaParams.put(p.get(TransformConsts.KEY_ISA_CONFIG_RESOURCE_CODE),
                    isaParamsBuilder.toString());
                voiceFormat.put(p.get(TransformConsts.KEY_ISA_CONFIG_RESOURCE_CODE),
                    p.get(TransformConsts.KEY_ISA_CONFIG_FORMAT));
            });
        } catch (Exception e) {
            logger.error("init failed", e);
        }
    }


    public String getVoiceFormat(String resourceCode) {
        return voiceFormat.get(resourceCode);
    }

    public String getIsaParamStr(String resourceCode) {
        return isaParams.get(resourceCode);
    }


    private void checkSettings() {
        if (StringUtils.isEmpty(appId)) {
            throw new NeedNecessaryInfoException("isa.appId");
        }
        if (StringUtils.isEmpty(searchParams)) {
            throw new NeedNecessaryInfoException("isa.searchParams");
        }
        if (StringUtils.isEmpty(maxEngine)) {
            throw new NeedNecessaryInfoException("isa.maxEngine");
        }
        if (CollectionUtils.isEmpty(configs)) {
            throw new NeedNecessaryInfoException("isa.configs");
        }
    }


    private void appendManufacturer(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_MANUFACTURER))) {
            throw new NeedNecessaryInfoException("manufacturer");
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA)
            .append(TransformConsts.KEY_ISA_PARAMS_MANUFACTURER)
            .append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_MANUFACTURER));
    }

    private void appendResourceCode(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_RESOURCE_CODE))) {
            throw new NeedNecessaryInfoException("resourceCode");
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA)
            .append(TransformConsts.KEY_ISA_PARAMS_RESOURCE_CODE)
            .append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_RESOURCE_CODE));
    }

    private void appendLang(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_LANG))) {
            throw new NeedNecessaryInfoException("lang");
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA).append(TransformConsts.KEY_ISA_PARAMS_LANG)
            .append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_LANG));
    }

    private void appendAcous(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_ACOUS))) {
            throw new NeedNecessaryInfoException("acous");
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA)
            .append(TransformConsts.KEY_ISA_PARAMS_ACOUS).append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_ACOUS));
    }

    private void appendExtraBundle(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_EXTRABUNDLE))) {
            return;
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA)
            .append(TransformConsts.KEY_ISA_PARAMS_EXTRABUNDLE)
            .append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_EXTRABUNDLE));
    }

    private void appendHotWord(StringBuilder builder, Map<String, String> configs) {
        if (StringUtils.isEmpty(configs.get(TransformConsts.KEY_ISA_CONFIG_HOTWORD))) {
            return;
        }
        builder.append(TransformConsts.KEY_SYMBOL_COMMA)
            .append(TransformConsts.KEY_ISA_PARAMS_HOTWORD).append(TransformConsts.KEY_SYMBOL_EQUAL)
            .append(configs.get(TransformConsts.KEY_ISA_CONFIG_HOTWORD));
    }



    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<Map<String, String>> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Map<String, String>> configs) {
        this.configs = configs;
    }

    public String getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(String searchParams) {
        this.searchParams = searchParams;
    }

    public boolean getXmlOption() {
        return Boolean.parseBoolean(xmlOption);
    }

    public void setXmlOption(String xmlOption) {
        this.xmlOption = xmlOption;
    }

    public String getMaxEngine() {
        return maxEngine;
    }

    public void setMaxEngine(String maxEngine) {
        this.maxEngine = maxEngine;
    }
}
