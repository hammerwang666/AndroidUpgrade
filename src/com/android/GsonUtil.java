package com.android;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Author: zengfan@ucweb.com
 * Usage: Gson工具
 * Date: 14/11/17
 */
public abstract class GsonUtil {

    private static final ThreadLocal<Gson> GSONS = new ThreadLocal<>();

    public static String toJson(Object src) {
        if(src == null) {
            return null;
        }

        return getGson().toJson(src);
    }

    public static String toJsonSupportExpose(Object src) {
        if (src == null) {
            return null;
        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属�?
                .disableHtmlEscaping().create();

        return gson.toJson(src);
    }

    public static String toJson(Object src, Type type) {
        if(src == null) {
            return null;
        }

        return getGson().toJson(src, type);
    }

    public static String toJsonWithDateformat(Object src) {
        if(src == null) {
            return null;
        }

        return getGsonWithDateformat().toJson(src);
    }

    public static String toJson(Object src, JsonSerializer<?>... adapters) {
        if(src == null) {
            return null;
        }

        GsonBuilder gson = new GsonBuilder();
        for (Object adapter : adapters) {
            Type t = ReflectionUtil.getMethod(adapter.getClass(), "serialize").getParameterTypes()[0];
            gson.registerTypeAdapter(t, adapter);
        }
        gson.disableHtmlEscaping();
        return gson.create().toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(json, e);
        }
    }

    public static <T> T fromJsonWithDateformat(String json, Class<T> classOfT) {
        try {
            return getGsonWithDateformat().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(json, e);
        }
    }

    public static <T> T fromJsonWithDateformat(String json, Type type) {
        try {
            return getGsonWithDateformat().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(json, e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return getGson().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(json, e);
        }
    }

    private static Gson getGson() {
        Gson gson = GSONS.get();
        if(gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().create();
            GSONS.set(gson);
        }
        return gson;
    }

    private static Gson getGsonWithDateformat() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        return gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
    }
}
