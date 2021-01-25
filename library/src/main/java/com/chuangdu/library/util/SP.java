/*
    ShengDao Android Client, PreferencesManager
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.chuangdu.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.chuangdu.library.util.encode.AuthUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础参数的操作
 * @author sc
 */
public class SP {

    private static final String DB_NAME = "qianxx";
    private SharedPreferences sp;

    public SP(Context context) {
        sp = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取SP的实例
     */
    public SharedPreferences getSP() {
        return sp;
    }

    /**
     * 获取字符串
     *
     * @param key key值，要求全局唯一
     * @return 返回 Key 对应的 value，默认为 null
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取字符串
     *
     * @param key      key值，要求全局唯一
     * @param defValue 默认值
     * @return 返回 Key 对应的 value，默认为 null
     */
    public String getString(String key, String defValue) {
        return AuthUtil.aesDecrypt(sp.getString("String-" + key, defValue));
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("String-" + key, AuthUtil.aesEncrypt(value));
        editor.apply();
    }

    /**
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        return sp.getBoolean("Boolean-" + key, false);
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean("Boolean-" + key, defValue);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("Boolean-" + key, value);
        editor.apply();
    }

    public Float getFloat(String key) {
        return sp.getFloat("Float-" + key, 0f);
    }

    public void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Float-" + key, value);
        editor.apply();
    }

    public Integer getInt(String key, int defaultValue) {
        return sp.getInt("Int-" + key, defaultValue);
    }

    public Integer getInt(String key) {
        return getInt(key, 0);
    }

    public void putInt(String key, Integer value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Int-" + key, value);
        editor.apply();
    }

    public Long getLong(String key) {
        return sp.getLong("Long-" + key, 0);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("Long-" + key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key) {
        return sp.getStringSet("StringSet-" + key, new HashSet<>());
    }

    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("StringSet-" + key, value);
        editor.apply();
    }

    /**
     * 使用 fastJson 转成 json 数据存储
     *
     * @param key
     * @param value
     */
    public void putObject(String key, Object value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Object-" + key, value == null ? null : JSON.toJSONString(value));
        editor.apply();
    }

    /**
     * 获取存储的对象, 获取不到则返回 null
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        String value = sp.getString("Object-" + key, null);
        if (null == value) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    /**
     * 清除对象
     *
     * @param key
     */
    public void removeObject(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("Object-" + key);
        editor.commit();
    }
    /**
     * 清除对象
     *
     * @param key
     */
    public void removeList(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("List-" + key);
        editor.commit();
    }

    /**
     * 使用 fastJson 转成 json 数据存储
     *
     * @param key
     * @param value
     */
    public void putList(String key, List value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("List-" + key, value == null ? null : AuthUtil.aesEncrypt(JSON.toJSONString(value)));
        editor.apply();
    }

    /**
     * 获取存储的对象列表, 获取不到则返回 null
     *
     * @param <T>
     * @param key
     * @return
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = sp.getString("List-" + key, null);
        if (null == value) {
            return null;
        }
        return JSON.parseArray(AuthUtil.aesDecrypt(value), clazz);
    }
}