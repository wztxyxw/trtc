package com.chuangdu.library.network;

import com.chuangdu.library.BuildConfig;
import com.chuangdu.library.util.LogUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private static boolean DEBUG = BuildConfig.DEBUG;

    private static final int CONNECT_TIMEOUT = 180000;
    private static final int READ_TIMEOUT = 180000;

    private static OkHttpClient sOkHttpClient = new OkHttpClient();
    public static Converter.Factory sGsonConverterFactory = GsonConverterFactory.create();
    public static CallAdapter.Factory sRxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    private static SSLSocketFactory sSslSocketFactory;

    public static void init(boolean debug, String cer) {
//        sSslSocketFactory = CloudSSLSocketFactory.getSSLSocketFactory(cer);
        DEBUG = debug;
    }

    // 简单的log : Level.BASIC, 详细的log: Level.BODY
    public static OkHttpClient getHttpClient() {
        return getHttpClient(HttpLoggingInterceptor.Level.BASIC);
    }

    //Fresco 自定义http
    public static OkHttpClient getFrescoHttpClient(HttpLoggingInterceptor.Level level) {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        SSLSocketFactory sslSocketFactory = trustAllHosts();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(DO_NOT_VERIFY);

        // 设置request 拦截器
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        builder.addInterceptor(requestInterceptor);

        // 设置log拦截器
        if (DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log leave
            logging.setLevel(level);
            //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            // add your other interceptors …
            builder.interceptors().add(logging);
            // add logging as last interceptor
        }
        return builder.build();
    }

    public static OkHttpClient getHttpClient(HttpLoggingInterceptor.Level level) {
        OkHttpClient.Builder builder = sOkHttpClient.newBuilder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            if (sSslSocketFactory == null) {
                SSLSocketFactory sslSocketFactory = trustAllHosts();
                builder.sslSocketFactory(sslSocketFactory);
            } else {
                builder.sslSocketFactory(sSslSocketFactory);
            }

            builder.hostnameVerifier(DO_NOT_VERIFY);
            if (sSslSocketFactory != null) {

            }
        } catch (Exception e) {
            LogUtil.e("Api", "getHttpClient e:" + e);

        } finally {

        }

        // 设置request 拦截器
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        builder.addInterceptor(requestInterceptor);

        // 设置log拦截器
        if (DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log leave
            logging.setLevel(level);
            //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            // add your other interceptors …
            builder.interceptors().add(logging);
            // add logging as last interceptor
        }
        return builder.build();
    }
    public static OkHttpClient getWorkHttpClient(HttpLoggingInterceptor.Level level, String token, String account ) {
        OkHttpClient.Builder builder = sOkHttpClient.newBuilder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            if (sSslSocketFactory == null) {
                SSLSocketFactory sslSocketFactory = trustAllHosts();
                builder.sslSocketFactory(sslSocketFactory);
            } else {
                builder.sslSocketFactory(sSslSocketFactory);
            }

            builder.hostnameVerifier(DO_NOT_VERIFY);
            if (sSslSocketFactory != null) {

            }
        } catch (Exception e) {
            LogUtil.e("Api", "getHttpClient e:" + e);

        } finally {

        }

        // 设置request 拦截器
        WorkRequestInterceptor requestInterceptor = new WorkRequestInterceptor(token, account);
        builder.addInterceptor(requestInterceptor);

        // 设置log拦截器
        if (DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log leave
            logging.setLevel(level);
            //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            // add your other interceptors …
            builder.interceptors().add(logging);
            // add logging as last interceptor
        }
        return builder.build();
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private static SSLSocketFactory trustAllHosts() {
        SSLSocketFactory sslSocketFactory = null;
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }


    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * This interceptor compresses the HTTP request body. Many webservers can't handle this!
     */
    final static class RequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request compressedRequest = originalRequest.newBuilder()
                    .header("X-Api-Version", "1.0.0")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("master_key", "d6d2f6b08feb46499b15837bed03e808")
                    .build();
            return chain.proceed(compressedRequest);
        }
    }
    final static class WorkRequestInterceptor implements Interceptor {
        String token;
        String account;
        public WorkRequestInterceptor(String token, String account){
            this.token = token;
            this.account = account;
        }
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request compressedRequest;
            if (null != token){
                compressedRequest = originalRequest.newBuilder()
                        .header("X-Api-Version", "1.0.0")
                        .addHeader("master_key", "d6d2f6b08feb46499b15837bed03e808")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("token", token)
                        .addHeader("account", account)
                        .build();
            } else {
                compressedRequest = originalRequest.newBuilder()
                        .header("X-Api-Version", "1.0.0")
                        .addHeader("master_key", "d6d2f6b08feb46499b15837bed03e808")
                        .build();
            }
            return chain.proceed(compressedRequest);
        }
    }

    public static void setDEBUG(boolean DEBUG) {
        HttpManager.DEBUG = DEBUG;
    }

    //下载客户端，可以监听进度
    public static OkHttpClient getHttpDownloadClient(String url, HttpLoggingInterceptor.Level level,
                                                     ProgressListener listener) {
        OkHttpClient.Builder builder = sOkHttpClient.newBuilder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        try {
            URL urlCon = new URL(url);
            if (urlCon.getProtocol().toLowerCase().equals("https")) {
                if (sSslSocketFactory == null) {
                    trustAllHosts();
                }

                builder.hostnameVerifier(DO_NOT_VERIFY);
                if (sSslSocketFactory != null) {
                    builder.sslSocketFactory(sSslSocketFactory);
                }
            }
        } catch (IOException e) {
            LogUtil.e("Api", "getHttpClient e:" + e);

        } finally {

        }

        // 设置request 拦截器
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        builder.addInterceptor(requestInterceptor);
        builder.addInterceptor(new DownloadProgressInterceptor(listener));

        // 设置log拦截器
        if (DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log leave
            logging.setLevel(level);
            //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            // add your other interceptors …
            builder.interceptors().add(logging);
            // add logging as last interceptor
        }
        return builder.build();
    }
}
