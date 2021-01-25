package com.chuangdu.pad.common.retrofit;

import android.text.TextUtils;

import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.ToastUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import com.vo.network.converter.FastJsonConverterFactory;
import com.vo.network.interceptor.ReceivedInterceptor;
import com.vo.network.interceptor.RequestInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * BaseApi baseApi = new RequestUtil(BaseApi.class, "http://www.baidu.com", sp).getService();
 * <p>
 *
 * @author Robotke
 * @since 2018/9/22
 */
public class RetrofitUtil {
    private static final String TAG = RetrofitUtil.class.getSimpleName();
    private static final String TAG_OKHTTP = "okhttp";

    private RetrofitUtil() {

    }

    /**
     * @param tClass retrofitAPI 类
     * @param host   服务器地址（必须以 / 结尾）
     * @param sp     SharePreference 用于保存token、header等
     * @return
     */
    public static <T> T getService(Class<T> tClass, String host, SP sp) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ReceivedInterceptor(sp))
                .addInterceptor(new RequestInterceptor(sp))
                .addInterceptor(new HttpLoggingInterceptor(message -> {
                    //if (BuildConfig.DEBUG) {
                        if (message.length() > 4000) {
                            for (int i = 0; i < message.length(); i += 4000) {
                                if (i + 4000 < message.length()) {
                                    LogUtil.e(TAG_OKHTTP, message.substring(i, i + 4000));
                                } else {
                                    LogUtil.e(TAG_OKHTTP, message.substring(i));
                                }
                            }
                        } else {
                            LogUtil.e(TAG_OKHTTP, message);
                        }
                    //}
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
//                .addInterceptor(new EncryptInterceptor())
                ;

//        if (BuildConfig.FLAVOR_project.equals("chuangdu")) { // https双向认证
//            SSLSocketFactory sslSocketFactory = HttpsUtil.getInstance().setSSLCertificates();
//
//            if (sslSocketFactory != null) {
//                Log.e(RetrofitUtil.class.getSimpleName(), "sslSocketFactory--->");
//                builder.hostnameVerifier((hostname, session) -> hostname.equals(session.getPeerHost()))
//                        .sslSocketFactory(sslSocketFactory, new X509TrustManager() {
//                            @Override
//                            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                            }
//
//                            @Override
//                            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                            }
//
//                            @Override
//                            public X509Certificate[] getAcceptedIssuers() {
//                                return new X509Certificate[0];
//                            }
//                        });
//            }
//        } else if (BuildConfig.FLAVOR.equals("YueYue")) { // https单向认证
//            SSLSocketFactory sslSocketFactory = HttpsUtil.getInstance().setCertificates();
//            if (sslSocketFactory != null) {
//                builder.hostnameVerifier((hostname, session) -> hostname.equals(session.getPeerHost()))
//                        .sslSocketFactory(sslSocketFactory, new X509TrustManager() {
//                            @Override
//                            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                            }
//
//                            @Override
//                            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                            }
//
//                            @Override
//                            public X509Certificate[] getAcceptedIssuers() {
//                                return new X509Certificate[0];
//                            }
//                        });
//            }
//        }

        OkHttpClient okHttpClient = builder.build();

        if (TextUtils.isEmpty(host)) {
            host = "http://localhost/";
            LogUtil.e(TAG, "host is empty");
            ToastUtil.getInstance().toast("host is empty");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(okHttpClient)
                // 添加 fastJson 解析器
                .addConverterFactory(FastJsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                // 添加 RxJava 适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(tClass);
    }

    /**
     * Retrofit 使用 OkHttp 上传时的 RequestBody
     *
     * @param value 参数
     * @return
     */
    public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    /**
     * Retrofit 使用 OkHttp 上传时的 file 包装
     *
     * @param key  参数
     * @param file 文件名
     * @return
     */
    public static MultipartBody.Part getRequestPart(String key, File file) {
        RequestBody fileBody = MultipartBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(key, file.getName(), fileBody);
    }

    /**
     * @param key   参数
     * @param files 文件名列表
     * @return
     */
    public static MultipartBody.Part[] getRequestParts(String key, File... files) {
        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];
        for (int i = 0; i < files.length; i++) {
            parts[i] = getRequestPart(key, files[i]);
        }
        return parts;
    }
}

