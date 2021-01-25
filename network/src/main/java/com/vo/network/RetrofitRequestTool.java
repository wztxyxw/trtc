package com.vo.network;

import android.text.TextUtils;

import com.chuangdu.library.util.AppUtils;
import com.chuangdu.library.util.SP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sc
 */
public class RetrofitRequestTool {

    private static Map<String, String> mHeaders;
    private static final String REQUEST_HEADERS = "REQUEST_HEADERS";
    private static final String SPLITER = "--qianxx--";
    private static final String KEY_TOKEN = "RetrofitRequestTool#KEY_TOKEN";
    private static final String KEY_DEVUUID = "RetrofitRequestTool#KEY_DEVUUID";
    private static final String KEY_USERID = "RetrofitRequestTool#KEY_USERID";
    private static final String KEY_APPTYPE = "RetrofitRequestTool#KEY_APPTYPE";
    private static final String KEY_ACCOUNT_ID = "RetrofitRequestTool#KEY_ACCOUNT_ID";
    private static final String KEY_DEV_TOKEN = "RetrofitRequestTool#KEY_DEV_TOKEN";
    private static final String KEY_LATITUDE = "RetrofitRequestTool#KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "RetrofitRequestTool#KEY_LONGITUDE";

    private static String mAppId;
    private static String key;
    private static String token;
    private static String userId;
    private static String accountId;
    private static String devToken;

    private static double latitude;
    private static double longitude;
    private static boolean show;

    private static int appType;

    private static String devUUID;


    public static double getLatitude() {
        return RetrofitRequestTool.latitude;
    }

    public static void setLatituden(double latitude) {
        RetrofitRequestTool.latitude = latitude;
    }

    public static double getLongitude() {
        return RetrofitRequestTool.longitude;
    }

    public static void setLongitude(double longitude) {
        RetrofitRequestTool.longitude = longitude;
    }

    public static String getDevToken(SP sp) {
        if (!TextUtils.isEmpty(devToken)) {
            return devToken;
        }
        devToken = sp.getString(KEY_DEV_TOKEN);
        return devToken;
    }

    public static void setDevToken(SP sp, String jpushRegistrationID) {
        RetrofitRequestTool.devToken = jpushRegistrationID;
        sp.putString(KEY_DEV_TOKEN, jpushRegistrationID);
    }

    public static boolean isShow() {
        return RetrofitRequestTool.show;
    }

    private static void setShow(boolean show) {
        RetrofitRequestTool.show = show;
    }


    public static String getAccountId(SP sp) {
        if (!TextUtils.isEmpty(accountId)) {
            return accountId;
        }
        accountId = sp.getString(KEY_ACCOUNT_ID);
        return accountId;
    }

    public static void setAccountId(SP sp, String accountId) {
        RetrofitRequestTool.accountId = accountId;
        sp.putString(KEY_ACCOUNT_ID, accountId);
    }

    public static String getUserId(SP sp) {
        if (!TextUtils.isEmpty(userId)) {
            return userId;
        }
        userId = sp.getString(KEY_USERID);
        return userId;
    }

    public static void setUserId(SP sp, String userId) {
        RetrofitRequestTool.userId = userId;
        sp.putString(KEY_USERID, userId);
    }

    public static int getAppType(SP sp) {
        if (appType != 0) {
            return appType;
        }
        appType = sp.getInt(KEY_APPTYPE, 1);
        return appType;
    }

    public static void setAppType(SP sp, int appType) {
        RetrofitRequestTool.appType = appType;
        sp.putInt(KEY_APPTYPE, appType);
        setShow(AppUtils.isShow());
    }

    public static String getDevUUID(SP sp) {
        if (!TextUtils.isEmpty(devUUID)) {
            return devUUID;
        }
        devUUID = sp.getString(KEY_DEVUUID);
        if (TextUtils.isEmpty(devUUID)) {
            devUUID = UUID.randomUUID().toString();
            setDevUUID(sp, devUUID);
        }
        return devUUID;
    }

    public static void setDevUUID(SP sp, String devUUID) {
        RetrofitRequestTool.devUUID = devUUID;
        sp.putString(KEY_DEVUUID, devUUID);
    }
    public static String getAppId() {
        return mAppId;
    }

    public static void setAppId(String appId) {
        mAppId = appId;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        RetrofitRequestTool.key = key;
    }

    public static void saveToken(SP sp, String token) {
        RetrofitRequestTool.token = token;
        sp.putString(KEY_TOKEN, token);
    }

    public static String getToken(SP sp) {
        return sp.getString(KEY_TOKEN);
    }

    public static Map<String, String> getHeaders(SP sp) {
        initHeaders(sp);
        return mHeaders;
    }

    public static void addHeader(SP sp, String key, String value) {
        initHeaders(sp);
        mHeaders.put(key, value);
        save(sp, mHeaders);
    }

    public static String getHeader(SP sp, String key) {
        initHeaders(sp);
        return mHeaders.get(key);
    }

    public static void setHeaders(SP sp, Map<String, String> headers) {
        initHeaders(sp);
        mHeaders.clear();
        for (String key : headers.keySet()) {
            mHeaders.put(key, headers.get(key));
        }
        save(sp, mHeaders);
    }

    public static void remove(SP sp, String key) {
        initHeaders(sp);
        if (mHeaders.containsKey(key)) {
            mHeaders.remove(key);
            save(sp, mHeaders);
        }
    }

    public static void removeAll(SP sp) {
        initHeaders(sp);
        mHeaders.clear();
        save(sp, mHeaders);
    }

    private static void save(SP sp, Map<String, String> headers) {
        Set<String> strings = new HashSet<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            strings.add(entry.getKey() + SPLITER + entry.getValue());
        }
        sp.putStringSet(REQUEST_HEADERS, strings);
    }

    private static void initHeaders(SP sp) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>(20);
            Set<String> strings = sp.getStringSet(REQUEST_HEADERS);
            for (String string : strings) {
                String[] sts = string.split(SPLITER);
                if (sts.length > 1) {
                    mHeaders.put(sts[0], sts[1]);
                }
            }
        }
    }
}
