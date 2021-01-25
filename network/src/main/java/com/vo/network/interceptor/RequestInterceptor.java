package com.vo.network.interceptor;

import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.chuangdu.library.app.BaseApplication;
import com.chuangdu.library.util.ChannelUtil;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.security.EncryptionUtil;
import com.vo.network.RetrofitRequestTool;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author sc
 */
public class RequestInterceptor implements Interceptor {

    private static final String TAG = RequestInterceptor.class.getSimpleName();
    private static final String KEY_APPID = "appid";
    private static final String KEY_DEVUUID = "devUUID";
    private static final String KEY_NONCESTR = "noncestr";
    private static final String KEY_SIGN = "sign";
    private static final String KEY_GRAY_VERSION_PREFIX = "V_";

    /**
     * 设备推送token 用于极光推送
     */
    private static final String KEY_DEV_TOKEN = "devToken";
    /**
     * 接入类型设备类型 1 Android 2 Pad 3 IOS 4 iPad 5 H5 6 pc 7外部appID用户 8app内部的h5 9微信
     */
    private static final String KEY_DEV_TYPE = "devType";
    /**
     * 终端系统名称
     */
    private static final String KEY_CLIENT_SYSTEM_NAME = "clientSystemName";

    /**
     * 设备型号
     */
    private static final String KEY_DEV_MODEL = "devModel";

    /**
     * 设备系统版本
     */
    private static final String KEY_OS_VERSION = "devOsVersion";

    /**
     * 软件应用版本
     */
    private static final String KEY_APPLY_VERSION = "applyVersion";

    /**
     * 经度
     */
    private static final String KEY_LONGITUDE = "longitude";

    /**
     * 维度
     */
    private static final String KEY_LATITUDE = "latitude";

    /**
     * 用户id
     */
    private static final String KEY_USER_ID = "userId";

    /**
     * 登录之后授权访问api接口的token
     */
    private static final String KEY_AUTHORIZATION= "authorization";

    /**
     * 1司机 2货主
     */
    private static final String KEY_APP_TYPE = "appType";

    /**操作者账户id*/
    private static final String KEY_ACCOUNT_ID = "accountId";

    private final SP mSP;

    public RequestInterceptor(SP sp) {
        mSP = sp;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        // 添加头部 token 等
        final Request.Builder builder = chain.request().newBuilder();

        // 添加 签名等
        Request original = chain.request();
        if ("POST".equals(original.method())) {
//            String appId = RetrofitRequestTool.getAppId();
//            if (appId != null) {
//                builder.addHeader(KEY_APPID, appId);
//            }
//            String devUUID = RetrofitRequestTool.getDevUUID(mSP);
//            if (devUUID != null) {
//                builder.addHeader(KEY_DEVUUID, devUUID);
//            }
//
//            builder.addHeader(KEY_APP_TYPE, String.valueOf(RetrofitRequestTool.getAppType(mSP)));
//
//            String devToken = RetrofitRequestTool.getDevToken(mSP);
//            if (devToken != null) {
//                builder.addHeader(KEY_DEV_TOKEN, devToken);
//            }
//
//
//
//            builder.addHeader(KEY_CLIENT_SYSTEM_NAME, "android");
//            builder.addHeader(KEY_DEV_MODEL, Build.MODEL);
//            builder.addHeader(KEY_OS_VERSION, Build.VERSION.RELEASE);
//
//
//            boolean show = !RetrofitRequestTool.isShow();
//
//            double latitude = RetrofitRequestTool.getLatitude();
//            double longitude = RetrofitRequestTool.getLongitude();
//
//            if (latitude > 0) {
//                if (show) {
//                    builder.addHeader(KEY_LATITUDE, String.valueOf(latitude));
//                } else {
//                    builder.addHeader(KEY_LATITUDE, "32.107555");
//                }
//            } else {
//                builder.addHeader(KEY_LATITUDE, "");
//            }
//            if (longitude > 0) {
//                if (show) {
//                    builder.addHeader(KEY_LONGITUDE, String.valueOf(longitude));
//                } else {
//                    builder.addHeader(KEY_LONGITUDE, "118.873999");
//                }
//            } else {
//                builder.addHeader(KEY_LONGITUDE, "");
//            }
//
//            builder.addHeader(KEY_DEV_TYPE, "1");
//
//            // noncestr
//            String noncestr = System.currentTimeMillis() + "";
//            builder.addHeader(KEY_NONCESTR, noncestr);
//            // token
//            String token = RetrofitRequestTool.getToken(mSP);
//            if (token != null) {
//                builder.addHeader(KEY_AUTHORIZATION, token);
//            }
            // userid
            String userid = RetrofitRequestTool.getUserId(mSP);
            if (userid != null) {
                builder.addHeader(KEY_USER_ID, userid);
            }

            String accountId = RetrofitRequestTool.getAccountId(mSP);
            if (accountId != null) {
                builder.addHeader(KEY_ACCOUNT_ID, accountId);
            }



            // grayVersion
            builder.addHeader(KEY_APPLY_VERSION, KEY_GRAY_VERSION_PREFIX + ChannelUtil.getVersionName(BaseApplication.getInstance()));
            // 生成加密 header 签名
            JSONObject headJson = new JSONObject(true);
//            headJson.put(KEY_APPID, appId);
//            headJson.put(KEY_DEVUUID, devUUID);
//            headJson.put(KEY_NONCESTR, noncestr);
            headJson.put(KEY_APPLY_VERSION, KEY_GRAY_VERSION_PREFIX + ChannelUtil.getVersionName(BaseApplication.getInstance()));
            headJson.put("key", RetrofitRequestTool.getKey());
            // read requestBody content
            Buffer buffer = new Buffer();
            original.body().writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = MediaType.parse("application/json; charset=UTF-8");
            if (contentType != null) {
                charset = contentType.charset();
            }
            String params = buffer.readString(charset);
            // 添加 sign
            builder.addHeader(KEY_SIGN, getNewSign(headJson.toString(), params));
            builder.addHeader("Content-Type", "application/json; charset=UTF-8");
            String url = URLDecoder.decode(original.url().toString(), "utf-8");

        }

        return chain.proceed(builder.build());
    }

    private class SortMap extends TreeMap<String, String> {
        public SortMap() {
            // 升序排序
            super((obj1, obj2) -> -obj2.compareTo(obj1));
        }
    }

    private static String getClientSign(Map<String, String> map) {
        StringBuilder params = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String val = map.get(key);
            params.append(key);
            params.append("=");
            params.append(val);
            params.append("&");
        }
        params.append("key=" + RetrofitRequestTool.getKey());
        return EncryptionUtil.md5Encode(params.toString()).toUpperCase();
    }

    private static String getNewSign(String headParam, String param) {
        return EncryptionUtil.md5Encode(headParam + param).toUpperCase();
    }
}
