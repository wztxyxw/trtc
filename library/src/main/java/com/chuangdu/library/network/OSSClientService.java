package com.chuangdu.library.network;//package com.sc.library.network;
//
//import android.content.Context;
//
//import com.alibaba.sdk.android.oss.ClientConfiguration;
//import com.alibaba.sdk.android.oss.OSS;
//import com.alibaba.sdk.android.oss.OSSClient;
//import com.alibaba.sdk.android.oss.common.OSSLog;
//import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
//import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
//
//public class OSSClientService {
//    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
//    private static String accessId = "rpMhOwtljABEoMIw";
//    private static String accessSecret = "62qfpEkIGB9RmH9m26jwBAGy8dAPJ5";
//    private static OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessId, accessSecret);
//    static ClientConfiguration conf = new ClientConfiguration();
//
//    static {
//        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
//        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
//        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
//        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.enableLog();
//    }
//
//    private static OSS oss;
//
//    public static OSS getOssClient(Context context) {
//        if (oss == null) {
//            oss = new OSSClient(context.getApplicationContext(), endpoint, credentialProvider, conf);
//        }
//        return oss;
//    }
//
//}
