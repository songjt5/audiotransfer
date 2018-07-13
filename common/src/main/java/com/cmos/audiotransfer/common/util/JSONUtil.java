package com.cmos.audiotransfer.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;

public class JSONUtil {
    public static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
    private static Gson gson;


    static {
        if (gson == null) {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls()
                .setDateFormat("yyyyMMddHHmmss").setPrettyPrinting().disableHtmlEscaping().create();
        }
    }

    public static String toJSON(Object obj) {
        return gson.toJson(obj);
    }


    public static <T> T fromJson(String json, Class<T> t) {

        try {
            return gson.fromJson(json, t);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toMap(String json) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            logger.error("task content parse error!", e);
            return null;
        }
    }

}
