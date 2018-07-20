package com.cmos.audiotransfer.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-19
 * Time: 下午5:13
 * Description:
 */
public class YamlUtils {

    private static Logger logger = LoggerFactory.getLogger(YamlUtils.class);

    public static boolean addNode(String sourceStr, Map<String, Object> yamlData, String key,
        Object value) {

        Object source = getNode(sourceStr, yamlData);
        if (source == null) {
            return false;
        }
        try {
            if (key == null) {
                ((List<Object>) source).add(value);
            } else {
                ((Map<String, Object>) source).put(key, value);
            }
        } catch (Exception e) {
            logger.error("add node failed!", e);
            return false;
        }
        return true;
    }

    public static boolean deleteNode(String sourceStr, Map<String, Object> yamlData, String key,
        String value) {

        Object source = getNode(sourceStr, yamlData);
        if (source == null) {
            return false;
        }
        try {
            if (value == null) {
                ((Map<String, Object>) source).remove(key);
            } else {
                List<Object> nodeList = ((List<Object>) source);
                int i;
                for (i = 0; i < nodeList.size(); i++) {
                    if (value.equals(((Map<String, Object>) nodeList.get(i)).get(key))) {
                        nodeList.remove(i);
                        break;
                    }
                }
                if (i == nodeList.size()) {
                    logger.error("can't find the removing object");
                    return false;
                }

            }

        } catch (Exception e) {
            logger.error("delete node failed!", e);
            return false;
        }
        return true;
    }


    public static boolean updateNode(String sourceStr, Map<String, Object> yamlData, String key,
        Object value) {

        Object source = getNode(sourceStr, yamlData);
        if (source instanceof List) {
            List<Object> nodeList = (List<Object>) source;
            int i;
            for (i = 0; i < nodeList.size(); i++) {
                if (((Map<String, Object>) value).get(key)
                    .equals(((Map<String, Object>) nodeList.get(i)).get(key))) {
                    nodeList.remove(i);
                    nodeList.add(value);
                    break;
                }
            }
            if (i == nodeList.size()) {
                logger.error("can't find the removing object");
                return false;
            }
        } else if (source instanceof Map) {
            Map<String, Object> nodeMap = (Map<String, Object>) source;
            if (nodeMap.containsKey(key)) {
                nodeMap.put(key, value);
            } else {
                logger.error("can't find node:" + key);
                return false;
            }
        } else {
            logger.error("illegal source data!");
            return false;
        }
        return true;
    }

    public static Object getNode(String nodeName, Map<String, Object> yamlData) {

        String[] nodes = nodeName.split(".");
        Object data = yamlData;
        for (String nodeStr : nodes) {
            if (data == null) {
                logger.error("the resource data is null:" + nodeStr);
                return null;
            }
            int start = nodeStr.indexOf("[");
            try {
                if (start == 0) {
                    data = ((Map<String, Object>) data).get(nodeStr);
                } else {
                    int end = nodeStr.indexOf("]");
                    if (end - start > 1) {
                        data = ((List<Object>) ((Map<String, Object>) data)
                            .get(nodeStr.substring(0, start - 1)))
                            .get(Integer.parseInt(nodeStr.substring(start, end)));
                    } else {
                        logger.error("illegal name " + nodeName);
                        return null;
                    }

                }
            } catch (Exception e) {
                logger.error("can't get data :" + nodeStr);
            }
        }
        return data;
    }




}
