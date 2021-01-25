package com.vo.network.interceptor;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.chuangdu.library.util.RsaUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sc
 */
public class EncryptInterceptor implements Interceptor {

    public EncryptInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // 添加 签名等
        Request original = chain.request();
        SortMap sortMap = new SortMap();

        // FormBody
        FormBody.Builder newFormBody = new FormBody.Builder();
        if (original.body() instanceof FormBody) {
            FormBody oldFormBody = (FormBody) original.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                String key = oldFormBody.encodedName(i);
                String value = oldFormBody.encodedValue(i);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    sortMap.put(URLDecoder.decode(key, "utf-8"), URLDecoder.decode(value, "utf-8"));
                }
            }
        }

        final Request.Builder builder = chain.request().newBuilder();
        try {
            newFormBody.add("params",
                    RsaUtil.encodeURL(RsaUtil.encryptByPublic(getRSASign(sortMap), RsaUtil.PUBLIC_KEY_DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (original.body() instanceof FormBody) {
            builder.method(original.method(), newFormBody.build());
        }

        return chain.proceed(builder.build());
    }

    private class SortMap extends TreeMap<String, String> {
        SortMap() {
            // 升序排序
            super((obj1, obj2) -> -obj2.compareTo(obj1));
        }
    }

    private static String getRSASign(Map<String, String> map) {
        return JSON.toJSONString(map);
    }
}
