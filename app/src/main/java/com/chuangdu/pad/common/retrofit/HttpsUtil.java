package com.chuangdu.pad.common.retrofit;

import android.content.Context;

import com.chuangdu.suyangpad.BuildConfig;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.utils.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.inject.Inject;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author Robotke
 * @since 2018/9/22
 */
public class HttpsUtil {

    @Inject
    Context mContext;

    private static HttpsUtil mHttpsUtil;

    private HttpsUtil() {
        Application.getAppComponent().inject(this);
    }

    public static HttpsUtil getInstance() {
        if (mHttpsUtil == null) {
            mHttpsUtil = new HttpsUtil();
        }
        return mHttpsUtil;
    }

    /** 设置证书 */
    public SSLSocketFactory setCertificates() {
        try {
            InputStream inputStream = mContext.getAssets().open("ssl_certificate.pem");

            // 读取证书
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            // 创建一个证书库，并将证书导入证书库
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry(BuildConfig.FLAVOR, certificateFactory.generateCertificate(inputStream));
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 创建一个信任管理器，并将证书库导入
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            // 创建一个使用我们信任管理器的SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 双向认证 */
    public SSLSocketFactory setSSLCertificates() {
        try {
            //服务端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //客户端信任的服务端证书
            //KeyStore trustStore = KeyStore.getInstance("BKS");

            //读取证书
            InputStream ksIn = mContext.getAssets().open("client.p12");
            //InputStream tsIn = mContext.getAssets().open("truststore.bks");

            //加载证书
            try {
                keyStore.load(ksIn, ",./123qwe".toCharArray());
                //trustStore.load(tsIn, "123456".toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    IOUtil.closeStream(ksIn);
                    //IOUtil.closeStream(tsIn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, ",./123qwe".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

