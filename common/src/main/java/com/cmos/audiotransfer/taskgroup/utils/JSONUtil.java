package com.cmos.audiotransfer.taskgroup.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JSONUtil {
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
        return gson.fromJson(json, t);
    }

    public static <T> T toMap(String json){
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
